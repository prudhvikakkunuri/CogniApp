package com.cogniapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.cogniapp.R
import com.cogniapp.model.CogniVoice
import com.cogniapp.model.PracticeDeck
import com.cogniapp.ui.theme.CogniTheme
import com.cogniapp.viewmodel.PracticeUiState

/**
 * Responsive, Viewport-Fit Launch / Home Screen for CogniApp.
 * Refined with warm, approachable quiz-app styling inspired by modern friendly quiz design:
 * - Warm off-white canvas with rounded cards and clean slate ink
 * - Approachable sky blue and deep teal accents with coral highlights
 * - Adaptive layout fitting phones, tablets, and wide viewports (max-width bounded)
 * - Pinned bottomBar with prominent full-width "Play" button
 * - Clean category cards for all 10 decks (including All Decks Mix)
 * - Independent accessible toggles for 15s Timer, Background Music, and Haptics & Sounds
 * - Compact Light / Dark mode toggle with TalkBack accessibility
 */
@Composable
fun HomeScreen(
    uiState: PracticeUiState,
    onSelectDeck: (String) -> Unit,
    onToggleTimer: () -> Unit,
    onToggleMusic: () -> Unit,
    onToggleHapticsAndSound: () -> Unit,
    onToggleDarkMode: () -> Unit = {},
    onSelectVoice: (String) -> Unit = {},
    onPreviewVoice: (String) -> Unit = {},
    onStartPractice: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CogniTheme.colors.background,
        bottomBar = {
            // Pinned Bottom Bar: Full-width "Play" button always visible without scrolling
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
                    Button(
                        onClick = onStartPractice,
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 640.dp)
                            .defaultMinSize(minHeight = 60.dp)
                            .semantics {
                                role = Role.Button
                                contentDescription = "Play button. Start practice with ${uiState.deckTitle}."
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CogniTheme.colors.primaryAction,
                            contentColor = CogniTheme.colors.primaryActionText
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 4.dp
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(26.dp),
                                tint = CogniTheme.colors.primaryActionText
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Play",
                                style = CogniTheme.typography.titleMedium.copy(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = CogniTheme.colors.primaryActionText
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
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(350))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 680.dp)
                        .statusBarsPadding()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Compact Header: Logo + App Name + Subtitle + Light / Dark Mode Toggle
                    CompactAppHeader(
                        isDarkMode = uiState.isDarkMode,
                        onToggleDarkMode = onToggleDarkMode
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Section Label: Categories
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Choose a Category",
                            style = CogniTheme.typography.titleMedium.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = CogniTheme.colors.primaryInk
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Responsive Multi-Deck Grid (2 cols on phones, 3 cols on wide screens)
                    ResponsiveDeckGrid(
                        availableDecks = uiState.availableDecks,
                        selectedDeckId = uiState.currentDeckId,
                        onSelectDeck = onSelectDeck
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Compact Preferences & Assist Toggles Section
                    CompactPreferencesCard(
                        timerEnabled = uiState.timerEnabled,
                        isMusicEnabled = uiState.isMusicEnabled,
                        isHapticsAndSoundEnabled = uiState.isHapticsAndSoundEnabled,
                        availableVoices = uiState.availableVoices,
                        selectedVoiceName = uiState.selectedVoiceName,
                        selectedVoiceDisplayName = uiState.selectedVoiceDisplayName,
                        onToggleTimer = onToggleTimer,
                        onToggleMusic = onToggleMusic,
                        onToggleHapticsAndSound = onToggleHapticsAndSound,
                        onSelectVoice = onSelectVoice,
                        onPreviewVoice = onPreviewVoice
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

/**
 * Compact header block: Logo, app name, calm subtitle, and accessible Light / Dark mode toggle button.
 */
@Composable
private fun CompactAppHeader(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp, bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_cogni_logo),
            contentDescription = "CogniApp Emblem",
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(14.dp))
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "CogniApp",
                style = CogniTheme.typography.titleLarge.copy(
                    fontSize = 22.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                color = CogniTheme.colors.primaryInk,
                maxLines = 1,
                softWrap = false
            )
            Text(
                text = "Calm practice for everyday memory and focus",
                style = CogniTheme.typography.bodyLarge.copy(
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                ),
                color = CogniTheme.colors.secondaryInk,
                maxLines = 1,
                softWrap = false
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Compact Light / Dark Mode Toggle Button (>= 48dp touch target)
        Box(
            modifier = Modifier
                .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(CogniTheme.colors.surface)
                .border(1.5.dp, CogniTheme.colors.cardBorder, RoundedCornerShape(14.dp))
                .clickable(onClick = onToggleDarkMode)
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .semantics {
                    role = Role.Button
                    contentDescription = if (isDarkMode) {
                        "Dark mode active. Tap to switch to light mode."
                    } else {
                        "Light mode active. Tap to switch to dark mode."
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = if (isDarkMode) CogniTheme.colors.selectedOptionText else CogniTheme.colors.primaryAction
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isDarkMode) "Light" else "Dark",
                    style = CogniTheme.typography.labelLarge.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = CogniTheme.colors.primaryInk,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}

/**
 * Responsive Deck Grid adapting from 2 columns on phones to 3 columns on tablets.
 * Rounded, friendly quiz card geometry with clear visual hierarchy.
 */
@Composable
private fun ResponsiveDeckGrid(
    availableDecks: List<PracticeDeck>,
    selectedDeckId: String,
    onSelectDeck: (String) -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val columns = if (maxWidth >= 540.dp) 3 else 2
        val chunkedDecks = availableDecks.chunked(columns)

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            chunkedDecks.forEach { rowDecks ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowDecks.forEach { deck ->
                        val isSelected = deck.id.equals(selectedDeckId, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .defaultMinSize(minHeight = 60.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) CogniTheme.colors.selectedOptionBg else CogniTheme.colors.surface)
                                .border(
                                    1.5.dp,
                                    if (isSelected) CogniTheme.colors.primaryAction else CogniTheme.colors.cardBorder,
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { onSelectDeck(deck.id) }
                                .padding(horizontal = 10.dp, vertical = 9.dp)
                                .semantics {
                                    role = Role.Button
                                    contentDescription = "Category: ${deck.categoryLabel}, ${deck.cardCount} cards${if (isSelected) ", selected" else ", tap to select"}"
                                },
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) CogniTheme.colors.surface else CogniTheme.colors.badgeBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = deck.emoji,
                                        fontSize = 20.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = deck.categoryLabel,
                                        style = CogniTheme.typography.titleMedium.copy(
                                            fontSize = 14.sp,
                                            lineHeight = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = if (isSelected) CogniTheme.colors.primaryAction else CogniTheme.colors.primaryInk,
                                        maxLines = 2,
                                        softWrap = true
                                    )
                                    Text(
                                        text = "${deck.cardCount} cards",
                                        style = CogniTheme.typography.labelLarge.copy(fontSize = 11.sp),
                                        color = CogniTheme.colors.secondaryInk,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                                if (isSelected) {
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Box(
                                        modifier = Modifier
                                            .background(CogniTheme.colors.primaryAction, RoundedCornerShape(6.dp))
                                            .padding(horizontal = 5.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "✓",
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
                    }

                    // Balance empty slots on last row
                    if (rowDecks.size < columns) {
                        for (i in 0 until (columns - rowDecks.size)) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

/**
 * Compact preferences card containing independent controls for:
 * 1. Optional 15-Second Timer
 * 2. Soothing Background Music
 * 3. Haptic & Feedback Sounds
 * 4. Free Offline Read Aloud Voice Selection with Preview
 */
@Composable
private fun CompactPreferencesCard(
    timerEnabled: Boolean,
    isMusicEnabled: Boolean,
    isHapticsAndSoundEnabled: Boolean,
    availableVoices: List<CogniVoice>,
    selectedVoiceName: String?,
    selectedVoiceDisplayName: String,
    onToggleTimer: () -> Unit,
    onToggleMusic: () -> Unit,
    onToggleHapticsAndSound: () -> Unit,
    onSelectVoice: (String) -> Unit,
    onPreviewVoice: (String) -> Unit
) {
    var showVoiceDialog by remember { mutableStateOf(false) }

    if (showVoiceDialog) {
        VoicePickerDialog(
            availableVoices = availableVoices,
            selectedVoiceName = selectedVoiceName,
            onSelectVoice = { voiceName ->
                onSelectVoice(voiceName)
                showVoiceDialog = false
            },
            onPreviewVoice = onPreviewVoice,
            onDismiss = { showVoiceDialog = false }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, CogniTheme.colors.cardBorder, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CogniTheme.colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header
            Text(
                text = "Preferences & Assist",
                style = CogniTheme.typography.titleMedium.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = CogniTheme.colors.primaryInk
            )

            // 1. Optional 15-Second Timer Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 44.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = if (timerEnabled) CogniTheme.colors.primaryAction else CogniTheme.colors.secondaryInk
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "15-Second Timer",
                        style = CogniTheme.typography.titleMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = CogniTheme.colors.primaryInk
                    )
                    Text(
                        text = "Optional prompt pacing. Defaults to OFF.",
                        style = CogniTheme.typography.bodyLarge.copy(fontSize = 11.sp, lineHeight = 15.sp),
                        color = CogniTheme.colors.secondaryInk
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = timerEnabled,
                    onCheckedChange = { onToggleTimer() },
                    modifier = Modifier.semantics {
                        contentDescription = "Toggle 15-second countdown timer. Currently ${if (timerEnabled) "enabled" else "disabled"}."
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = CogniTheme.colors.primaryActionText,
                        checkedTrackColor = CogniTheme.colors.primaryAction,
                        uncheckedThumbColor = CogniTheme.colors.secondaryInk,
                        uncheckedTrackColor = CogniTheme.colors.badgeBg
                    )
                )
            }

            // 2. Soothing Background Music Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 44.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isMusicEnabled) Icons.Default.MusicNote else Icons.Default.MusicOff,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = if (isMusicEnabled) CogniTheme.colors.primaryAction else CogniTheme.colors.secondaryInk
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Background Music",
                        style = CogniTheme.typography.titleMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = CogniTheme.colors.primaryInk
                    )
                    Text(
                        text = "Soothing harmonic ambient soundscape.",
                        style = CogniTheme.typography.bodyLarge.copy(fontSize = 11.sp, lineHeight = 15.sp),
                        color = CogniTheme.colors.secondaryInk
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = isMusicEnabled,
                    onCheckedChange = { onToggleMusic() },
                    modifier = Modifier.semantics {
                        contentDescription = "Toggle background ambient music. Currently ${if (isMusicEnabled) "playing" else "muted"}."
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = CogniTheme.colors.primaryActionText,
                        checkedTrackColor = CogniTheme.colors.primaryAction,
                        uncheckedThumbColor = CogniTheme.colors.secondaryInk,
                        uncheckedTrackColor = CogniTheme.colors.badgeBg
                    )
                )
            }

            // 3. Tactile Haptic & Feedback Sounds Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 44.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isHapticsAndSoundEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeMute,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = if (isHapticsAndSoundEnabled) CogniTheme.colors.primaryAction else CogniTheme.colors.secondaryInk
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Haptics & Feedback Sounds",
                        style = CogniTheme.typography.titleMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = CogniTheme.colors.primaryInk
                    )
                    Text(
                        text = "Gentle vibration and match confirmation chime.",
                        style = CogniTheme.typography.bodyLarge.copy(fontSize = 11.sp, lineHeight = 15.sp),
                        color = CogniTheme.colors.secondaryInk
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = isHapticsAndSoundEnabled,
                    onCheckedChange = { onToggleHapticsAndSound() },
                    modifier = Modifier.semantics {
                        contentDescription = "Toggle gentle haptics and feedback sounds. Currently ${if (isHapticsAndSoundEnabled) "enabled" else "disabled"}."
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = CogniTheme.colors.primaryActionText,
                        checkedTrackColor = CogniTheme.colors.primaryAction,
                        uncheckedThumbColor = CogniTheme.colors.secondaryInk,
                        uncheckedTrackColor = CogniTheme.colors.badgeBg
                    )
                )
            }

            // 4. Free Offline Read Aloud Voice Selection with Preview
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 44.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.RecordVoiceOver,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = CogniTheme.colors.primaryAction
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { showVoiceDialog = true }
                        .semantics {
                            role = Role.Button
                            contentDescription = "Read aloud voice: $selectedVoiceDisplayName. Tap to change voice."
                        }
                ) {
                    Text(
                        text = "Read Aloud Voice",
                        style = CogniTheme.typography.titleMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = CogniTheme.colors.primaryInk
                    )
                    Text(
                        text = selectedVoiceDisplayName,
                        style = CogniTheme.typography.bodyLarge.copy(fontSize = 11.sp, lineHeight = 15.sp),
                        color = CogniTheme.colors.secondaryInk,
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                // Preview Voice Sample Button
                OutlinedButton(
                    onClick = {
                        val activeVoice = selectedVoiceName ?: availableVoices.firstOrNull()?.name ?: ""
                        onPreviewVoice(activeVoice)
                    },
                    modifier = Modifier
                        .defaultMinSize(minHeight = 32.dp)
                        .semantics {
                            role = Role.Button
                            contentDescription = "Preview voice sample"
                        },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CogniTheme.colors.cardBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = CogniTheme.colors.surface,
                        contentColor = CogniTheme.colors.primaryInk
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = CogniTheme.colors.primaryAction
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Preview",
                        style = CogniTheme.typography.labelLarge.copy(fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
                        color = CogniTheme.colors.primaryInk,
                        maxLines = 1,
                        softWrap = false
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                // Change Voice Button
                OutlinedButton(
                    onClick = { showVoiceDialog = true },
                    modifier = Modifier
                        .defaultMinSize(minHeight = 32.dp)
                        .semantics {
                            role = Role.Button
                            contentDescription = "Change read aloud voice"
                        },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CogniTheme.colors.cardBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = CogniTheme.colors.surface,
                        contentColor = CogniTheme.colors.primaryInk
                    )
                ) {
                    Text(
                        text = "Change",
                        style = CogniTheme.typography.labelLarge.copy(fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
                        color = CogniTheme.colors.primaryInk,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}

/**
 * Clean, accessible dialog for selecting an offline Read Aloud (TTS) voice.
 */
@Composable
private fun VoicePickerDialog(
    availableVoices: List<CogniVoice>,
    selectedVoiceName: String?,
    onSelectVoice: (String) -> Unit,
    onPreviewVoice: (String) -> Unit,
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
                    text = "Read Aloud Voice",
                    style = CogniTheme.typography.headlineMedium.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = CogniTheme.colors.primaryInk
                )
                Text(
                    text = "Select an offline English voice:",
                    style = CogniTheme.typography.bodyLarge.copy(fontSize = 13.sp),
                    color = CogniTheme.colors.secondaryInk
                )

                Spacer(modifier = Modifier.height(14.dp))

                if (availableVoices.isEmpty()) {
                    Text(
                        text = "System Default Voice (Offline)",
                        style = CogniTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                        color = CogniTheme.colors.primaryInk,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                } else {
                    availableVoices.forEach { voice ->
                        val isSelected = voice.name == selectedVoiceName ||
                                (selectedVoiceName == null && voice.isDefault)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 48.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) CogniTheme.colors.selectedOptionBg else CogniTheme.colors.surface)
                                .border(
                                    1.5.dp,
                                    if (isSelected) CogniTheme.colors.primaryAction else CogniTheme.colors.cardBorder,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { onSelectVoice(voice.name) }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .semantics {
                                    role = Role.Button
                                    contentDescription = "${voice.displayName}${if (isSelected) ", selected" else ""}"
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = voice.displayName,
                                    style = CogniTheme.typography.titleMedium.copy(
                                        fontSize = 14.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    ),
                                    color = if (isSelected) CogniTheme.colors.primaryAction else CogniTheme.colors.primaryInk
                                )
                                Text(
                                    text = voice.locale,
                                    style = CogniTheme.typography.bodyLarge.copy(fontSize = 11.sp),
                                    color = CogniTheme.colors.secondaryInk
                                )
                            }

                            // Sample preview button for each voice in dialog
                            OutlinedButton(
                                onClick = { onPreviewVoice(voice.name) },
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 30.dp),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CogniTheme.colors.cardBorder),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = CogniTheme.colors.surface,
                                    contentColor = CogniTheme.colors.primaryInk
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = "Test voice sample",
                                    modifier = Modifier.size(13.dp),
                                    tint = CogniTheme.colors.primaryAction
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Test",
                                    style = CogniTheme.typography.labelLarge.copy(fontSize = 11.sp),
                                    color = CogniTheme.colors.primaryInk
                                )
                            }

                            if (isSelected) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .background(CogniTheme.colors.primaryAction, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "✓",
                                        style = CogniTheme.typography.labelLarge.copy(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = CogniTheme.colors.primaryActionText
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Done Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CogniTheme.colors.primaryAction,
                        contentColor = CogniTheme.colors.primaryActionText
                    )
                ) {
                    Text(
                        text = "Done",
                        style = CogniTheme.typography.titleMedium.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = CogniTheme.colors.primaryActionText
                    )
                }
            }
        }
    }
}
