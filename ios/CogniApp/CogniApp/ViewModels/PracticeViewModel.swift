import Foundation
import Combine
import SwiftUI

/// Observable ViewModel managing the cognitive practice session for CogniApp on iOS & iPadOS.
/// Parallels Android PracticeViewModel with 100% logic and state parity.
public final class PracticeViewModel: ObservableObject {

    private let deckRepository = PracticeDeckRepository()
    private var timerCancellable: AnyCancellable?
    private var lastAdvanceTimestamp: TimeInterval = 0

    public static let advanceDebounceThreshold: TimeInterval = 0.40 // 400ms guard
    public static let defaultSeconds: Int = 15

    // MARK: - Published State
    @Published public var isSessionStarted: Bool = false
    @Published public var isDarkMode: Bool = false
    @Published public var currentDeck: PracticeDeck
    @Published public var availableDecks: [PracticeDeck]
    @Published public var cards: [PracticeCard] = []
    @Published public var currentIndex: Int = 0

    @Published public var userAnswerInput: String = ""
    @Published public var isListening: Bool = false
    @Published public var speechStatusMessage: String? = nil
    @Published public var feedbackState: FeedbackState = .idle

    @Published public var isAnswerRevealed: Bool = false
    @Published public var timerEnabled: Bool = false
    @Published public var secondsRemaining: Int = PracticeViewModel.defaultSeconds
    @Published public var isTimerPaused: Bool = false

    @Published public var isMusicEnabled: Bool = false
    @Published public var isHapticsAndSoundEnabled: Bool = true

    @Published public var isSessionComplete: Bool = false
    @Published public var sessionResults: [SessionCardResult] = []

    // MARK: - Initializer
    public init() {
        let initialDeck = deckRepository.getDefaultDeck()
        self.currentDeck = initialDeck
        self.availableDecks = deckRepository.getAllDecks()
        self.cards = initialDeck.cards.shuffled()
    }

    // MARK: - Computed Properties
    public var currentCard: PracticeCard? {
        guard currentIndex >= 0 && currentIndex < cards.count else { return nil }
        return cards[currentIndex]
    }

    public var deckTitle: String {
        currentDeck.title
    }

    public var totalCards: Int {
        cards.count
    }

    public var cardProgressText: String {
        "Card \(currentIndex + 1) of \(totalCards)"
    }

    public var matchedCount: Int {
        sessionResults.filter { $0.outcome == .matched }.count
    }

    public var revealedCount: Int {
        sessionResults.filter { $0.outcome == .revealed }.count
    }

    public var skippedCount: Int {
        sessionResults.filter { $0.outcome == .skipped }.count
    }

    public var palette: CogniThemePalette {
        CogniThemePalette(isDark: isDarkMode)
    }

    // MARK: - Session Flow
    public func startPractice() {
        stopTimer()
        let shuffled = currentDeck.cards.shuffled()
        self.cards = shuffled
        self.currentIndex = 0
        self.userAnswerInput = ""
        self.isListening = false
        self.speechStatusMessage = nil
        self.feedbackState = .idle
        self.isAnswerRevealed = false
        self.timerEnabled = false
        self.secondsRemaining = Self.defaultSeconds
        self.isTimerPaused = false
        self.isSessionComplete = false
        self.sessionResults = []
        self.isSessionStarted = true

        CogniAudioController.shared.playSubtleTap(hapticsAndSoundEnabled: isHapticsAndSoundEnabled)
    }

    public func returnToHome() {
        stopTimer()
        SpeechRecognizerManager.shared.stopListening()
        self.isSessionStarted = false
        self.currentIndex = 0
        self.userAnswerInput = ""
        self.isListening = false
        self.speechStatusMessage = nil
        self.feedbackState = .idle
        self.isAnswerRevealed = false
        self.timerEnabled = false
        self.secondsRemaining = Self.defaultSeconds
        self.isTimerPaused = false
        self.isSessionComplete = false
        self.sessionResults = []

        CogniAudioController.shared.playSubtleTap(hapticsAndSoundEnabled: isHapticsAndSoundEnabled)
    }

    public func selectDeck(id: String) {
        guard let deck = deckRepository.getDeck(id: id) else { return }
        stopTimer()
        SpeechRecognizerManager.shared.stopListening()
        self.currentDeck = deck
        self.cards = deck.cards.shuffled()
        self.currentIndex = 0
        self.userAnswerInput = ""
        self.isListening = false
        self.speechStatusMessage = nil
        self.feedbackState = .idle
        self.isAnswerRevealed = false
        self.timerEnabled = false
        self.secondsRemaining = Self.defaultSeconds
        self.isTimerPaused = false
        self.isSessionComplete = false
        self.sessionResults = []

        CogniAudioController.shared.playSubtleTap(hapticsAndSoundEnabled: isHapticsAndSoundEnabled)
    }

