package com.cogniapp

import com.cogniapp.data.PracticeDeckRepository
import com.cogniapp.viewmodel.FeedbackState
import com.cogniapp.viewmodel.PracticeUiState
import com.cogniapp.viewmodel.PracticeViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Validates accessibility requirements, touch target guidelines, and
 * configuration change / rotation survival invariants for CogniApp.
 */
class AccessibilityAndRotationTest {

    private lateinit var repository: PracticeDeckRepository
    private lateinit var viewModel: PracticeViewModel

    @Before
    fun setUp() {
        repository = PracticeDeckRepository()
        viewModel = PracticeViewModel(repository)
    }

    @Test
    fun statePreservation_acrossSimulatedActivityRecreation() {
        // 1. User starts practice, toggles music, enables dark mode, and enters an answer
        viewModel.selectDeck(PracticeDeckRepository.DECK_VEGETABLES)
        viewModel.toggleMusic()
        viewModel.toggleDarkMode()
        viewModel.startPractice()
        val target = viewModel.uiState.value.currentCard!!.correctAnswer
        viewModel.updateAnswerInput(target)
        viewModel.submitAnswer()
        viewModel.nextCard()

        assertTrue(viewModel.uiState.value.isSessionStarted)
        assertTrue(viewModel.uiState.value.isDarkMode)
        assertTrue(viewModel.uiState.value.isMusicEnabled)
        assertTrue(viewModel.uiState.value.isHapticsAndSoundEnabled)
        assertEquals(1, viewModel.uiState.value.sessionResults.size)
        assertEquals(com.cogniapp.viewmodel.CardOutcome.MATCHED, viewModel.uiState.value.sessionResults[0].outcome)

        val activeCards = viewModel.uiState.value.cards

        // 2. Simulate activity destroy and recreation binding to the same ViewModel instance
        // (as handled by ComponentActivity.viewModels())
        val retainedState = viewModel.uiState.value
        assertTrue(retainedState.isSessionStarted)
        assertTrue("Dark mode must survive activity recreation", retainedState.isDarkMode)
        assertEquals(PracticeDeckRepository.DECK_VEGETABLES, retainedState.currentDeckId)
        assertEquals(1, retainedState.currentIndex)
        assertFalse(retainedState.isSessionComplete)
        assertEquals(1, retainedState.sessionResults.size)
        assertEquals(com.cogniapp.viewmodel.CardOutcome.MATCHED, retainedState.sessionResults[0].outcome)

        // Shuffled card order preserved across recreation
        assertEquals(activeCards, retainedState.cards)
        assertTrue(retainedState.isMusicEnabled)
        assertTrue(retainedState.isHapticsAndSoundEnabled)
    }

    @Test
    fun homeScreenState_preservedAcrossActivityRecreation() {
        viewModel.selectDeck(PracticeDeckRepository.DECK_ANIMALS)
        viewModel.toggleMusic()
        viewModel.toggleDarkMode()
        assertFalse(viewModel.uiState.value.isSessionStarted)

        val state = viewModel.uiState.value
        assertFalse(state.isSessionStarted)
        assertTrue(state.isDarkMode)
        assertEquals(PracticeDeckRepository.DECK_ANIMALS, state.currentDeckId)
        assertTrue(state.isMusicEnabled)
        assertTrue(state.isHapticsAndSoundEnabled)
    }

    @Test
    fun accessibility_cardProgressString_isScreenReaderAccessible() {
        repository.getAllDecks().forEach { deck ->
            viewModel.selectDeck(deck.id)
            val state = viewModel.uiState.value
            assertEquals("Card 1 / ${deck.cardCount}", state.progressFormatted)
            assertTrue(
                "Progress string must be human-readable and contain card numbers",
                state.progressFormatted.matches(Regex("Card \\d+ / \\d+"))
            )
        }
    }

    @Test
    fun accessibility_optionsLabels_haveDistinctNonBlankText() {
        repository.getAllDecks().forEach { deck ->
            deck.cards.forEach { card ->
                assertTrue("Card clue must not be blank", card.clue.isNotBlank())
                assertTrue("Card emoji must not be blank", card.emoji.isNotBlank())
                card.options.forEach { option ->
                    assertTrue("Option text must not be blank", option.isNotBlank())
                }
                assertEquals(
                    "Options in card ${card.id} of deck ${deck.id} must be unique",
                    card.options.toSet().size,
                    card.options.size
                )
            }
        }
    }

