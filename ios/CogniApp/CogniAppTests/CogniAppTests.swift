import XCTest
@testable import CogniApp

final class CogniAppTests: XCTestCase {

    // MARK: - 1. Repository & Deck Library Integrity Tests
    func testRepositoryContainsAll10DecksAnd86Cards() {
        let repo = PracticeDeckRepository()
        let decks = repo.getAllDecks()
        XCTAssertEqual(decks.count, 10, "Repository must contain 10 decks (9 thematic + 1 mixed)")

        let thematicDecks = repo.getThematicDecks()
        XCTAssertEqual(thematicDecks.count, 9, "Must contain exactly 9 thematic decks")

        let expectedCardCounts: [String: Int] = [
            PracticeDeckRepository.deckFruits: 12,
            PracticeDeckRepository.deckVegetables: 10,
            PracticeDeckRepository.deckAnimals: 10,
            PracticeDeckRepository.deckDailyObjects: 10,
            PracticeDeckRepository.deckFoodDrinks: 10,
            PracticeDeckRepository.deckColorsShapes: 8,
            PracticeDeckRepository.deckClothesWearables: 8,
            PracticeDeckRepository.deckNatureWeather: 8,
            PracticeDeckRepository.deckDailyRoutines: 8,
            PracticeDeckRepository.deckMixedAll: 86
        ]

        for (deckId, expectedCount) in expectedCardCounts {
            guard let deck = repo.getDeck(id: deckId) else {
                XCTFail("Missing deck with id: \(deckId)")
                continue
            }
            XCTAssertEqual(deck.cardCount, expectedCount, "Deck \(deckId) must contain \(expectedCount) cards")
            XCTAssertFalse(deck.title.isEmpty, "Deck \(deckId) must have a non-empty title")
            XCTAssertFalse(deck.categoryLabel.isEmpty, "Deck \(deckId) must have a non-empty category label")
            XCTAssertFalse(deck.emoji.isEmpty, "Deck \(deckId) must have a valid emoji")
        }
    }

    func testCardsIntegrityAcrossAllDecks() {
        let repo = PracticeDeckRepository()
        let allDecks = repo.getAllDecks()

        for deck in allDecks {
            for card in deck.cards {
                XCTAssertGreaterThan(card.id, 0, "Card ID must be positive in \(deck.id)")
                XCTAssertFalse(card.clue.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty, "Card clue cannot be empty")
                XCTAssertFalse(card.correctAnswer.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty, "Card answer cannot be empty")
                XCTAssertFalse(card.emoji.isEmpty, "Card emoji cannot be empty")
                XCTAssertGreaterThanOrEqual(card.options.count, 2, "Card options should have at least 2 choices")
                XCTAssertTrue(card.options.contains(card.correctAnswer), "Card options must contain correctAnswer: \(card.correctAnswer)")
            }
        }
    }

    func testMixedDeckIntegrityAndNoDuplicateSelfAggregation() {
        let repo = PracticeDeckRepository()
        guard let mixedDeck = repo.getDeck(id: PracticeDeckRepository.deckMixedAll) else {
            XCTFail("Mixed All Decks not found")
            return
        }

        XCTAssertEqual(mixedDeck.cardCount, 86, "Mixed deck must aggregate all 86 thematic cards")

        // Verify categories are diverse
        let uniqueCategories = Set(mixedDeck.cards.map { $0.category })
        XCTAssertGreaterThanOrEqual(uniqueCategories.count, 9, "Mixed deck must aggregate cards across all 9 thematic categories")

        // Verify all IDs in mixed deck are sequential from 1 to 86
        let cardIds = mixedDeck.cards.map { $0.id }
        XCTAssertEqual(cardIds, Array(1...86), "Mixed deck card IDs should be indexed sequentially from 1 to 86")
    }

    // MARK: - 2. Shuffling Tests
    func testCardShufflingOnStartSelectAndRestart() {
        let viewModel = PracticeViewModel()
        let defaultDeckCards = viewModel.currentDeck.cards

        // Start session
        viewModel.startPractice()
        XCTAssertEqual(viewModel.cards.count, defaultDeckCards.count)

        // Select a different deck (Vegetables)
        viewModel.selectDeck(id: PracticeDeckRepository.deckVegetables)
        XCTAssertEqual(viewModel.currentDeck.id, PracticeDeckRepository.deckVegetables)
        XCTAssertEqual(viewModel.cards.count, 10)
        XCTAssertEqual(viewModel.currentIndex, 0)

        // Restart deck
        viewModel.restartDeck()
        XCTAssertEqual(viewModel.currentDeck.id, PracticeDeckRepository.deckVegetables)
        XCTAssertEqual(viewModel.cards.count, 10)
        XCTAssertEqual(viewModel.currentIndex, 0)
    }

