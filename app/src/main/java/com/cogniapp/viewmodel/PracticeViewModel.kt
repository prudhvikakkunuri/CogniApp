package com.cogniapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cogniapp.data.FruitDeckRepository
import com.cogniapp.data.PracticeDeckRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel managing the cognitive practice session for CogniApp.
 *
 * Implements:
 * - Dedicated launch / home screen flow via [isSessionStarted], [startPractice], [returnToHome]
 * - Multi-deck offline practice with seamless deck switching via [selectDeck]
 * - Shuffled card ordering on session start, deck switch, and restart, preserved across rotation
 * - Light / Dark mode toggling via [toggleDarkMode]
 * - Session card-by-card outcome tracking ([CardOutcome.MATCHED], [CardOutcome.REVEALED], [CardOutcome.SKIPPED])
 * - Independent background music toggle via [toggleMusic] / [toggleBackgroundMusic]
 * - Independent haptics & feedback sound toggle via [toggleHapticsAndSound] / [toggleHapticSound]
 * - Unidirectional Data Flow via [uiState] StateFlow
 * - Double-tap / rapid-click debounce protection on card navigation
 * - Typed and speech-to-text (voice) answer input with forgiving, normalized matching
 * - Calm, non-punitive feedback states ([FeedbackState.MATCHED], [FeedbackState.TRY_AGAIN])
 * - Explicit reveal ("Show Answer") ensuring users are never stuck
 * - Optional, non-rushed countdown timer defaulting to OFF (15s duration when enabled)
 */
