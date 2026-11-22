package com.cogniapp.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.cogniapp.model.PracticeCard
import com.cogniapp.model.PracticeDeck
import com.cogniapp.ui.theme.CogniTheme
import com.cogniapp.viewmodel.CardOutcome
import com.cogniapp.viewmodel.FeedbackState
import com.cogniapp.viewmodel.PracticeUiState
import com.cogniapp.viewmodel.SessionCardResult

/**
 * Calm, accessible practice screen for CogniApp with typing and Speech-to-Text (STT) input.
 *
 * Implements:
 * - Responsive width constraint (`widthIn(max = 680.dp)`) for phones, tablets, and wide screens
 * - Uncrowded top bar with non-wrapping "Home" and "Decks" buttons and centered progress
 * - In-game control strip: 15s live timer, independent Music toggle, independent Sound toggle
 * - Balanced clue / answer workbook card (160dp minHeight)
 * - Accessible Answer Input Zone: High-contrast text field + large Voice ("Speak") button + Submit
 * - Calm, non-punitive feedback states (MATCHED, TRY_AGAIN) with zero harsh alarms
 * - Pinned bottomBar with primary action ("Show Answer" / "Next Card")
 * - 16dp consistent outer margins with zero text clipping
 */
@Composable
fun GameScreen(
    uiState: PracticeUiState,
    onUpdateAnswerInput: (String) -> Unit,
    onSubmitAnswer: () -> Unit,
    onRevealAnswer: () -> Unit,
    onNextCard: () -> Unit,
    onRestartDeck: () -> Unit,
    onSelectDeck: (String) -> Unit,
    onToggleTimer: () -> Unit,
    onPauseTimer: () -> Unit,
    onResumeTimer: () -> Unit,
    onReturnToHome: () -> Unit,
    onToggleMusic: () -> Unit = {},
    onToggleHapticsAndSound: () -> Unit = {},
    onSetListening: (Boolean) -> Unit = {},
    onSpeechResult: (String) -> Unit = {},
    onSpeechError: (String?) -> Unit = {},
    @Suppress("UNUSED_PARAMETER") onSelectOption: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var isDeckPickerVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Intercept system Back press to return to the launch Home screen
    BackHandler(enabled = true) {
        onReturnToHome()
    }

    // Check speech recognition service availability
    val isRecognitionAvailable = remember(context) {
        SpeechRecognizer.isRecognitionAvailable(context)
    }

    val speechRecognizer = remember(context) {
        if (isRecognitionAvailable) {
            try {
                SpeechRecognizer.createSpeechRecognizer(context)
            } catch (_: Exception) {
                null
            }
        } else {
            null
        }
    }

    DisposableEffect(speechRecognizer) {
        val listener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {
                onSetListening(true)
            }
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {
                onSetListening(false)
            }
            override fun onError(error: Int) {
                onSetListening(false)
                val message = when (error) {
                    SpeechRecognizer.ERROR_NO_MATCH -> "Didn't catch that. Tap Speak again or type above."
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Listening timed out. Tap Speak again when you are ready."
                    SpeechRecognizer.ERROR_AUDIO -> "Audio recording issue. Please check microphone."
                    SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network issue. You can type your answer above."
                    else -> "Could not hear clearly. Tap Speak again or type above."
                }
                onSpeechError(message)
            }
            override fun onResults(results: Bundle?) {
                onSetListening(false)
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    onSpeechResult(matches[0])
                } else {
                    onSpeechError("Didn't catch that. Tap Speak again or type above.")
                }
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        }

        speechRecognizer?.setRecognitionListener(listener)

        onDispose {
            speechRecognizer?.destroy()
        }
    }

    val startListeningAction = {
        if (!isRecognitionAvailable || speechRecognizer == null) {
            onSpeechError("Voice input is not available on this device. You can type your answer above.")
        } else {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Say your answer...")
            }
            try {
                onSetListening(true)
                speechRecognizer.startListening(intent)
            } catch (_: Exception) {
                onSetListening(false)
                onSpeechError("Could not start microphone. You can type your answer above.")
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startListeningAction()
        } else {
            onSpeechError("Microphone permission is required for voice input. You can type your answer above.")
        }
    }

    val onToggleSpeech = {
        if (uiState.isListening) {
            try {
                speechRecognizer?.stopListening()
            } catch (_: Exception) {}
            onSetListening(false)
        } else {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED

            if (hasPermission) {
                startListeningAction()
            } else {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CogniTheme.colors.background,
        topBar = {
            // Uncongested single-tier top bar with non-wrapping buttons
            Surface(
                color = CogniTheme.colors.background,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.widthIn(max = 680.dp)) {
                        GameTopBar(
                            uiState = uiState,
                            onReturnToHome = onReturnToHome,
                            onOpenDeckPicker = { isDeckPickerVisible = true }
                        )
                    }
                }
            }
        },
        bottomBar = {
            // Pinned Bottom Bar: primary action always accessible above navigation bar
            if (!uiState.isSessionComplete && uiState.currentCard != null) {
                Surface(
                    color = CogniTheme.colors.background,
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.widthIn(max = 640.dp)) {
                            PinnedActionButton(
                                isAnswerRevealed = uiState.isAnswerRevealed,
                                onRevealAnswer = onRevealAnswer,
                                onNextCard = onNextCard
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (uiState.isSessionComplete) {
                    // Calm, Respectful Session Results Summary
                    SessionCompleteView(
                        uiState = uiState,
                        onStartOver = onRestartDeck,
                        onOpenDeckPicker = { isDeckPickerVisible = true },
                        onReturnToHome = onReturnToHome
                    )
                } else {
                    uiState.currentCard?.let { card ->
                        // In-Game Control Strip (Timer, Music, Sounds)
                        InGameControlStrip(
                            timerEnabled = uiState.timerEnabled,
                            secondsRemaining = uiState.secondsRemaining,
                            isTimerPaused = uiState.isTimerPaused,
                            isAnswerRevealed = uiState.isAnswerRevealed,
                            isMusicEnabled = uiState.isMusicEnabled,
                            isHapticsAndSoundEnabled = uiState.isHapticsAndSoundEnabled,
                            onToggleTimer = onToggleTimer,
                            onPauseTimer = onPauseTimer,
                            onResumeTimer = onResumeTimer,
                            onToggleMusic = onToggleMusic,
                            onToggleHapticsAndSound = onToggleHapticsAndSound
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Balanced Clue or Answer Card
                        BalancedWorkbookCard(
                            card = card,
                            isAnswerRevealed = uiState.isAnswerRevealed,
                            feedbackState = uiState.feedbackState
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Accessible Answer Input Zone: Typing + Voice Input
                        AnswerInputZone(
                            userAnswerInput = uiState.userAnswerInput,
                            isListening = uiState.isListening,
                            isAnswerRevealed = uiState.isAnswerRevealed,
                            feedbackState = uiState.feedbackState,
                            speechStatusMessage = uiState.speechStatusMessage,
                            onUpdateAnswerInput = onUpdateAnswerInput,
                            onSubmitAnswer = onSubmitAnswer,
                            onToggleSpeech = onToggleSpeech
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

    // Modal Deck Picker Dialog
    if (isDeckPickerVisible) {
        DeckPickerDialog(
            availableDecks = uiState.availableDecks,
            currentDeckId = uiState.currentDeckId,
            onSelectDeck = { deckId ->
                onSelectDeck(deckId)
                isDeckPickerVisible = false
            },
            onDismiss = { isDeckPickerVisible = false }
        )
    }
}

/**
 * Top bar: Left Home button, Centered Title + Progress, Right Decks button.
 * Uses softWrap = false and maxLines = 1 to strictly prevent awkward word-wrapping like "Deck\ns".
 */
@Composable
private fun GameTopBar(
    uiState: PracticeUiState,
    onReturnToHome: () -> Unit,
    onOpenDeckPicker: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Home navigation button
        OutlinedButton(
            onClick = onReturnToHome,
            modifier = Modifier
                .defaultMinSize(minHeight = 40.dp)
                .semantics {
                    role = Role.Button
                    contentDescription = "Home button. Return to category selection."
                },
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, CogniTheme.colors.cardBorder),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = CogniTheme.colors.surface,
                contentColor = CogniTheme.colors.primaryInk
            )
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = CogniTheme.colors.primaryAction
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Home",
                style = CogniTheme.typography.labelLarge.copy(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
                color = CogniTheme.colors.primaryInk,
                maxLines = 1,
                softWrap = false
            )
        }

        // Center: Title & Progress Counter with clean breathing room
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = uiState.deckTitle,
                style = CogniTheme.typography.titleMedium.copy(
                    fontSize = 17.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = CogniTheme.colors.primaryInk,
                textAlign = TextAlign.Center,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Card ${if (uiState.totalCards > 0 && !uiState.isSessionComplete) uiState.currentIndex + 1 else uiState.totalCards} of ${uiState.totalCards}",
                style = CogniTheme.typography.bodyLarge.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = CogniTheme.colors.secondaryInk,
                textAlign = TextAlign.Center,
                maxLines = 1,
                softWrap = false
            )
        }

        // Right: Decks modal button (anti-wrapping)
        OutlinedButton(
            onClick = onOpenDeckPicker,
            modifier = Modifier
                .defaultMinSize(minHeight = 40.dp)
                .semantics {
                    role = Role.Button
                    contentDescription = "Decks button. Switch to another practice category."
                },
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, CogniTheme.colors.cardBorder),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = CogniTheme.colors.surface,
                contentColor = CogniTheme.colors.primaryInk
            )
        ) {
            Text(
                text = "Decks",
                style = CogniTheme.typography.labelLarge.copy(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
                color = CogniTheme.colors.primaryInk,
                maxLines = 1,
                softWrap = false
            )
        }
    }
}

/**
 * Dedicated In-Game Control Strip containing:
 * - 15-second countdown timer badge/toggle
 * - Compact background music toggle button
 * - Compact sound & haptic feedback toggle button
 */
@Composable
private fun InGameControlStrip(
    timerEnabled: Boolean,
    secondsRemaining: Int,
    isTimerPaused: Boolean,
    isAnswerRevealed: Boolean,
    isMusicEnabled: Boolean,
    isHapticsAndSoundEnabled: Boolean,
    onToggleTimer: () -> Unit,
    onPauseTimer: () -> Unit,
    onResumeTimer: () -> Unit,
    onToggleMusic: () -> Unit,
    onToggleHapticsAndSound: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Timer controls
        if (!timerEnabled) {
            OutlinedButton(
                onClick = onToggleTimer,
                modifier = Modifier
                    .defaultMinSize(minHeight = 32.dp)
                    .semantics {
                        role = Role.Button
                        contentDescription = "Turn on optional 15-second timer"
                    },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CogniTheme.colors.cardBorder),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = CogniTheme.colors.surface,
                    contentColor = CogniTheme.colors.secondaryInk
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = CogniTheme.colors.secondaryInk
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Timer",
                    style = CogniTheme.typography.labelLarge.copy(fontSize = 12.sp),
                    color = CogniTheme.colors.secondaryInk,
                    maxLines = 1,
                    softWrap = false
                )
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Countdown Badge
                Box(
                    modifier = Modifier
                        .background(CogniTheme.colors.selectedOptionBg, RoundedCornerShape(8.dp))
                        .border(1.dp, CogniTheme.colors.primaryAction, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = CogniTheme.colors.primaryAction
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isTimerPaused) "$secondsRemaining s (P)" else "$secondsRemaining s",
                            style = CogniTheme.typography.labelLarge.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
                            color = CogniTheme.colors.primaryAction,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                if (!isAnswerRevealed) {
                    OutlinedButton(
                        onClick = { if (isTimerPaused) onResumeTimer() else onPauseTimer() },
                        modifier = Modifier.defaultMinSize(minHeight = 32.dp),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CogniTheme.colors.cardBorder),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = CogniTheme.colors.surface,
                            contentColor = CogniTheme.colors.primaryInk
                        )
                    ) {
                        Icon(
                            imageVector = if (isTimerPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                            contentDescription = if (isTimerPaused) "Resume timer" else "Pause timer",
                            modifier = Modifier.size(13.dp),
                            tint = CogniTheme.colors.primaryAction
                        )
                    }
                }

                OutlinedButton(
                    onClick = onToggleTimer,
                    modifier = Modifier.defaultMinSize(minHeight = 32.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CogniTheme.colors.cardBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = CogniTheme.colors.surface,
                        contentColor = CogniTheme.colors.secondaryInk
                    )
                ) {
                    Text(
                        text = "Off",
                        style = CogniTheme.typography.labelLarge.copy(fontSize = 11.sp),
                        color = CogniTheme.colors.secondaryInk,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }

        // Right: Compact Music & Sound Toggle Buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Music Toggle Button
            OutlinedButton(
                onClick = onToggleMusic,
                modifier = Modifier
                    .defaultMinSize(minHeight = 32.dp)
                    .semantics {
                        role = Role.Button
                        contentDescription = "Background music. Currently ${if (isMusicEnabled) "on. Tap to turn off." else "off. Tap to turn on."}"
                    },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isMusicEnabled) CogniTheme.colors.primaryAction else CogniTheme.colors.cardBorder
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isMusicEnabled) CogniTheme.colors.selectedOptionBg else CogniTheme.colors.surface,
                    contentColor = if (isMusicEnabled) CogniTheme.colors.primaryAction else CogniTheme.colors.secondaryInk
                )
            ) {
                Icon(
                    imageVector = if (isMusicEnabled) Icons.Default.MusicNote else Icons.Default.MusicOff,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = if (isMusicEnabled) CogniTheme.colors.primaryAction else CogniTheme.colors.secondaryInk
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Music",
                    style = CogniTheme.typography.labelLarge.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
                    color = if (isMusicEnabled) CogniTheme.colors.primaryAction else CogniTheme.colors.secondaryInk,
                    maxLines = 1,
                    softWrap = false
                )
            }

            // Sound Toggle Button
            OutlinedButton(
                onClick = onToggleHapticsAndSound,
                modifier = Modifier
                    .defaultMinSize(minHeight = 32.dp)
                    .semantics {
                        role = Role.Button
                        contentDescription = "Feedback sounds and haptics. Currently ${if (isHapticsAndSoundEnabled) "on. Tap to turn off." else "off. Tap to turn on."}"
                    },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isHapticsAndSoundEnabled) CogniTheme.colors.primaryAction else CogniTheme.colors.cardBorder
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isHapticsAndSoundEnabled) CogniTheme.colors.selectedOptionBg else CogniTheme.colors.surface,
                    contentColor = if (isHapticsAndSoundEnabled) CogniTheme.colors.primaryAction else CogniTheme.colors.secondaryInk
                )
            ) {
                Icon(
                    imageVector = if (isHapticsAndSoundEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeMute,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = if (isHapticsAndSoundEnabled) CogniTheme.colors.primaryAction else CogniTheme.colors.secondaryInk
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Sound",
                    style = CogniTheme.typography.labelLarge.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
                    color = if (isHapticsAndSoundEnabled) CogniTheme.colors.primaryAction else CogniTheme.colors.secondaryInk,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}

/**
 * Balanced Clue or Answer Card surface (160dp minHeight).
 */
@Composable
private fun BalancedWorkbookCard(
    card: PracticeCard,
    isAnswerRevealed: Boolean,
    feedbackState: FeedbackState
) {
    val cardBg = if (isAnswerRevealed) CogniTheme.colors.revealCardSurface else CogniTheme.colors.clueCardSurface
    val cardBorder = if (isAnswerRevealed) CogniTheme.colors.revealCardBorder else CogniTheme.colors.cardBorder

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 160.dp)
            .border(1.5.dp, cardBorder, RoundedCornerShape(18.dp))
            .semantics {
                contentDescription = if (isAnswerRevealed) {
                    "Answer: ${card.correctAnswer}. ${card.clue}"
                } else {
                    "Clue: ${card.clue}"
                }
            },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!isAnswerRevealed) {
                // Clue Text: 22sp–24sp with generous 32sp line height (1.45x)
                Text(
                    text = card.clue,
                    style = CogniTheme.typography.headlineMedium.copy(
                        fontSize = 22.sp,
                        lineHeight = 32.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = CogniTheme.colors.primaryInk,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                // Reveal Text: Emoji + Answer Title + Calm status
                Text(
                    text = card.emoji,
                    fontSize = 44.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = card.correctAnswer,
                    style = CogniTheme.typography.headlineMedium.copy(
                        fontSize = 26.sp,
                        lineHeight = 32.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = CogniTheme.colors.primaryInk,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                val statusText = when (feedbackState) {
                    FeedbackState.MATCHED -> "Correct answer: ${card.correctAnswer}"
                    else -> "Answer: ${card.correctAnswer}"
                }
                Text(
                    text = statusText,
                    style = CogniTheme.typography.bodyLarge.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color = CogniTheme.colors.secondaryInk,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Accessible Answer Input Zone supporting high-contrast typing and Speech-to-Text (STT) voice input.
 */
@Composable
private fun AnswerInputZone(
    userAnswerInput: String,
    isListening: Boolean,
    isAnswerRevealed: Boolean,
    feedbackState: FeedbackState,
    speechStatusMessage: String?,
    onUpdateAnswerInput: (String) -> Unit,
    onSubmitAnswer: () -> Unit,
    onToggleSpeech: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // High-contrast, large text input field
        OutlinedTextField(
            value = userAnswerInput,
            onValueChange = onUpdateAnswerInput,
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 58.dp)
                .semantics {
                    contentDescription = "Answer text input. Enter or speak your answer."
                },
            enabled = !isAnswerRevealed,
            textStyle = CogniTheme.typography.titleMedium.copy(
                fontSize = 20.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.Medium,
                color = CogniTheme.colors.primaryInk
            ),
            placeholder = {
                Text(
                    text = "Type or speak your answer...",
                    style = CogniTheme.typography.bodyLarge.copy(
                        fontSize = 17.sp,
                        color = CogniTheme.colors.mutedInk
                    )
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CogniTheme.colors.surface,
                unfocusedContainerColor = CogniTheme.colors.surface,
                disabledContainerColor = CogniTheme.colors.surface,
                focusedBorderColor = CogniTheme.colors.primaryAction,
                unfocusedBorderColor = CogniTheme.colors.cardBorder,
                disabledBorderColor = CogniTheme.colors.cardBorder,
                cursorColor = CogniTheme.colors.primaryAction
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences
            ),
            keyboardActions = KeyboardActions(
                onDone = { onSubmitAnswer() }
            ),
            trailingIcon = {
                if (userAnswerInput.isNotBlank() && !isAnswerRevealed) {
                    IconButton(
                        onClick = { onUpdateAnswerInput("") },
                        modifier = Modifier.semantics {
                            contentDescription = "Clear answer input"
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = CogniTheme.colors.secondaryInk,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        )

        // Action Buttons Row: Microphone Button & Submit Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Large Voice / Microphone Button (>= 60dp)
            Button(
                onClick = onToggleSpeech,
                enabled = !isAnswerRevealed,
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 60.dp)
                    .semantics {
                        role = Role.Button
                        contentDescription = if (isListening) {
                            "Listening to voice input. Tap to stop."
                        } else {
                            "Speak your answer using voice."
                        }
                    },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isListening) CogniTheme.colors.primaryAction else CogniTheme.colors.selectedOptionBg,
                    contentColor = if (isListening) CogniTheme.colors.primaryActionText else CogniTheme.colors.primaryAction
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    CogniTheme.colors.primaryAction
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isListening) "Listening..." else "Speak",
                        style = CogniTheme.typography.titleMedium.copy(
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            // Large Submit Button (>= 60dp)
            Button(
                onClick = onSubmitAnswer,
                enabled = !isAnswerRevealed && userAnswerInput.isNotBlank(),
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 60.dp)
                    .semantics {
                        role = Role.Button
                        contentDescription = "Submit answer."
                    },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CogniTheme.colors.primaryAction,
                    contentColor = CogniTheme.colors.primaryActionText,
                    disabledContainerColor = CogniTheme.colors.badgeBg,
                    disabledContentColor = CogniTheme.colors.mutedInk
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Submit",
                        style = CogniTheme.typography.titleMedium.copy(
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }

        // Calm, non-punitive feedback area
        when (feedbackState) {
            FeedbackState.MATCHED -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CogniTheme.colors.selectedOptionBg, RoundedCornerShape(10.dp))
                        .border(1.5.dp, CogniTheme.colors.primaryAction, RoundedCornerShape(10.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = CogniTheme.colors.primaryAction,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "That's right! Tap Next Card below to continue.",
                            style = CogniTheme.typography.bodyLarge.copy(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = CogniTheme.colors.primaryAction,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            FeedbackState.TRY_AGAIN -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CogniTheme.colors.unmatchedBg, RoundedCornerShape(10.dp))
                        .border(1.5.dp, CogniTheme.colors.unmatchedBorder, RoundedCornerShape(10.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Not quite yet — try again, or tap Show Answer below.",
                        style = CogniTheme.typography.bodyLarge.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = CogniTheme.colors.unmatchedText,
                        textAlign = TextAlign.Center
                    )
                }
            }
            FeedbackState.IDLE -> {}
        }

        // Friendly speech recognition status or error message
        if (!speechStatusMessage.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CogniTheme.colors.surface, RoundedCornerShape(10.dp))
                .border(1.dp, CogniTheme.colors.cardBorder, RoundedCornerShape(10.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = speechStatusMessage,
                    style = CogniTheme.typography.bodyLarge.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = CogniTheme.colors.secondaryInk,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Pinned Bottom Bar: primary action always accessible above navigation bar.
 */
@Composable
private fun PinnedActionButton(
    isAnswerRevealed: Boolean,
    onRevealAnswer: () -> Unit,
    onNextCard: () -> Unit
) {
    Button(
        onClick = {
            if (isAnswerRevealed) {
                onNextCard()
            } else {
                onRevealAnswer()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 60.dp)
            .semantics {
                role = Role.Button
                contentDescription = if (isAnswerRevealed) {
                    "Next Card button. Advance to the next card."
                } else {
                    "Show Answer button. Reveal the correct answer."
                }
            },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isAnswerRevealed) CogniTheme.colors.primaryAction else CogniTheme.colors.surface,
            contentColor = if (isAnswerRevealed) CogniTheme.colors.primaryActionText else CogniTheme.colors.primaryAction
        ),
        border = if (!isAnswerRevealed) androidx.compose.foundation.BorderStroke(1.5.dp, CogniTheme.colors.primaryAction) else null,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = if (isAnswerRevealed) "Next Card" else "Show Answer",
            style = CogniTheme.typography.titleMedium.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            color = if (isAnswerRevealed) CogniTheme.colors.primaryActionText else CogniTheme.colors.primaryAction
        )
    }
}

/**
 * Calm, respectful session results summary when a user completes a deck in CogniApp.
 *
 * Implements:
 * - Completed deck title and total cards completed
 * - Gentle counts of matched vs. revealed/skipped cards with respectful, non-punitive wording
 * - Clean card-by-card recap of clues, correct answers, and entered/spoken attempts
 * - Clear, non-wrapping action buttons (>= 60dp tall) fitting phones and tablets
 */
@Composable
private fun SessionCompleteView(
    uiState: PracticeUiState,
    onStartOver: () -> Unit,
    onOpenDeckPicker: () -> Unit,
    onReturnToHome: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, CogniTheme.colors.cardBorder, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CogniTheme.colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Calm completion mark & title
            Text(
                text = "✨",
                fontSize = 40.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Session Complete",
                style = CogniTheme.typography.headlineMedium.copy(
                    fontSize = 24.sp,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = CogniTheme.colors.primaryInk,
                textAlign = TextAlign.Center
            )
            Text(
                text = "You finished all ${uiState.totalCards} cards in ${uiState.deckTitle}.",
                style = CogniTheme.typography.bodyLarge.copy(
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                ),
                color = CogniTheme.colors.secondaryInk,
                textAlign = TextAlign.Center
            )

            // Gentle Outcome Stats Row (Non-punitive)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Recalled Directly
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.5.dp, CogniTheme.colors.selectedOptionBorder, RoundedCornerShape(10.dp)),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = CogniTheme.colors.selectedOptionBg)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${uiState.matchedCount}",
                            style = CogniTheme.typography.titleLarge.copy(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = CogniTheme.colors.selectedOptionText
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Recalled Directly",
                            style = CogniTheme.typography.labelMedium.copy(fontSize = 11.sp),
                            color = CogniTheme.colors.selectedOptionText,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                // Answer Viewed
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.5.dp, CogniTheme.colors.revealCardBorder, RoundedCornerShape(10.dp)),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = CogniTheme.colors.revealCardSurface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${uiState.revealedCount}",
                            style = CogniTheme.typography.titleLarge.copy(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = CogniTheme.colors.primaryInk
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Answer Viewed",
                            style = CogniTheme.typography.labelMedium.copy(fontSize = 11.sp),
                            color = CogniTheme.colors.secondaryInk,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                // Skipped (if any)
                if (uiState.skippedCount > 0) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.5.dp, CogniTheme.colors.cardBorder, RoundedCornerShape(10.dp)),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = CogniTheme.colors.badgeBg)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${uiState.skippedCount}",
                                style = CogniTheme.typography.titleLarge.copy(
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = CogniTheme.colors.primaryInk
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Skipped",
                                style = CogniTheme.typography.labelMedium.copy(fontSize = 11.sp),
                                color = CogniTheme.colors.secondaryInk,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }

            // Warm Encouraging Appreciation Note
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(CogniTheme.colors.selectedOptionBg)
                    .border(1.dp, CogniTheme.colors.selectedOptionBorder, RoundedCornerShape(10.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Thank you for practicing today! Your effort and focus in completing this deck are wonderful for keeping your memory active.",
                    style = CogniTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color = CogniTheme.colors.selectedOptionText,
                    textAlign = TextAlign.Center
                )
            }

            // Respectful, adult educational reinforcement
            Text(
                text = "Viewing clues and recalling familiar items strengthens memory pathways and everyday focus.",
                style = CogniTheme.typography.bodyMedium.copy(
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                ),
                color = CogniTheme.colors.secondaryInk,
                textAlign = TextAlign.Center
            )

            // Card-by-Card Recap Section
            if (uiState.sessionResults.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Card-by-Card Recap",
                            style = CogniTheme.typography.titleMedium.copy(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = CogniTheme.colors.primaryInk
                        )
                    }

                    uiState.sessionResults.forEachIndexed { index, result ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(CogniTheme.colors.surface)
                                .border(1.dp, CogniTheme.colors.cardBorder, RoundedCornerShape(10.dp))
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = result.card.emoji,
                                        fontSize = 18.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Card ${index + 1}: ${result.correctAnswer}",
                                        style = CogniTheme.typography.titleMedium.copy(
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = CogniTheme.colors.primaryInk,
                                        modifier = Modifier.weight(1f)
                                    )
                                    // Outcome Badge
                                    val (badgeText, badgeBg, badgeTextColor, badgeBorder) = when (result.outcome) {
                                        CardOutcome.MATCHED -> Quadruple(
                                            "Recalled",
                                            CogniTheme.colors.selectedOptionBg,
                                            CogniTheme.colors.selectedOptionText,
                                            CogniTheme.colors.selectedOptionBorder
                                        )
                                        CardOutcome.REVEALED -> Quadruple(
                                            "Answer Viewed",
                                            CogniTheme.colors.revealCardSurface,
                                            CogniTheme.colors.primaryInk,
                                            CogniTheme.colors.revealCardBorder
                                        )
                                        CardOutcome.SKIPPED -> Quadruple(
                                            "Skipped",
                                            CogniTheme.colors.badgeBg,
                                            CogniTheme.colors.secondaryInk,
                                            CogniTheme.colors.cardBorder
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .background(badgeBg, RoundedCornerShape(6.dp))
                                            .border(1.dp, badgeBorder, RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = badgeText,
                                            style = CogniTheme.typography.labelSmall.copy(
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold
                                            ),
                                            color = badgeTextColor
                                        )
                                    }
                                }

                                Text(
                                    text = result.card.clue,
                                    style = CogniTheme.typography.bodySmall.copy(
                                        fontSize = 13.sp,
                                        lineHeight = 17.sp
                                    ),
                                    color = CogniTheme.colors.secondaryInk
                                )

                                if (result.userAnswer.isNotBlank()) {
                                    if (result.outcome == CardOutcome.MATCHED) {
                                        Text(
                                            text = "Your answer: \"${result.userAnswer}\"",
                                            style = CogniTheme.typography.labelSmall.copy(
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            ),
                                            color = CogniTheme.colors.selectedOptionText
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(CogniTheme.colors.unmatchedBg)
                                                .border(1.dp, CogniTheme.colors.unmatchedBorder, RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = "Your answer: \"${result.userAnswer}\"",
                                                style = CogniTheme.typography.labelSmall.copy(
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                ),
                                                color = CogniTheme.colors.unmatchedText
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Primary Action 1: Play Again (>= 60dp tall)
            Button(
                onClick = onStartOver,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 60.dp)
                    .semantics {
                        role = Role.Button
                        contentDescription = "Play again with ${uiState.deckTitle}."
                    },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CogniTheme.colors.primaryAction,
                    contentColor = CogniTheme.colors.primaryActionText
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                        tint = CogniTheme.colors.primaryActionText
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Play Again",
                        style = CogniTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = CogniTheme.colors.primaryActionText,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            // Secondary Action 2: Choose Another Category (>= 60dp tall)
            OutlinedButton(
                onClick = onOpenDeckPicker,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 60.dp)
                    .semantics {
                        role = Role.Button
                        contentDescription = "Choose another practice category."
                    },
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, CogniTheme.colors.primaryAction),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = CogniTheme.colors.surface,
                    contentColor = CogniTheme.colors.primaryAction
                )
            ) {
                Text(
                    text = "Choose Another Category",
                    style = CogniTheme.typography.titleMedium.copy(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = CogniTheme.colors.primaryAction,
                    maxLines = 1,
                    softWrap = false
                )
            }

            // Tertiary Action 3: Return to Home (>= 60dp tall)
            OutlinedButton(
                onClick = onReturnToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 60.dp)
                    .semantics {
                        role = Role.Button
                        contentDescription = "Return to launch home screen."
                    },
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, CogniTheme.colors.cardBorder),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = CogniTheme.colors.surface,
                    contentColor = CogniTheme.colors.secondaryInk
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = CogniTheme.colors.secondaryInk
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Return to Home",
                        style = CogniTheme.typography.titleMedium.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = CogniTheme.colors.secondaryInk,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

/**
 * Accessible Modal Deck Picker Dialog.
 */
@Composable
private fun DeckPickerDialog(
    availableDecks: List<PracticeDeck>,
    currentDeckId: String,
    onSelectDeck: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = CogniTheme.colors.surface,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, CogniTheme.colors.cardBorder),
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 500.dp)
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(20.dp)
            ) {
                Text(
                    text = "Choose a Category",
                    style = CogniTheme.typography.headlineMedium.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = CogniTheme.colors.primaryInk
                )
                Text(
                    text = "Select a practice deck below:",
                    style = CogniTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                    color = CogniTheme.colors.secondaryInk
                )

                Spacer(modifier = Modifier.height(14.dp))

                availableDecks.forEach { deck ->
                    val isSelected = deck.id.equals(currentDeckId, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 52.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) CogniTheme.colors.selectedOptionBg else CogniTheme.colors.surface)
                            .border(
                                1.5.dp,
                                if (isSelected) CogniTheme.colors.primaryAction else CogniTheme.colors.cardBorder,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { onSelectDeck(deck.id) }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                            .semantics {
                                role = Role.Button
                                contentDescription = "${deck.categoryLabel}, ${deck.cardCount} cards${if (isSelected) ", currently selected" else ""}"
                            },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = deck.emoji,
                                fontSize = 24.sp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = deck.categoryLabel,
                                    style = CogniTheme.typography.titleMedium.copy(
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = if (isSelected) CogniTheme.colors.primaryAction else CogniTheme.colors.primaryInk
                                )
                                Text(
                                    text = "${deck.cardCount} cards",
                                    style = CogniTheme.typography.labelLarge.copy(fontSize = 12.sp),
                                    color = CogniTheme.colors.secondaryInk
                                )
                            }
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .background(CogniTheme.colors.primaryAction, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Active",
                                        style = CogniTheme.typography.labelLarge.copy(
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = CogniTheme.colors.primaryActionText
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 48.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, CogniTheme.colors.cardBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = CogniTheme.colors.surface,
                        contentColor = CogniTheme.colors.secondaryInk
                    )
                ) {
                    Text(
                        text = "Close",
                        style = CogniTheme.typography.titleMedium.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = CogniTheme.colors.secondaryInk
                    )
                }
            }
        }
    }
}