    // MARK: - 3. Forgiving Answer Matching Tests
    func testForgivingAnswerMatchingExtensive() {
        // Direct exact match
        XCTAssertTrue(PracticeViewModel.isForgivingAnswerMatch(input: "Apple", target: "Apple"))
        XCTAssertTrue(PracticeViewModel.isForgivingAnswerMatch(input: "apple", target: "Apple"))
        XCTAssertTrue(PracticeViewModel.isForgivingAnswerMatch(input: "APPLE", target: "Apple"))

        // Whitespace tolerance
        XCTAssertTrue(PracticeViewModel.isForgivingAnswerMatch(input: "   apple   ", target: "Apple"))
        XCTAssertTrue(PracticeViewModel.isForgivingAnswerMatch(input: "  Watermelon  ", target: "Watermelon"))

        // Punctuation tolerance
        XCTAssertTrue(PracticeViewModel.isForgivingAnswerMatch(input: "Apple!", target: "Apple"))
        XCTAssertTrue(PracticeViewModel.isForgivingAnswerMatch(input: "\"Pear\"", target: "Pear"))

        // Leading articles
        XCTAssertTrue(PracticeViewModel.isForgivingAnswerMatch(input: "an apple", target: "Apple"))
        XCTAssertTrue(PracticeViewModel.isForgivingAnswerMatch(input: "the pear", target: "Pear"))
        XCTAssertTrue(PracticeViewModel.isForgivingAnswerMatch(input: "a banana", target: "Banana"))

        // Singular & Plural matching
        XCTAssertTrue(PracticeViewModel.isForgivingAnswerMatch(input: "pears", target: "Pear"))
        XCTAssertTrue(PracticeViewModel.isForgivingAnswerMatch(input: "pear", target: "Pears"))
        XCTAssertTrue(PracticeViewModel.isForgivingAnswerMatch(input: "tomatoes", target: "Tomato"))
        XCTAssertTrue(PracticeViewModel.isForgivingAnswerMatch(input: "tomato", target: "Tomatoes"))
        XCTAssertTrue(PracticeViewModel.isForgivingAnswerMatch(input: "strawberries", target: "Strawberry"))
        XCTAssertTrue(PracticeViewModel.isForgivingAnswerMatch(input: "strawberry", target: "Strawberries"))

        // Conversational voice phrases
        XCTAssertTrue(PracticeViewModel.isForgivingAnswerMatch(input: "it is a pear", target: "Pear"))
        XCTAssertTrue(PracticeViewModel.isForgivingAnswerMatch(input: "i think it is an apple", target: "Apple"))
        XCTAssertTrue(PracticeViewModel.isForgivingAnswerMatch(input: "that looks like a cat", target: "Cat"))
        XCTAssertTrue(PracticeViewModel.isForgivingAnswerMatch(input: "dog please", target: "Dog"))

        // Negative cases
        XCTAssertFalse(PracticeViewModel.isForgivingAnswerMatch(input: "Orange", target: "Apple"))
        XCTAssertFalse(PracticeViewModel.isForgivingAnswerMatch(input: "Potato", target: "Tomato"))
        XCTAssertFalse(PracticeViewModel.isForgivingAnswerMatch(input: "", target: "Apple"))
        XCTAssertFalse(PracticeViewModel.isForgivingAnswerMatch(input: "   ", target: "Apple"))
        XCTAssertFalse(PracticeViewModel.isForgivingAnswerMatch(input: "something completely unrelated", target: "Banana"))
    }