class PracticeViewModel(
    private val deckRepository: PracticeDeckRepository = PracticeDeckRepository()
) : ViewModel() {

    // Convenience constructor for backward compatibility with FruitDeckRepository
    constructor(@Suppress("UNUSED_PARAMETER") fruitDeckRepository: FruitDeckRepository) : this(PracticeDeckRepository())

    private val initialDeck = deckRepository.getDefaultDeck()
    private val allDecks = deckRepository.getAllDecks()

    private val _uiState = MutableStateFlow(
        PracticeUiState(
            isSessionStarted = false,
            isDarkMode = false,
            currentDeck = initialDeck,
            availableDecks = allDecks,
            cards = initialDeck.cards,
            currentIndex = 0,
            userAnswerInput = "",
            isListening = false,
            speechStatusMessage = null,
            feedbackState = FeedbackState.IDLE,
            timerEnabled = false,
            secondsRemaining = PracticeUiState.DEFAULT_SECONDS,
            isMusicEnabled = false,
            isHapticsAndSoundEnabled = true,
            sessionResults = emptyList()
        )
    )
    val uiState: StateFlow<PracticeUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var lastAdvanceTimestampMs: Long = 0L

    companion object {
        /**
         * Minimum interval (in ms) allowed between consecutive card advance requests
         * to prevent accidental double-taps by older adults or tremors.
         */
        const val ADVANCE_DEBOUNCE_THRESHOLD_MS: Long = 400L

        /**
         * Normalized, forgiving matching between user input and card's correct answer.
         */
        fun isForgivingAnswerMatch(input: String, target: String): Boolean {
            val cleanInput = normalizeForComparison(input)
            val cleanTarget = normalizeForComparison(target)
            if (cleanInput.isEmpty() || cleanTarget.isEmpty()) return false

            // Direct exact match
            if (cleanInput == cleanTarget) return true

            // Singular / plural matches
            if (matchesSingularPlural(cleanInput, cleanTarget)) return true

            // Spoken phrase tolerance (e.g. "an apple", "it is a pear", "dog please")
            val words = cleanInput.split(" ").filter { it.isNotBlank() }
            if (words.contains(cleanTarget)) return true
            if (words.any { word -> word == cleanTarget || matchesSingularPlural(word, cleanTarget) }) {
                return true
            }

            return false
        }

        private fun matchesSingularPlural(a: String, b: String): Boolean {
            // "pears" <-> "pear"
            if (a.endsWith("s") && a.dropLast(1) == b) return true
            if (b.endsWith("s") && b.dropLast(1) == a) return true
            // "tomatoes" <-> "tomato"
            if (a.endsWith("es") && a.dropLast(2) == b) return true
            if (b.endsWith("es") && b.dropLast(2) == a) return true
            // "strawberries" <-> "strawberry"
            if (a.endsWith("ies") && a.dropLast(3) + "y" == b) return true
            if (b.endsWith("ies") && b.dropLast(3) + "y" == a) return true
            return false
        }

        private fun normalizeForComparison(str: String): String {
            return str.trim()
                .lowercase()
                .replace(Regex("[^a-z0-9\\s]"), " ")
                .replace(Regex("\\s+"), " ")
                .trim()
                .replace(Regex("^(a|an|the)\\s+"), "")
                .trim()
        }
    }

    /**
     * Toggles between Light and Dark themes.
     */
    fun toggleDarkMode() {
        _uiState.update { state ->
            state.copy(isDarkMode = !state.isDarkMode)
        }
    }

    /**
     * Explicitly sets dark mode enabled state.
     */
    fun setDarkMode(enabled: Boolean) {
        _uiState.update { state ->
            state.copy(isDarkMode = enabled)
        }
    }

    /**
     * Starts the practice session for the selected deck with freshly shuffled card order.
     */
    fun startPractice() {
        stopTimer()
        val deck = _uiState.value.currentDeck ?: initialDeck
        val shuffledCards = deck.cards.shuffled()

        _uiState.update { state ->
            state.copy(
                isSessionStarted = true,
                cards = shuffledCards,
                currentIndex = 0,
                userAnswerInput = "",
                isListening = false,
                speechStatusMessage = null,
                feedbackState = FeedbackState.IDLE,
                selectedOption = null,
                isAnswerRevealed = false,
                timerEnabled = false,
                secondsRemaining = PracticeUiState.DEFAULT_SECONDS,
                isTimerPaused = false,
                isSessionComplete = false,
                sessionResults = emptyList()
            )
        }
    }

    /**
     * Returns to the launch / home screen.
     */
    fun returnToHome() {
        stopTimer()
        _uiState.update { state ->
            state.copy(
                isSessionStarted = false,
                currentIndex = 0,
                userAnswerInput = "",
                isListening = false,
                speechStatusMessage = null,
                feedbackState = FeedbackState.IDLE,
                selectedOption = null,
                isAnswerRevealed = false,
                timerEnabled = false,
                secondsRemaining = PracticeUiState.DEFAULT_SECONDS,
                isTimerPaused = false,
                isSessionComplete = false,
                sessionResults = emptyList()
            )
        }
    }

    /**
     * Switches the active practice deck and shuffles the card order.
     */
    fun selectDeck(deckId: String) {
        val targetDeck = deckRepository.getDeck(deckId) ?: return

        stopTimer()
        val shuffledCards = targetDeck.cards.shuffled()

        _uiState.update { state ->
            state.copy(
                currentDeck = targetDeck,
                cards = shuffledCards,
                currentIndex = 0,
                userAnswerInput = "",
                isListening = false,
                speechStatusMessage = null,
                feedbackState = FeedbackState.IDLE,
                selectedOption = null,
                isAnswerRevealed = false,
                timerEnabled = false,
                secondsRemaining = PracticeUiState.DEFAULT_SECONDS,
                isTimerPaused = false,
                isSessionComplete = false,
                sessionResults = emptyList()
            )
        }
    }

    /**
     * Toggles background ambient music on or off.
     */
    fun toggleMusic() {
        setMusicEnabled(!_uiState.value.isMusicEnabled)
    }

    /**
     * Compatibility alias for toggleMusic.
     */
    fun toggleBackgroundMusic() {
        toggleMusic()
    }

    /**
     * Explicitly sets background music state.
     */
    fun setMusicEnabled(enabled: Boolean) {
        _uiState.update { state ->
            state.copy(isMusicEnabled = enabled)
        }
    }

    /**
     * Toggles tactile haptic feedback and interaction sound effects on or off.
     */
    fun toggleHapticsAndSound() {
        setHapticsAndSoundEnabled(!_uiState.value.isHapticsAndSoundEnabled)
    }

    /**
     * Compatibility alias for toggleHapticsAndSound.
     */
    fun toggleHapticSound() {
        toggleHapticsAndSound()
    }

    /**
     * Explicitly sets haptics and sound feedback state.
     */
    fun setHapticsAndSoundEnabled(enabled: Boolean) {
        _uiState.update { state ->
            state.copy(isHapticsAndSoundEnabled = enabled)
        }
    }

    /**
     * Updates typed answer input in the text box.
     */
    fun updateAnswerInput(text: String) {
        _uiState.update { state ->
            state.copy(
                userAnswerInput = text,
                feedbackState = if (state.feedbackState == FeedbackState.TRY_AGAIN) FeedbackState.IDLE else state.feedbackState,
                speechStatusMessage = null
            )
        }
    }

    /**
     * Submits the current typed or spoken answer with forgiving matching.
     */
    fun submitAnswer() {
        val current = _uiState.value
        val card = current.currentCard ?: return
        if (!current.isSessionStarted || current.isSessionComplete) return

        val input = current.userAnswerInput
        if (input.isBlank()) return

        val isMatch = isForgivingAnswerMatch(input, card.correctAnswer)

        if (isMatch) {
            stopTimer()
            _uiState.update { state ->
                state.copy(
                    feedbackState = FeedbackState.MATCHED,
                    isAnswerRevealed = true,
                    selectedOption = card.correctAnswer,
                    speechStatusMessage = null
                )
            }
        } else {
            _uiState.update { state ->
                state.copy(
                    feedbackState = FeedbackState.TRY_AGAIN,
                    speechStatusMessage = null
                )
            }
        }
    }

    /**
     * Legacy helper method for direct option selection.
     */
    fun selectOption(option: String) {
        val current = _uiState.value
        if (!current.isSessionStarted || current.isSessionComplete) return

        updateAnswerInput(option)
        submitAnswer()
    }

    /**
     * Explicitly reveals the answer card for the current clue.
     */
    fun revealAnswer() {
        val current = _uiState.value
        if (!current.isSessionStarted || current.isSessionComplete || current.isAnswerRevealed) return

        stopTimer()
        _uiState.update { state ->
            state.copy(
                isAnswerRevealed = true,
                feedbackState = FeedbackState.IDLE,
                speechStatusMessage = null
            )
        }
    }

    /**
     * Updates listening state for Speech-to-Text.
     */
    fun setListening(listening: Boolean) {
        _uiState.update { state ->
            state.copy(isListening = listening)
        }
    }

    /**
     * Handles recognized speech text, populating input and automatically submitting.
     */
    fun onSpeechResult(recognizedText: String) {
        _uiState.update { state ->
            state.copy(
                userAnswerInput = recognizedText,
                isListening = false,
                speechStatusMessage = null
            )
        }
        submitAnswer()
    }

    /**
     * Handles speech recognition errors or timeouts calmly.
     */
    fun onSpeechError(errorMessage: String?) {
        _uiState.update { state ->
            state.copy(
                isListening = false,
                speechStatusMessage = errorMessage ?: "Could not hear clearly. Tap Speak again or type above."
            )
        }
    }

    /**
     * Clears speech status message.
     */
    fun clearSpeechStatus() {
        _uiState.update { state ->
            state.copy(speechStatusMessage = null)
        }
    }

    /**
     * Advances to the next card in the deck or completes the session.
     * Records the card result into sessionResults.
     */
    fun nextCard() {
        val now = System.currentTimeMillis()
        if (now - lastAdvanceTimestampMs < ADVANCE_DEBOUNCE_THRESHOLD_MS) {
            return
        }
        lastAdvanceTimestampMs = now

        val current = _uiState.value
        if (!current.isSessionStarted || current.isSessionComplete) return

        stopTimer()

        // Determine outcome for current card
        val outcome = when {
            current.feedbackState == FeedbackState.MATCHED -> CardOutcome.MATCHED
            current.isAnswerRevealed -> CardOutcome.REVEALED
            else -> CardOutcome.SKIPPED
        }

        val currentCard = current.currentCard
        val updatedResults = if (currentCard != null) {
            val result = SessionCardResult(
                card = currentCard,
                outcome = outcome,
                userAnswer = current.userAnswerInput.trim(),
                correctAnswer = currentCard.correctAnswer
            )
            current.sessionResults + result
        } else {
            current.sessionResults
        }

        if (current.currentIndex < current.cards.size - 1) {
            _uiState.update { state ->
                state.copy(
                    currentIndex = state.currentIndex + 1,
                    userAnswerInput = "",
                    isListening = false,
                    speechStatusMessage = null,
                    feedbackState = FeedbackState.IDLE,
                    selectedOption = null,
                    isAnswerRevealed = false,
                    secondsRemaining = PracticeUiState.DEFAULT_SECONDS,
                    isTimerPaused = false,
                    sessionResults = updatedResults
                )
            }
            if (_uiState.value.timerEnabled) {
                startTimer()
            }
        } else {
            // Reached end of current deck
            _uiState.update { state ->
                state.copy(
                    isSessionComplete = true,
                    userAnswerInput = "",
                    isListening = false,
                    speechStatusMessage = null,
                    feedbackState = FeedbackState.IDLE,
                    isAnswerRevealed = false,
                    selectedOption = null,
                    sessionResults = updatedResults
                )
            }
        }
    }

    /**
     * Restarts practice from Card 1 of the active deck with a fresh shuffled card order.
     */
    fun restartDeck() {
        stopTimer()
        val deck = _uiState.value.currentDeck ?: initialDeck
        val reshuffled = deck.cards.shuffled()

        _uiState.update { state ->
            state.copy(
                currentIndex = 0,
                cards = reshuffled,
                userAnswerInput = "",
                isListening = false,
                speechStatusMessage = null,
                feedbackState = FeedbackState.IDLE,
                selectedOption = null,
                isAnswerRevealed = false,
                secondsRemaining = PracticeUiState.DEFAULT_SECONDS,
                isTimerPaused = false,
                isSessionComplete = false,
                sessionResults = emptyList()
            )
        }
        if (_uiState.value.timerEnabled) {
            startTimer()
        }
    }

    /**
     * Toggles the optional timer on or off. Defaults to off.
     */
    fun toggleTimer() {
        val newTimerState = !_uiState.value.timerEnabled
        _uiState.update { state ->
            state.copy(
                timerEnabled = newTimerState,
                secondsRemaining = PracticeUiState.DEFAULT_SECONDS,
                isTimerPaused = false
            )
        }
        if (newTimerState && _uiState.value.isSessionStarted && !_uiState.value.isSessionComplete) {
            startTimer()
        } else {
            stopTimer()
        }
    }

    /**
     * Explicitly sets timer enabled state.
     */
    fun setTimerEnabled(enabled: Boolean) {
        if (_uiState.value.timerEnabled != enabled) {
            toggleTimer()
        }
    }

    /**
     * Pauses the timer countdown if running.
     */
    fun pauseTimer() {
        if (!_uiState.value.timerEnabled || _uiState.value.isTimerPaused) return
        stopTimer()
        _uiState.update { state ->
            state.copy(isTimerPaused = true)
        }
    }

    /**
     * Resumes the timer countdown from the remaining seconds.
     */
    fun resumeTimer() {
        if (!_uiState.value.timerEnabled || !_uiState.value.isTimerPaused) return
        _uiState.update { state ->
            state.copy(isTimerPaused = false)
        }
        startTimer()
    }

    private fun startTimer() {
        stopTimer()
        timerJob = viewModelScope.launch {
            while (_uiState.value.secondsRemaining > 0) {
                delay(1000L)
                val currentSeconds = _uiState.value.secondsRemaining
                if (currentSeconds > 1) {
                    _uiState.update { state ->
                        state.copy(secondsRemaining = state.secondsRemaining - 1)
                    }
                } else {
                    // Timer expired: reach 0 and reveal answer calmly
                    _uiState.update { state ->
                        state.copy(secondsRemaining = 0)
                    }
                    onTimerExpired()
                    break
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun onTimerExpired() {
        stopTimer()
        _uiState.update { state ->
            state.copy(
                isAnswerRevealed = true,
                feedbackState = FeedbackState.IDLE
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }
}