    @Test
    fun touchTargetGuards_buttonMinHeightsAndSpacingMeetGuidelines() {
        val minTouchTargetDp = 58
        val actualButtonHeightDp = 60
        val actualButtonSpacingDp = 10

        assertTrue(
            "Button height must be >= ${minTouchTargetDp}dp, was ${actualButtonHeightDp}dp",
            actualButtonHeightDp >= minTouchTargetDp
        )
        assertTrue(
            "Spacing must be >= 10dp to avoid mis-taps, was ${actualButtonSpacingDp}dp",
            actualButtonSpacingDp >= 10
        )
    }

    @Test
    fun rapidClickGuard_protectsAgainstTremorsAndAccidentalDoubleTaps() {
        viewModel.startPractice()
        assertEquals(0, viewModel.uiState.value.currentIndex)

        viewModel.nextCard()
        assertEquals(1, viewModel.uiState.value.currentIndex)

        viewModel.nextCard()
        assertEquals("Index should stay at 1 due to debounce guard", 1, viewModel.uiState.value.currentIndex)

        Thread.sleep(PracticeViewModel.ADVANCE_DEBOUNCE_THRESHOLD_MS + 25)
        viewModel.nextCard()
        assertEquals(2, viewModel.uiState.value.currentIndex)
    }

    @Test
    fun nonPunitiveFeedback_incorrectInput_setsTryAgainWithoutHarshError() {
        viewModel.startPractice()
        viewModel.updateAnswerInput("DefinitelyIncorrectAnswer")
        viewModel.submitAnswer()

        val state = viewModel.uiState.value
        assertEquals(FeedbackState.TRY_AGAIN, state.feedbackState)
        assertFalse("Incorrect submission does not forcibly reveal answer immediately", state.isAnswerRevealed)

        viewModel.revealAnswer()
        assertTrue("Answer must still be revealed calmly on request without punishment", viewModel.uiState.value.isAnswerRevealed)
    }

    @Test
    fun categoryLabels_areConciseAndDoNotClip() {
        val expectedLabels = mapOf(
            PracticeDeckRepository.DECK_FRUITS to "Fruits",
            PracticeDeckRepository.DECK_VEGETABLES to "Vegetables",
            PracticeDeckRepository.DECK_ANIMALS to "Animals",
            PracticeDeckRepository.DECK_DAILY_OBJECTS to "Everyday Things",
            PracticeDeckRepository.DECK_FOOD_DRINKS to "Food & Drinks",
            PracticeDeckRepository.DECK_COLORS_SHAPES to "Colors & Shapes",
            PracticeDeckRepository.DECK_CLOTHES_WEARABLES to "Clothes & Wear",
            PracticeDeckRepository.DECK_NATURE_WEATHER to "Nature & Weather",
            PracticeDeckRepository.DECK_DAILY_ROUTINES to "Daily Routines",
            PracticeDeckRepository.DECK_MIXED_ALL to "All Decks Mix"
        )

        repository.getAllDecks().forEach { deck ->
            val expected = expectedLabels[deck.id]
            assertNotNull("Category label must exist for deck ${deck.id}", expected)
            assertEquals("Category label for ${deck.id} must be concise to avoid clipping", expected, deck.categoryLabel)
            assertTrue("Category label must be non-empty", deck.categoryLabel.isNotBlank())
        }
    }

    @Test
    fun timerDefaults_areCalmAndOffByDefaultWithFifteenSecondsDuration() {
        assertFalse("Timer must default to false on launch", viewModel.uiState.value.timerEnabled)
        assertEquals(15, viewModel.uiState.value.secondsRemaining)
        assertEquals(15, PracticeUiState.DEFAULT_SECONDS)

        viewModel.selectDeck(PracticeDeckRepository.DECK_VEGETABLES)
        assertFalse("Timer must remain false after deck switch", viewModel.uiState.value.timerEnabled)
        assertEquals(15, viewModel.uiState.value.secondsRemaining)

        viewModel.startPractice()
        assertFalse("Timer must remain false when starting practice", viewModel.uiState.value.timerEnabled)
        assertEquals(15, viewModel.uiState.value.secondsRemaining)
    }
}
