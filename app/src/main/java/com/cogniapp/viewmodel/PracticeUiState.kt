package com.cogniapp.viewmodel

import com.cogniapp.model.PracticeCard
import com.cogniapp.model.PracticeDeck

/**
 * Calm, non-punitive feedback states for answer attempts in CogniApp.
 */
enum class FeedbackState {
    /**
     * Default state before answer submission.
     */
    IDLE,

    /**
     * The submitted typed or spoken answer matched the card's target answer.
     */
    MATCHED,

    /**
     * The submitted answer did not match. Displayed softly without harsh red error states
     * allowing the user to try again or tap "Show Answer".
     */
    TRY_AGAIN
}

/**
 * Outcome of a single practice card upon advancing to the next card.
 */
enum class CardOutcome {
    /**
     * Successfully recalled and matched by typing or speech.
     */
    MATCHED,

    /**
     * Answer revealed by tapping "Show Answer" or timer expiration.
     */
    REVEALED,

    /**
     * Advanced without matching or explicitly revealing.
     */
    SKIPPED
}

/**
 * Record of a single card's practice attempt during a session.
 */
data class SessionCardResult(
    val card: PracticeCard,
    val outcome: CardOutcome,
    val userAnswer: String = "",
    val correctAnswer: String = card.correctAnswer
)

/**
 * Immutable UI state model for CogniApp practice sessions.
 *
 * @property isSessionStarted Whether the user has started practice or is on the launch/home screen.
 * @property isDarkMode Whether dark mode is enabled (defaults to false).
 * @property currentDeck The currently selected practice deck (defaults to Guess the Fruit).
 * @property availableDecks The list of offline decks available for practice.
 * @property currentIndex 0-based index of the currently active card (0 until cards.size).
 * @property cards Local offline cards for the currently active deck (stored in shuffled order during session).
 * @property userAnswerInput Current text entered by typing or speech-to-text.
 * @property isListening Whether speech recognition is actively listening for voice input.
 * @property speechStatusMessage Non-punitive informational or error message from speech recognition.
 * @property feedbackState Current feedback status (IDLE, MATCHED, TRY_AGAIN).
 * @property selectedOption Legacy/backward-compatible option selection string.
 * @property isAnswerRevealed Whether the answer reveal card is currently showing.
 * @property timerEnabled Whether the optional countdown timer is enabled (defaults to false).
 * @property secondsRemaining Seconds left on countdown for the current card (defaults to 15s).
 * @property isTimerPaused Whether countdown timer is temporarily paused.
 * @property isMusicEnabled Whether soothing background ambient audio is enabled (defaults to false).
 * @property isHapticsAndSoundEnabled Whether gentle haptic and sound cues are enabled (defaults to true).
 * @property isSessionComplete Whether the user has finished all cards in the current deck.
 * @property sessionResults List of recorded card outcomes for the current deck session.
 */
data class PracticeUiState(
    val isSessionStarted: Boolean = false,
    val isDarkMode: Boolean = false,
    val currentDeck: PracticeDeck? = null,
    val availableDecks: List<PracticeDeck> = emptyList(),
    val currentIndex: Int = 0,
    val cards: List<PracticeCard> = emptyList(),
    val userAnswerInput: String = "",
    val isListening: Boolean = false,
    val speechStatusMessage: String? = null,
    val feedbackState: FeedbackState = FeedbackState.IDLE,
    val selectedOption: String? = null,
    val isAnswerRevealed: Boolean = false,
    val timerEnabled: Boolean = false,
    val secondsRemaining: Int = DEFAULT_SECONDS,
    val isTimerPaused: Boolean = false,
    val isMusicEnabled: Boolean = false,
    val isHapticsAndSoundEnabled: Boolean = true,
    val isSessionComplete: Boolean = false,
    val sessionResults: List<SessionCardResult> = emptyList()
) {
    companion object {
        /**
         * Default countdown seconds when optional timer is enabled (15s duration).
         */
        const val DEFAULT_SECONDS = 15
    }

    /**
     * Compatibility alias for background music state.
     */
    val backgroundMusicEnabled: Boolean
        get() = isMusicEnabled

    /**
     * Compatibility alias for tactile and feedback sounds state.
     */
    val hapticSoundEnabled: Boolean
        get() = isHapticsAndSoundEnabled

    /**
     * Unique string ID of the current deck, or "fruits" fallback.
     */
    val currentDeckId: String
        get() = currentDeck?.id ?: "fruits"

    /**
     * Human-readable title of current deck, or category of current card.
     */
    val deckTitle: String
        get() = currentDeck?.title ?: (currentCard?.category ?: "CogniApp")

    /**
     * The card currently in focus, or null if deck is empty or out of bounds.
     */
    val currentCard: PracticeCard?
        get() = cards.getOrNull(currentIndex)

    /**
     * Total number of cards in the active deck.
     */
    val totalCards: Int
        get() = cards.size

    /**
     * Human-readable progress indicator string (e.g. "Card 1 / 10").
     */
    val progressFormatted: String
        get() {
            val displayIndex = if (totalCards > 0 && !isSessionComplete) currentIndex + 1 else totalCards
            return "Card $displayIndex / $totalCards"
        }

    /**
     * Helper check to determine if the user selected or typed the correct answer.
     */
    val isCorrectSelection: Boolean
        get() = feedbackState == FeedbackState.MATCHED ||
                (selectedOption != null && currentCard != null &&
                        selectedOption.equals(currentCard?.correctAnswer, ignoreCase = true))

    /**
     * Total number of cards successfully matched directly during the session.
     */
    val matchedCount: Int
        get() = sessionResults.count { it.outcome == CardOutcome.MATCHED }

    /**
     * Total number of cards where the answer was viewed.
     */
    val revealedCount: Int
        get() = sessionResults.count { it.outcome == CardOutcome.REVEALED }

    /**
     * Total number of cards skipped without matching or viewing.
     */
    val skippedCount: Int
        get() = sessionResults.count { it.outcome == CardOutcome.SKIPPED }
}