    public func restartDeck() {
        stopTimer()
        SpeechRecognizerManager.shared.stopListening()
        self.cards = currentDeck.cards.shuffled()
        self.currentIndex = 0
        self.userAnswerInput = ""
        self.isListening = false
        self.speechStatusMessage = nil
        self.feedbackState = .idle
        self.isAnswerRevealed = false
        self.secondsRemaining = Self.defaultSeconds
        self.isTimerPaused = false
        self.isSessionComplete = false
        self.sessionResults = []

        if timerEnabled {
            startTimer()
        }
        CogniAudioController.shared.playSubtleTap(hapticsAndSoundEnabled: isHapticsAndSoundEnabled)
    }

    // MARK: - Answer Submission & Forgiving Matching
    public func updateAnswerInput(_ text: String) {
        self.userAnswerInput = text
        if feedbackState == .tryAgain {
            feedbackState = .idle
        }
        speechStatusMessage = nil
    }

    public func submitAnswer() {
        guard let card = currentCard, isSessionStarted, !isSessionComplete else { return }
        let input = userAnswerInput.trimmingCharacters(in: .whitespacesAndNewlines)
        guard !input.isEmpty else { return }

        let isMatch = Self.isForgivingAnswerMatch(input: input, target: card.correctAnswer)

        if isMatch {
            stopTimer()
            feedbackState = .matched
            isAnswerRevealed = true
            speechStatusMessage = nil
            CogniAudioController.shared.playMatchSuccess(hapticsAndSoundEnabled: isHapticsAndSoundEnabled)
        } else {
            feedbackState = .tryAgain
            speechStatusMessage = nil
            CogniAudioController.shared.playTryAgain(hapticsAndSoundEnabled: isHapticsAndSoundEnabled)
        }
    }

    public func revealAnswer() {
        guard isSessionStarted, !isSessionComplete, !isAnswerRevealed else { return }
        stopTimer()
        isAnswerRevealed = true
        feedbackState = .idle
        speechStatusMessage = nil
        CogniAudioController.shared.playSubtleTap(hapticsAndSoundEnabled: isHapticsAndSoundEnabled)
    }

    public func nextCard() {
        let now = Date().timeIntervalSince1970
        guard now - lastAdvanceTimestamp >= Self.advanceDebounceThreshold else { return }
        lastAdvanceTimestamp = now

        guard isSessionStarted, !isSessionComplete else { return }
        stopTimer()
        SpeechRecognizerManager.shared.stopListening()

        // Record outcome
        let outcome: CardOutcome
        if feedbackState == .matched {
            outcome = .matched
        } else if isAnswerRevealed {
            outcome = .revealed
        } else {
            outcome = .skipped
        }

        if let card = currentCard {
            let result = SessionCardResult(
                card: card,
                outcome: outcome,
                userAnswer: userAnswerInput.trimmingCharacters(in: .whitespacesAndNewlines),
                correctAnswer: card.correctAnswer
            )
            sessionResults.append(result)
        }

        if currentIndex < cards.count - 1 {
            currentIndex += 1
            userAnswerInput = ""
            isListening = false
            speechStatusMessage = nil
            feedbackState = .idle
            isAnswerRevealed = false
            secondsRemaining = Self.defaultSeconds
            isTimerPaused = false

            if timerEnabled {
                startTimer()
            }
        } else {
            // Completed deck
            isSessionComplete = true
            userAnswerInput = ""
            isListening = false
            speechStatusMessage = nil
            feedbackState = .idle
            isAnswerRevealed = false
        }

        CogniAudioController.shared.playSubtleTap(hapticsAndSoundEnabled: isHapticsAndSoundEnabled)
    }

    // MARK: - Voice / Speech-to-Text Handling
    public func toggleVoiceInput() {
        if isListening {
            SpeechRecognizerManager.shared.stopListening()
            self.isListening = false
        } else {
            SpeechRecognizerManager.shared.requestAuthorization { [weak self] granted in
                guard let self = self else { return }
                guard granted else {
                    self.speechStatusMessage = "Microphone and speech permissions are required to use voice input."
                    return
                }

                self.isListening = true
                self.speechStatusMessage = nil

                SpeechRecognizerManager.shared.startListening(
                    onResult: { [weak self] recognizedText in
                        guard let self = self else { return }
                        self.userAnswerInput = recognizedText
                        self.submitAnswer()
                    },
                    onError: { [weak self] errorMessage in
                        guard let self = self else { return }
                        self.isListening = false
                        self.speechStatusMessage = errorMessage ?? "Could not hear clearly. Tap Speak again or type above."
                    }
                )
            }
        }
    }