    // MARK: - 4. Double-Tap Debounce Protection Tests
    func testDoubleTapDebounceProtection() {
        let viewModel = PracticeViewModel()
        viewModel.startPractice()

        XCTAssertEqual(viewModel.currentIndex, 0)

        // Advance card
        viewModel.nextCard()
        XCTAssertEqual(viewModel.currentIndex, 1)

        // Rapid advance should be blocked by 400ms debounce guard
        viewModel.nextCard()
        XCTAssertEqual(viewModel.currentIndex, 1, "Immediate consecutive nextCard() call must be ignored by debounce")

        // Wait past 400ms threshold
        let expectation = expectation(description: "Debounce threshold pass")
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.45) {
            viewModel.nextCard()
            XCTAssertEqual(viewModel.currentIndex, 2, "nextCard() should succeed after 400ms debounce threshold")
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 1.0)
    }

    // MARK: - 5. Optional Timer Tests
    func testOptionalTimerDefaultsToOff() {
        let viewModel = PracticeViewModel()
        XCTAssertFalse(viewModel.timerEnabled, "Timer must default to OFF")
        XCTAssertEqual(viewModel.secondsRemaining, 15, "Default timer duration must be 15s")

        viewModel.startPractice()
        XCTAssertFalse(viewModel.timerEnabled, "Timer must remain OFF when practice starts")

        viewModel.toggleTimer()
        XCTAssertTrue(viewModel.timerEnabled, "Timer must be ON after toggle")

        viewModel.toggleTimer()
        XCTAssertFalse(viewModel.timerEnabled, "Timer must be OFF after second toggle")
    }

    // MARK: - 6. Audio & Haptics Toggles
    func testAudioAndHapticsToggles() {
        let viewModel = PracticeViewModel()

        // Music defaults to OFF
        XCTAssertFalse(viewModel.isMusicEnabled, "Background music must default to OFF")
        viewModel.toggleMusic()
        XCTAssertTrue(viewModel.isMusicEnabled)
        viewModel.toggleMusic()
        XCTAssertFalse(viewModel.isMusicEnabled)

        // Haptics & Sounds default to ON
        XCTAssertTrue(viewModel.isHapticsAndSoundEnabled, "Haptics & sounds must default to ON")
        viewModel.toggleHapticsAndSound()
        XCTAssertFalse(viewModel.isHapticsAndSoundEnabled)
        viewModel.toggleHapticsAndSound()
        XCTAssertTrue(viewModel.isHapticsAndSoundEnabled)
    }

    // MARK: - 7. Session Outcomes: Matched, Revealed, Skipped
    func testSessionOutcomeTrackingMatchedRevealedSkipped() {
        let viewModel = PracticeViewModel()
        viewModel.startPractice()

        // Card 1: Correct Answer -> MATCHED
        if let card1 = viewModel.currentCard {
            viewModel.updateAnswerInput(card1.correctAnswer)
            viewModel.submitAnswer()
            XCTAssertEqual(viewModel.feedbackState, .matched)
        }
        viewModel.nextCard()
        XCTAssertEqual(viewModel.sessionResults.count, 1)
        XCTAssertEqual(viewModel.sessionResults.first?.outcome, .matched)

        // Wait debounce
        Thread.sleep(forTimeInterval: 0.42)

        // Card 2: Reveal Answer -> REVEALED
        viewModel.updateAnswerInput("wrong input")
        viewModel.revealAnswer()
        XCTAssertTrue(viewModel.isAnswerRevealed)
        viewModel.nextCard()
        XCTAssertEqual(viewModel.sessionResults.count, 2)
        XCTAssertEqual(viewModel.sessionResults[1].outcome, .revealed)
        XCTAssertEqual(viewModel.sessionResults[1].userAnswer, "wrong input")

        // Wait debounce
        Thread.sleep(forTimeInterval: 0.42)

        // Card 3: Direct next card without answering -> SKIPPED
        viewModel.nextCard()
        XCTAssertEqual(viewModel.sessionResults.count, 3)
        XCTAssertEqual(viewModel.sessionResults[2].outcome, .skipped)

        // Verify summary counts
        XCTAssertEqual(viewModel.matchedCount, 1)
        XCTAssertEqual(viewModel.revealedCount, 1)
        XCTAssertEqual(viewModel.skippedCount, 1)
    }

    // MARK: - 8. Return to Home Resets Session State
    func testReturnToHomeResetsSessionState() {
        let viewModel = PracticeViewModel()
        viewModel.startPractice()

        XCTAssertTrue(viewModel.isSessionStarted)
        viewModel.nextCard()

        viewModel.returnToHome()
        XCTAssertFalse(viewModel.isSessionStarted)
        XCTAssertEqual(viewModel.currentIndex, 0)
        XCTAssertEqual(viewModel.sessionResults.count, 0)
    }

    // MARK: - 9. Offline Read Aloud TTS Voice Tests
    func testTtsVoiceQueryAndDefault() {
        let voices = CogniTtsController.shared.queryAvailableVoices()
        XCTAssertFalse(voices.isEmpty, "Querying voices should always return at least system default English voice")
        for voice in voices {
            XCTAssertFalse(voice.id.isEmpty)
            XCTAssertFalse(voice.name.isEmpty)
            XCTAssertFalse(voice.language.isEmpty)
        }
    }

    func testVoiceSelectionAndPersistence() {
        let viewModel = PracticeViewModel()
        XCTAssertFalse(viewModel.availableVoices.isEmpty)
        XCTAssertFalse(viewModel.selectedVoiceDisplayName.isEmpty)

        // Select a custom voice
        let customId = "com.apple.speech.voice.Alex"
        viewModel.selectVoice(identifier: customId)
        XCTAssertEqual(viewModel.selectedVoiceIdentifier, customId)
        XCTAssertEqual(UserDefaults.standard.string(forKey: "cogniapp_selected_voice_id"), customId)
    }
}
