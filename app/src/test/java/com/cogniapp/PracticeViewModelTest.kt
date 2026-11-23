package com.cogniapp

import com.cogniapp.data.PracticeDeckRepository
import com.cogniapp.viewmodel.FeedbackState
import com.cogniapp.viewmodel.PracticeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PracticeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: PracticeViewModel
    private lateinit var deckRepository: PracticeDeckRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        deckRepository = PracticeDeckRepository()
        viewModel = PracticeViewModel(deckRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_startsOnHomeScreen_withTenDecksAndTimerDisabled() {
        val state = viewModel.uiState.value
        assertFalse("Must start on Launch / Home screen", state.isSessionStarted)
        assertEquals("fruits", state.currentDeckId)
        assertEquals("Guess the Fruit", state.deckTitle)
        assertEquals(10, state.availableDecks.size)
        assertTrue(state.totalCards >= 10)
        assertEquals(0, state.currentIndex)
        assertEquals("Card 1 / ${state.totalCards}", state.progressFormatted)
        assertFalse("Timer must default to false/off", state.timerEnabled)
        assertEquals(15, state.secondsRemaining)
        assertFalse(state.isAnswerRevealed)
        assertEquals("", state.userAnswerInput)
        assertEquals(FeedbackState.IDLE, state.feedbackState)
        assertFalse(state.isSessionComplete)
        assertNotNull(state.currentCard)

        // Music and Sound initial states
        assertFalse("Background music must default to false/off", state.isMusicEnabled)
        assertEquals(state.isMusicEnabled, state.backgroundMusicEnabled)
        assertTrue("Haptics and sound should default to true/on", state.isHapticsAndSoundEnabled)
        assertEquals(state.isHapticsAndSoundEnabled, state.hapticSoundEnabled)
    }

    @Test
    fun startPractice_shufflesCardsAndTransitionsToActivePractice() {
        assertFalse(viewModel.uiState.value.isSessionStarted)

        viewModel.startPractice()
        val state = viewModel.uiState.value
        assertTrue("Session must be started", state.isSessionStarted)
        assertEquals(0, state.currentIndex)
        assertEquals("", state.userAnswerInput)
        assertEquals(FeedbackState.IDLE, state.feedbackState)
        assertFalse(state.isAnswerRevealed)
        assertEquals(15, state.secondsRemaining)

        // Verify cards collection is populated and belongs to the deck
        assertTrue(state.cards.isNotEmpty())
        state.cards.forEach { card ->
            assertEquals("fruits", card.deckId)
        }
    }

    @Test
    fun returnToHome_transitionsBackToHomeScreen() {
        viewModel.startPractice()
        assertTrue(viewModel.uiState.value.isSessionStarted)

        val firstAnswer = viewModel.uiState.value.currentCard!!.correctAnswer
        viewModel.updateAnswerInput(firstAnswer)
        viewModel.submitAnswer()
        viewModel.nextCard()
        assertEquals(1, viewModel.uiState.value.currentIndex)

        viewModel.returnToHome()
        val state = viewModel.uiState.value
        assertFalse("Session must be closed and returned to Home", state.isSessionStarted)
        assertEquals(0, state.currentIndex)
        assertEquals("", state.userAnswerInput)
        assertEquals(FeedbackState.IDLE, state.feedbackState)
        assertFalse(state.isAnswerRevealed)
    }

    @Test
    fun musicToggle_enablesAndDisablesBackgroundMusic() {
        assertFalse(viewModel.uiState.value.isMusicEnabled)

        viewModel.toggleMusic()
        assertTrue(viewModel.uiState.value.isMusicEnabled)
        assertTrue(viewModel.uiState.value.backgroundMusicEnabled)

        viewModel.toggleBackgroundMusic()
        assertFalse(viewModel.uiState.value.isMusicEnabled)
        assertFalse(viewModel.uiState.value.backgroundMusicEnabled)

        viewModel.setMusicEnabled(true)
        assertTrue(viewModel.uiState.value.isMusicEnabled)
    }

    @Test
    fun hapticsAndSoundToggle_enablesAndDisablesHapticsAndSound() {
        assertTrue(viewModel.uiState.value.isHapticsAndSoundEnabled)

        viewModel.toggleHapticsAndSound()
        assertFalse(viewModel.uiState.value.isHapticsAndSoundEnabled)
        assertFalse(viewModel.uiState.value.hapticSoundEnabled)

        viewModel.toggleHapticSound()
        assertTrue(viewModel.uiState.value.isHapticsAndSoundEnabled)
        assertTrue(viewModel.uiState.value.hapticSoundEnabled)

        viewModel.setHapticsAndSoundEnabled(false)
        assertFalse(viewModel.uiState.value.isHapticsAndSoundEnabled)
    }

    @Test
    fun updateAnswerInput_updatesStateAndClearsTryAgain() {
        viewModel.startPractice()
        viewModel.updateAnswerInput("DefinitelyWrongAnswer123")
        viewModel.submitAnswer()
        assertEquals(FeedbackState.TRY_AGAIN, viewModel.uiState.value.feedbackState)

        viewModel.updateAnswerInput("P")
        assertEquals("P", viewModel.uiState.value.userAnswerInput)
        assertEquals(FeedbackState.IDLE, viewModel.uiState.value.feedbackState)
    }

    @Test
    fun submitAnswer_exactMatch_setsMatchedAndReveals() {
        viewModel.startPractice()
        val target = viewModel.uiState.value.currentCard!!.correctAnswer
        viewModel.updateAnswerInput(target)
        viewModel.submitAnswer()

        val state = viewModel.uiState.value
        assertEquals(target, state.userAnswerInput)
        assertEquals(FeedbackState.MATCHED, state.feedbackState)
        assertTrue(state.isAnswerRevealed)
        assertTrue(state.isCorrectSelection)
    }

    @Test
    fun submitAnswer_forgivingMatching_toleratesCasePunctuationArticlesAndPlurals() {
        assertTrue(PracticeViewModel.isForgivingAnswerMatch("pear", "Pear"))
        assertTrue(PracticeViewModel.isForgivingAnswerMatch("a pear", "Pear"))
        assertTrue(PracticeViewModel.isForgivingAnswerMatch("the pear", "Pear"))
        assertTrue(PracticeViewModel.isForgivingAnswerMatch("pears", "Pear"))
        assertTrue(PracticeViewModel.isForgivingAnswerMatch("it's an apple", "Apple"))
        assertTrue(PracticeViewModel.isForgivingAnswerMatch("tomatoes", "Tomato"))
        assertTrue(PracticeViewModel.isForgivingAnswerMatch("strawberries", "Strawberry"))
        assertTrue(PracticeViewModel.isForgivingAnswerMatch("noodle", "Noodles"))

        viewModel.startPractice()
        val target = viewModel.uiState.value.currentCard!!.correctAnswer
        viewModel.updateAnswerInput("a $target!")
        viewModel.submitAnswer()

        val state = viewModel.uiState.value
        assertEquals(FeedbackState.MATCHED, state.feedbackState)
        assertTrue(state.isAnswerRevealed)
    }

    @Test
    fun submitAnswer_incorrectChoice_setsTryAgainWithoutPunishment() {
        viewModel.startPractice()
        viewModel.updateAnswerInput("DefinitelyWrongChoice")
        viewModel.submitAnswer()

        val state = viewModel.uiState.value
        assertEquals(FeedbackState.TRY_AGAIN, state.feedbackState)
        assertFalse("Card should not be revealed immediately on Try Again", state.isAnswerRevealed)
        assertFalse(state.isCorrectSelection)
    }

    @Test
    fun speechRecognition_populatesTextAndAutoSubmits() {
        viewModel.startPractice()
        viewModel.setListening(true)
        assertTrue(viewModel.uiState.value.isListening)

        val target = viewModel.uiState.value.currentCard!!.correctAnswer
        viewModel.onSpeechResult(target)
        val state = viewModel.uiState.value
        assertFalse(state.isListening)
        assertEquals(target, state.userAnswerInput)
        assertEquals(FeedbackState.MATCHED, state.feedbackState)
        assertTrue(state.isAnswerRevealed)
    }

    @Test
    fun speechError_setsCalmStatusMessageWithoutCrashing() {
        viewModel.startPractice()
        viewModel.setListening(true)
        viewModel.onSpeechError("Didn't catch that. Please tap Speak again.")

        val state = viewModel.uiState.value
        assertFalse(state.isListening)
        assertEquals("Didn't catch that. Please tap Speak again.", state.speechStatusMessage)
    }

    @Test
    fun revealAnswer_showsAnswerCardWithoutAdvancing() {
        viewModel.startPractice()
        viewModel.revealAnswer()
        val state = viewModel.uiState.value
        assertTrue(state.isAnswerRevealed)
        assertEquals(0, state.currentIndex)
    }

    @Test
    fun nextCard_advancesToNextCardAndResetsInputAndFeedback() {
        viewModel.startPractice()
        val firstTarget = viewModel.uiState.value.currentCard!!.correctAnswer
        viewModel.updateAnswerInput(firstTarget)
        viewModel.submitAnswer()
        assertTrue(viewModel.uiState.value.isAnswerRevealed)

        // Advance card
        viewModel.nextCard()
        val state = viewModel.uiState.value
        assertEquals(1, state.currentIndex)
        assertEquals("Card 2 / ${state.totalCards}", state.progressFormatted)
        assertFalse(state.isAnswerRevealed)
        assertEquals("", state.userAnswerInput)
        assertEquals(FeedbackState.IDLE, state.feedbackState)
        assertEquals(15, state.secondsRemaining)
    }

    @Test
    fun nextCard_rapidClickProtection_preventsDoubleAdvance() {
        viewModel.startPractice()
        viewModel.nextCard()
        assertEquals(1, viewModel.uiState.value.currentIndex)

        // Immediate subsequent click within debounce interval should be ignored
        viewModel.nextCard()
        assertEquals("Index should stay 1 due to rapid-click guard", 1, viewModel.uiState.value.currentIndex)
    }

    @Test
    fun nextCard_reachesEndOfDeck_completesSession() {
        viewModel.startPractice()
        val total = viewModel.uiState.value.totalCards
        for (i in 0 until (total - 1)) {
            viewModel.nextCard()
            Thread.sleep(PracticeViewModel.ADVANCE_DEBOUNCE_THRESHOLD_MS + 20)
        }
        assertEquals(total - 1, viewModel.uiState.value.currentIndex)
        assertFalse(viewModel.uiState.value.isSessionComplete)

        viewModel.nextCard()
        val finalState = viewModel.uiState.value
        assertTrue("Session should be complete on reaching end", finalState.isSessionComplete)
        assertFalse(finalState.isAnswerRevealed)
    }

    @Test
    fun restartDeck_resetsToCardOneWithCleanShuffledState() {
        viewModel.startPractice()
        val target = viewModel.uiState.value.currentCard!!.correctAnswer
        viewModel.updateAnswerInput(target)
        viewModel.submitAnswer()
        viewModel.nextCard()
        viewModel.restartDeck()

        val state = viewModel.uiState.value
        assertEquals(0, state.currentIndex)
        assertEquals("", state.userAnswerInput)
        assertEquals(FeedbackState.IDLE, state.feedbackState)
        assertFalse(state.isAnswerRevealed)
        assertFalse(state.isSessionComplete)
        assertEquals(15, state.secondsRemaining)
        assertEquals("Card 1 / ${state.totalCards}", state.progressFormatted)
    }

    @Test
    fun selectDeck_switchesAcrossAllAvailableDecksIncludingMixedAll() {
        val deckIds = listOf(
            PracticeDeckRepository.DECK_ANIMALS to "Guess the Animal",
            PracticeDeckRepository.DECK_DAILY_OBJECTS to "Everyday Things",
            PracticeDeckRepository.DECK_FOOD_DRINKS to "Food & Drinks",
            PracticeDeckRepository.DECK_COLORS_SHAPES to "Colors & Shapes",
            PracticeDeckRepository.DECK_CLOTHES_WEARABLES to "Clothes & Wearables",
            PracticeDeckRepository.DECK_NATURE_WEATHER to "Nature & Weather",
            PracticeDeckRepository.DECK_DAILY_ROUTINES to "Daily Routines",
            PracticeDeckRepository.DECK_MIXED_ALL to "Mixed All Decks",
            PracticeDeckRepository.DECK_FRUITS to "Guess the Fruit"
        )

        deckIds.forEach { (deckId, title) ->
            viewModel.selectDeck(deckId)
            assertEquals(deckId, viewModel.uiState.value.currentDeckId)
            assertEquals(title, viewModel.uiState.value.deckTitle)
            assertEquals(0, viewModel.uiState.value.currentIndex)
            assertNotNull(viewModel.uiState.value.currentCard)
        }
    }

    @Test
    fun toggleTimer_enablesAndDisablesTimer() {
        assertFalse(viewModel.uiState.value.timerEnabled)

        viewModel.toggleTimer()
        assertTrue(viewModel.uiState.value.timerEnabled)

        viewModel.toggleTimer()
        assertFalse(viewModel.uiState.value.timerEnabled)
    }

    @Test
    fun timerCountdown_whenEnabled_revealsAnswerAtZero() = runTest(testDispatcher) {
        viewModel.startPractice()
        viewModel.setTimerEnabled(true)
        assertTrue(viewModel.uiState.value.timerEnabled)
        assertEquals(15, viewModel.uiState.value.secondsRemaining)

        advanceTimeBy(8_000L)
        testScheduler.runCurrent()
        assertEquals(7, viewModel.uiState.value.secondsRemaining)
        assertFalse(viewModel.uiState.value.isAnswerRevealed)

        advanceTimeBy(7_000L)
        testScheduler.runCurrent()
        assertEquals(0, viewModel.uiState.value.secondsRemaining)
        assertTrue("Answer should be revealed calmly when timer hits zero", viewModel.uiState.value.isAnswerRevealed)
    }

    @Test
    fun timer_pauseAndResume() = runTest(testDispatcher) {
        viewModel.startPractice()
        viewModel.setTimerEnabled(true)
        advanceTimeBy(5_000L)
        testScheduler.runCurrent()
        assertEquals(10, viewModel.uiState.value.secondsRemaining)

        viewModel.pauseTimer()
        assertTrue(viewModel.uiState.value.isTimerPaused)

        advanceTimeBy(5_000L)
        testScheduler.runCurrent()
        assertEquals("Timer should not decrement while paused", 10, viewModel.uiState.value.secondsRemaining)

        viewModel.resumeTimer()
        assertFalse(viewModel.uiState.value.isTimerPaused)

        advanceTimeBy(3_000L)
        testScheduler.runCurrent()
        assertEquals(7, viewModel.uiState.value.secondsRemaining)
    }

    @Test
    fun darkModeToggle_togglesBetweenLightAndDark() {
        assertFalse("Default theme must be light mode", viewModel.uiState.value.isDarkMode)

        viewModel.toggleDarkMode()
        assertTrue("Dark mode must be enabled after toggle", viewModel.uiState.value.isDarkMode)

        viewModel.toggleDarkMode()
        assertFalse("Light mode must be restored after second toggle", viewModel.uiState.value.isDarkMode)

        viewModel.setDarkMode(true)
        assertTrue(viewModel.uiState.value.isDarkMode)

        viewModel.setDarkMode(false)
        assertFalse(viewModel.uiState.value.isDarkMode)
    }

    @Test
    fun sessionResults_tracksOutcomesCorrectly_matchedRevealedAndSkipped() {
        viewModel.startPractice()
        val cards = viewModel.uiState.value.cards
        assertTrue(cards.size >= 3)

        // Card 1: Submit correct answer -> MATCHED
        val card1 = viewModel.uiState.value.currentCard!!
        viewModel.updateAnswerInput(card1.correctAnswer)
        viewModel.submitAnswer()
        assertEquals(FeedbackState.MATCHED, viewModel.uiState.value.feedbackState)
        viewModel.nextCard()

        assertEquals(1, viewModel.uiState.value.sessionResults.size)
        val result1 = viewModel.uiState.value.sessionResults[0]
        assertEquals(card1.id, result1.card.id)
        assertEquals(com.cogniapp.viewmodel.CardOutcome.MATCHED, result1.outcome)
        assertEquals(card1.correctAnswer, result1.userAnswer)
        assertEquals(1, viewModel.uiState.value.matchedCount)
        assertEquals(0, viewModel.uiState.value.revealedCount)
        assertEquals(0, viewModel.uiState.value.skippedCount)

        Thread.sleep(PracticeViewModel.ADVANCE_DEBOUNCE_THRESHOLD_MS + 20)

        // Card 2: Tap reveal answer -> REVEALED
        val card2 = viewModel.uiState.value.currentCard!!
        viewModel.revealAnswer()
        assertTrue(viewModel.uiState.value.isAnswerRevealed)
        viewModel.nextCard()

        assertEquals(2, viewModel.uiState.value.sessionResults.size)
        val result2 = viewModel.uiState.value.sessionResults[1]
        assertEquals(card2.id, result2.card.id)
        assertEquals(com.cogniapp.viewmodel.CardOutcome.REVEALED, result2.outcome)
        assertEquals(1, viewModel.uiState.value.matchedCount)
        assertEquals(1, viewModel.uiState.value.revealedCount)
        assertEquals(0, viewModel.uiState.value.skippedCount)

        Thread.sleep(PracticeViewModel.ADVANCE_DEBOUNCE_THRESHOLD_MS + 20)

        // Card 3: Skip without matching or revealing -> SKIPPED
        val card3 = viewModel.uiState.value.currentCard!!
        viewModel.nextCard()

        assertEquals(3, viewModel.uiState.value.sessionResults.size)
        val result3 = viewModel.uiState.value.sessionResults[2]
        assertEquals(card3.id, result3.card.id)
        assertEquals(com.cogniapp.viewmodel.CardOutcome.SKIPPED, result3.outcome)
        assertEquals(1, viewModel.uiState.value.matchedCount)
        assertEquals(1, viewModel.uiState.value.revealedCount)
        assertEquals(1, viewModel.uiState.value.skippedCount)
    }

    @Test
    fun sessionResults_resetsOnRestartSelectDeckAndStartPractice() {
        viewModel.startPractice()
        val card = viewModel.uiState.value.currentCard!!
        viewModel.updateAnswerInput(card.correctAnswer)
        viewModel.submitAnswer()
        viewModel.nextCard()
        assertEquals(1, viewModel.uiState.value.sessionResults.size)

        // Restarting deck resets results
        viewModel.restartDeck()
        assertEquals(0, viewModel.uiState.value.sessionResults.size)
        assertEquals(0, viewModel.uiState.value.matchedCount)
        assertEquals(0, viewModel.uiState.value.revealedCount)

        Thread.sleep(PracticeViewModel.ADVANCE_DEBOUNCE_THRESHOLD_MS + 20)

        // Advancing again and then switching deck
        val cardAfterRestart = viewModel.uiState.value.currentCard!!
        viewModel.updateAnswerInput(cardAfterRestart.correctAnswer)
        viewModel.submitAnswer()
        viewModel.nextCard()
        assertEquals(1, viewModel.uiState.value.sessionResults.size)

        // Switching deck resets results
        viewModel.selectDeck(PracticeDeckRepository.DECK_ANIMALS)
        assertEquals(0, viewModel.uiState.value.sessionResults.size)

        // Starting practice resets results
        viewModel.startPractice()
        assertEquals(0, viewModel.uiState.value.sessionResults.size)
    }

    @Test
    fun voiceSelection_updatesUiStateAndPersistsToStorage() {
        val testStorage = com.cogniapp.data.InMemoryVoiceStorage()
        val vm = PracticeViewModel(deckRepository, testStorage)

        val testVoices = listOf(
            com.cogniapp.model.CogniVoice(name = "en-us-x-sfg#female", displayName = "English (US) Calm Voice 1", locale = "en_US", isDefault = true),
            com.cogniapp.model.CogniVoice(name = "en-gb-x-rjs#male", displayName = "English (UK) Gentle Voice 2", locale = "en_GB", isDefault = false)
        )

        vm.setAvailableVoices(testVoices)
        assertEquals(2, vm.uiState.value.availableVoices.size)
        assertEquals("en-us-x-sfg#female", vm.uiState.value.selectedVoiceName)
        assertEquals("English (US) Calm Voice 1", vm.uiState.value.selectedVoiceDisplayName)

        // Select second voice
        vm.selectVoice("en-gb-x-rjs#male")
        assertEquals("en-gb-x-rjs#male", vm.uiState.value.selectedVoiceName)
        assertEquals("English (UK) Gentle Voice 2", vm.uiState.value.selectedVoiceDisplayName)
        assertEquals("en-gb-x-rjs#male", testStorage.getSelectedVoice())
    }

    @Test
    fun voiceSelection_restoresFromStorageOnInit() {
        val preloadedStorage = com.cogniapp.data.InMemoryVoiceStorage("en-gb-x-rjs#male")
        val vm = PracticeViewModel(deckRepository, preloadedStorage)

        assertEquals("en-gb-x-rjs#male", vm.uiState.value.selectedVoiceName)

        val testVoices = listOf(
            com.cogniapp.model.CogniVoice(name = "en-us-x-sfg#female", displayName = "English (US) Calm Voice 1", locale = "en_US", isDefault = true),
            com.cogniapp.model.CogniVoice(name = "en-gb-x-rjs#male", displayName = "English (UK) Gentle Voice 2", locale = "en_GB", isDefault = false)
        )
        vm.setAvailableVoices(testVoices)
        // Stored voice must take precedence over default
        assertEquals("en-gb-x-rjs#male", vm.uiState.value.selectedVoiceName)
        assertEquals("English (UK) Gentle Voice 2", vm.uiState.value.selectedVoiceDisplayName)
    }
}