    // MARK: - Timer Handling
    public func toggleTimer() {
        timerEnabled.toggle()
        secondsRemaining = Self.defaultSeconds
        isTimerPaused = false

        if timerEnabled && isSessionStarted && !isSessionComplete {
            startTimer()
        } else {
            stopTimer()
        }
        CogniAudioController.shared.playSubtleTap(hapticsAndSoundEnabled: isHapticsAndSoundEnabled)
    }

    public func pauseTimer() {
        guard timerEnabled, !isTimerPaused else { return }
        stopTimer()
        isTimerPaused = true
    }

    public func resumeTimer() {
        guard timerEnabled, isTimerPaused else { return }
        isTimerPaused = false
        startTimer()
    }

    private func startTimer() {
        stopTimer()
        timerCancellable = Timer.publish(every: 1.0, on: .main, in: .common)
            .autoconnect()
            .sink { [weak self] _ in
                guard let self = self else { return }
                if self.secondsRemaining > 1 {
                    self.secondsRemaining -= 1
                } else {
                    self.secondsRemaining = 0
                    self.stopTimer()
                    self.isAnswerRevealed = true
                    self.feedbackState = .idle
                }
            }
    }

    private func stopTimer() {
        timerCancellable?.cancel()
        timerCancellable = nil
    }

    // MARK: - Settings Toggles
    public func toggleDarkMode() {
        isDarkMode.toggle()
        CogniAudioController.shared.playSubtleTap(hapticsAndSoundEnabled: isHapticsAndSoundEnabled)
    }

    public func toggleMusic() {
        isMusicEnabled.toggle()
        CogniAudioController.shared.setMusicEnabled(isMusicEnabled)
        CogniAudioController.shared.playSubtleTap(hapticsAndSoundEnabled: isHapticsAndSoundEnabled)
    }

    public func toggleHapticsAndSound() {
        isHapticsAndSoundEnabled.toggle()
        CogniAudioController.shared.playSubtleTap(hapticsAndSoundEnabled: isHapticsAndSoundEnabled)
    }

    // MARK: - Forgiving Matching Algorithm
    public static func isForgivingAnswerMatch(input: String, target: String) -> Bool {
        let cleanInput = normalizeForComparison(input)
        let cleanTarget = normalizeForComparison(target)

        guard !cleanInput.isEmpty && !cleanTarget.isEmpty else { return false }

        // Direct match
        if cleanInput == cleanTarget { return true }

        // Singular / Plural match
        if matchesSingularPlural(a: cleanInput, b: cleanTarget) { return true }

        // Multi-word phrase tolerance
        let words = cleanInput.components(separatedBy: " ").filter { !$0.isEmpty }
        if words.contains(cleanTarget) { return true }
        if words.contains(where: { $0 == cleanTarget || matchesSingularPlural(a: $0, b: cleanTarget) }) {
            return true
        }

        return false
    }

    private static func matchesSingularPlural(a: String, b: String) -> Bool {
        if a.hasSuffix("s") && String(a.dropLast()) == b { return true }
        if b.hasSuffix("s") && String(b.dropLast()) == a { return true }

        if a.hasSuffix("es") && String(a.dropLast(2)) == b { return true }
        if b.hasSuffix("es") && String(b.dropLast(2)) == a { return true }

        if a.hasSuffix("ies") && (String(a.dropLast(3)) + "y") == b { return true }
        if b.hasSuffix("ies") && (String(b.dropLast(3)) + "y") == a { return true }

        return false
    }

    private static func normalizeForComparison(_ str: String) -> String {
        var result = str.trimmingCharacters(in: .whitespacesAndNewlines).lowercased()
        // Replace non-alphanumeric with spaces
        result = result.replacingOccurrences(of: "[^a-z0-9\\s]", with: " ", options: .regularExpression)
        // Consolidate whitespace
        result = result.replacingOccurrences(of: "\\s+", with: " ", options: .regularExpression)
            .trimmingCharacters(in: .whitespacesAndNewlines)
        // Strip leading articles
        result = result.replacingOccurrences(of: "^(a|an|the)\\s+", with: "", options: .regularExpression)
        return result.trimmingCharacters(in: .whitespacesAndNewlines)
    }
}
