package com.cogniapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.cogniapp.audio.CogniAudioController
import com.cogniapp.ui.screens.GameScreen
import com.cogniapp.ui.screens.HomeScreen
import com.cogniapp.ui.theme.CogniAppTheme
import com.cogniapp.viewmodel.FeedbackState
import com.cogniapp.viewmodel.PracticeViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: PracticeViewModel by viewModels()
    private val audioController by lazy { CogniAudioController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by viewModel.uiState.collectAsState()

            CogniAppTheme(darkTheme = uiState.isDarkMode) {
                // Synchronize background ambient music with state
                LaunchedEffect(uiState.isMusicEnabled) {
                    audioController.setMusicEnabled(uiState.isMusicEnabled)
                }

                // Pause background music during active voice listening so mic hears user voice clearly
                LaunchedEffect(uiState.isListening, uiState.isMusicEnabled) {
                    if (uiState.isListening) {
                        audioController.pauseMusicForSpeech()
                    } else if (uiState.isMusicEnabled) {
                        audioController.resumeMusicAfterSpeech()
                    }
                }

                // Play gentle chime and tactile haptic feedback on match
                LaunchedEffect(uiState.feedbackState) {
                    if (uiState.feedbackState == FeedbackState.MATCHED) {
                        audioController.playMatchedChime(uiState.isHapticsAndSoundEnabled)
                    }
                }

                // Intercept system/hardware back press during gameplay to return to Home screen
                BackHandler(enabled = uiState.isSessionStarted) {
                    viewModel.returnToHome()
                }

                if (!uiState.isSessionStarted) {
                    HomeScreen(
                        uiState = uiState,
                        onSelectDeck = viewModel::selectDeck,
                        onToggleTimer = viewModel::toggleTimer,
                        onToggleMusic = viewModel::toggleMusic,
                        onToggleHapticsAndSound = viewModel::toggleHapticsAndSound,
                        onToggleDarkMode = viewModel::toggleDarkMode,
                        onStartPractice = viewModel::startPractice
                    )
                } else {
                    GameScreen(
                        uiState = uiState,
                        onUpdateAnswerInput = viewModel::updateAnswerInput,
                        onSubmitAnswer = viewModel::submitAnswer,
                        onRevealAnswer = viewModel::revealAnswer,
                        onNextCard = viewModel::nextCard,
                        onRestartDeck = viewModel::restartDeck,
                        onSelectDeck = viewModel::selectDeck,
                        onToggleTimer = viewModel::toggleTimer,
                        onPauseTimer = viewModel::pauseTimer,
                        onResumeTimer = viewModel::resumeTimer,
                        onReturnToHome = viewModel::returnToHome,
                        onToggleMusic = viewModel::toggleMusic,
                        onToggleHapticsAndSound = viewModel::toggleHapticsAndSound,
                        onSetListening = viewModel::setListening,
                        onSpeechResult = viewModel::onSpeechResult,
                        onSpeechError = viewModel::onSpeechError,
                        onSelectOption = viewModel::selectOption
                    )
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        audioController.onPause()
    }

    override fun onResume() {
        super.onResume()
        audioController.onResume(viewModel.uiState.value.isMusicEnabled)
    }

    override fun onDestroy() {
        super.onDestroy()
        audioController.release()
    }
}
