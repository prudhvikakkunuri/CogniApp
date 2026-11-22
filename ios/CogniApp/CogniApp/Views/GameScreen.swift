import SwiftUI

/// Accessible gameplay screen for CogniApp on iOS & iPadOS.
/// Fully matches Android GameScreen:
/// - Uncrowded top bar with non-wrapping "Home" and "Decks" buttons and centered progress
/// - In-game control strip: 15s Timer, independent Music toggle, independent Sound toggle
/// - Balanced clue/answer card (minHeight 160)
/// - Answer input zone: High-contrast text field + large Voice ("Speak") button + Submit
/// - Calm, non-punitive feedback states (MATCHED, TRY_AGAIN)
/// - Pinned bottom action bar ("Show Answer" / "Next Card")
/// - Automatic transition to ResultsView on deck completion
public struct GameScreen: View {
    @ObservedObject public var viewModel: PracticeViewModel
    @State private var isDeckPickerPresented: Bool = false
    @FocusState private var isInputFocused: Bool

    private var palette: CogniThemePalette {
        viewModel.palette
    }

    public var body: some View {
        ZStack {
            palette.background
                .ignoresSafeArea()

            if viewModel.isSessionComplete {
                ResultsView(
                    viewModel: viewModel,
                    isDeckPickerPresented: $isDeckPickerPresented
                )
            } else {
                VStack(spacing: 0) {
                    // Top Bar
                    HStack(alignment: .center) {
                        // Home Button
                        Button(action: {
                            viewModel.returnToHome()
                        }) {
                            HStack(spacing: 4) {
                                Image(systemName: "house.fill")
                                    .font(.system(size: 14))
                                Text("Home")
                                    .font(CogniTypography.bodyBold(size: 14))
                            }
                            .foregroundColor(palette.primaryInk)
                            .padding(.horizontal, 10)
                            .padding(.vertical, 8)
                            .background(palette.surface)
                            .clipShape(RoundedRectangle(cornerRadius: 10, style: .continuous))
                            .overlay(
                                RoundedRectangle(cornerRadius: 10, style: .continuous)
                                    .stroke(palette.cardBorder, lineWidth: 1)
                            )
                        }

                        Spacer()

                        // Centered Progress & Title
                        VStack(spacing: 2) {
                            Text(viewModel.deckTitle)
                                .font(CogniTypography.bodyBold(size: 16))
                                .foregroundColor(palette.primaryInk)
                                .lineLimit(1)
                            Text(viewModel.cardProgressText)
                                .font(CogniTypography.caption(size: 12))
                                .foregroundColor(palette.secondaryInk)
                        }

                        Spacer()

                        // Decks Button
                        Button(action: {
                            isDeckPickerPresented = true
                        }) {
                            HStack(spacing: 4) {
                                Image(systemName: "square.stack.fill")
                                    .font(.system(size: 14))
                                Text("Decks")
                                    .font(CogniTypography.bodyBold(size: 14))
                            }
                            .foregroundColor(palette.primaryInk)
                            .padding(.horizontal, 10)
                            .padding(.vertical, 8)
                            .background(palette.surface)
                            .clipShape(RoundedRectangle(cornerRadius: 10, style: .continuous))
                            .overlay(
                                RoundedRectangle(cornerRadius: 10, style: .continuous)
                                    .stroke(palette.cardBorder, lineWidth: 1)
                            )
                        }
                    }
                    .padding(.horizontal, 16)
                    .padding(.top, 8)
                    .frame(maxWidth: 680)

                    // Scrollable Practice Content
                    ScrollView {
                        VStack(spacing: 12) {
                            // In-Game Control Strip
                            HStack {
                                // Left: 15s Timer Controls
                                HStack(spacing: 6) {
                                    Image(systemName: "timer")
                                        .font(.system(size: 14))
                                        .foregroundColor(palette.secondaryInk)

                                    if viewModel.timerEnabled {
                                        Text("\(viewModel.secondsRemaining)s")
                                            .font(CogniTypography.bodyBold(size: 13))
                                            .foregroundColor(viewModel.secondsRemaining <= 5 ? palette.unmatchedBorder : palette.primaryInk)

                                        Button(action: {
                                            if viewModel.isTimerPaused {
                                                viewModel.resumeTimer()
                                            } else {
                                                viewModel.pauseTimer()
                                            }
                                        }) {
                                            Image(systemName: viewModel.isTimerPaused ? "play.fill" : "pause.fill")
                                                .font(.system(size: 11))
                                                .foregroundColor(palette.primaryInk)
                                                .padding(4)
                                                .background(palette.surface)
                                                .clipShape(Circle())
                                        }

                                        Button("Off") {
                                            viewModel.toggleTimer()
                                        }
                                        .font(CogniTypography.caption(size: 11))
                                        .foregroundColor(palette.secondaryInk)
                                        .padding(.horizontal, 6)
                                        .padding(.vertical, 2)
                                        .background(palette.surface)
                                        .clipShape(RoundedRectangle(cornerRadius: 6, style: .continuous))
                                    } else {
                                        Button(action: {
                                            viewModel.toggleTimer()
                                        }) {
                                            Text("Timer Off")
                                                .font(CogniTypography.caption(size: 12))
                                                .foregroundColor(palette.secondaryInk)
                                                .padding(.horizontal, 8)
                                                .padding(.vertical, 4)
                                                .background(palette.surface)
                                                .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
                                                .overlay(
                                                    RoundedRectangle(cornerRadius: 8, style: .continuous)
                                                        .stroke(palette.cardBorder, lineWidth: 1)
                                                )
                                        }
                                    }
                                }

                                Spacer()

                                // Right: Compact Music & Sound Toggles
                                HStack(spacing: 6) {
                                    // Music Toggle
                                    Button(action: {
                                        viewModel.toggleMusic()
                                    }) {
                                        HStack(spacing: 4) {
                                            Image(systemName: viewModel.isMusicEnabled ? "music.note" : "speaker.slash")
                                                .font(.system(size: 12))
                                            Text("Music")
                                                .font(CogniTypography.caption(size: 12))
                                        }
                                        .foregroundColor(viewModel.isMusicEnabled ? palette.primaryAction : palette.secondaryInk)
                                        .padding(.horizontal, 8)
                                        .padding(.vertical, 5)
                                        .background(viewModel.isMusicEnabled ? palette.selectedOptionBg : palette.surface)
                                        .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
                                        .overlay(
                                            RoundedRectangle(cornerRadius: 8, style: .continuous)
                                                .stroke(viewModel.isMusicEnabled ? palette.primaryAction : palette.cardBorder, lineWidth: 1)
                                        )
                                    }

                                    // Sound Toggle
                                    Button(action: {
                                        viewModel.toggleHapticsAndSound()
                                    }) {
                                        HStack(spacing: 4) {
                                            Image(systemName: viewModel.isHapticsAndSoundEnabled ? "speaker.wave.2.fill" : "speaker.slash.fill")
                                                .font(.system(size: 12))
                                            Text("Sound")
                                                .font(CogniTypography.caption(size: 12))
                                        }
                                        .foregroundColor(viewModel.isHapticsAndSoundEnabled ? palette.primaryAction : palette.secondaryInk)
                                        .padding(.horizontal, 8)
                                        .padding(.vertical, 5)
                                        .background(viewModel.isHapticsAndSoundEnabled ? palette.selectedOptionBg : palette.surface)
                                        .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
                                        .overlay(
                                            RoundedRectangle(cornerRadius: 8, style: .continuous)
                                                .stroke(viewModel.isHapticsAndSoundEnabled ? palette.primaryAction : palette.cardBorder, lineWidth: 1)
                                        )
                                    }
                                }
                            }
                            .padding(.top, 4)

                            // Balanced Clue or Answer Card
                            if let card = viewModel.currentCard {
                                ZStack {
                                    RoundedRectangle(cornerRadius: 18, style: .continuous)
                                        .fill(viewModel.isAnswerRevealed ? palette.revealCardSurface : palette.clueCardSurface)
                                        .overlay(
                                            RoundedRectangle(cornerRadius: 18, style: .continuous)
                                                .stroke(viewModel.isAnswerRevealed ? palette.revealCardBorder : palette.cardBorder, lineWidth: 1.5)
                                        )

                                    VStack(spacing: 12) {
                                        if !viewModel.isAnswerRevealed {
                                            Text(card.clue)
                                                .font(CogniTypography.headline(size: 22))
                                                .foregroundColor(palette.primaryInk)
                                                .multilineTextAlignment(.center)
                                                .lineSpacing(6)
                                                .padding(.horizontal, 20)
                                                .padding(.vertical, 24)
                                        } else {
                                            Text(card.emoji)
                                                .font(.system(size: 54))

                                            Text(card.correctAnswer)
                                                .font(CogniTypography.displayLarge(size: 26))
                                                .foregroundColor(palette.primaryInk)

                                            Text("Familiar & memorable")
                                                .font(CogniTypography.caption(size: 13))
                                                .foregroundColor(palette.secondaryInk)
                                        }
                                    }
                                    .padding(.vertical, 16)
                                }
                                .frame(maxWidth: .infinity)
                                .frame(minHeight: 160)
                            }

                            // Feedback State Banners
                            if viewModel.feedbackState == .matched {
                                HStack(spacing: 8) {
                                    Image(systemName: "checkmark.circle.fill")
                                        .foregroundColor(palette.selectedOptionText)
                                    Text("Wonderful! That's correct.")
                                        .font(CogniTypography.bodyBold(size: 15))
                                        .foregroundColor(palette.selectedOptionText)
                                }
                                .frame(maxWidth: .infinity)
                                .padding(12)
                                .background(palette.selectedOptionBg)
                                .clipShape(RoundedRectangle(cornerRadius: 12, style: .continuous))
                                .overlay(
                                    RoundedRectangle(cornerRadius: 12, style: .continuous)
                                        .stroke(palette.selectedOptionBorder, lineWidth: 1)
                                )
                            } else if viewModel.feedbackState == .tryAgain {
                                // Soft light-red feedback state (calm and non-punitive)
                                HStack(spacing: 8) {
                                    Image(systemName: "arrow.clockwise")
                                        .foregroundColor(palette.unmatchedText)
                                    Text("Not quite yet — try again, or tap Show Answer below.")
                                        .font(CogniTypography.body(size: 14))
                                        .foregroundColor(palette.unmatchedText)
                                }
                                .frame(maxWidth: .infinity)
                                .padding(12)
                                .background(palette.unmatchedBg)
                                .clipShape(RoundedRectangle(cornerRadius: 12, style: .continuous))
                                .overlay(
                                    RoundedRectangle(cornerRadius: 12, style: .continuous)
                                        .stroke(palette.unmatchedBorder, lineWidth: 1)
                                )
                            }

                            // Speech Status Message
                            if let status = viewModel.speechStatusMessage {
                                Text(status)
                                    .font(CogniTypography.caption(size: 13))
                                    .foregroundColor(palette.secondaryInk)
                                    .multilineTextAlignment(.center)
                                    .padding(.horizontal, 16)
                            }

                            // Answer Input Zone (Typing + Voice Microphone + Submit)
                            VStack(spacing: 10) {
                                // High-contrast text field
                                HStack(spacing: 8) {
                                    TextField("Type your answer here...", text: Binding(
                                        get: { viewModel.userAnswerInput },
                                        set: { viewModel.updateAnswerInput($0) }
                                    ))
                                    .focused($isInputFocused)
                                    .font(CogniTypography.bodyBold(size: 18))
                                    .foregroundColor(palette.primaryInk)
                                    .autocorrectionDisabled()
                                    .textInputAutocapitalization(.never)
                                    .onSubmit {
                                        viewModel.submitAnswer()
                                    }

                                    if !viewModel.userAnswerInput.isEmpty {
                                        Button(action: {
                                            viewModel.updateAnswerInput("")
                                        }) {
                                            Image(systemName: "xmark.circle.fill")
                                                .foregroundColor(palette.secondaryInk)
                                        }
                                    }
                                }
                                .padding(.horizontal, 14)
                                .frame(height: 56)
                                .background(palette.surface)
                                .clipShape(RoundedRectangle(cornerRadius: 14, style: .continuous))
                                .overlay(
                                    RoundedRectangle(cornerRadius: 14, style: .continuous)
                                        .stroke(palette.cardBorder, lineWidth: 1.5)
                                )

                                // Action Buttons Row: Speak Microphone + Submit
                                HStack(spacing: 10) {
                                    // Voice Microphone Button
                                    Button(action: {
                                        isInputFocused = false
                                        viewModel.toggleVoiceInput()
                                    }) {
                                        HStack(spacing: 8) {
                                            Image(systemName: viewModel.isListening ? "waveform.badge.mic" : "mic.fill")
                                                .font(.system(size: 18, weight: .bold))
                                            Text(viewModel.isListening ? "Listening..." : "Speak")
                                                .font(CogniTypography.button(size: 17))
                                        }
                                        .foregroundColor(viewModel.isListening ? palette.primaryActionText : palette.primaryInk)
                                        .frame(maxWidth: .infinity)
                                        .frame(height: 56)
                                        .background(viewModel.isListening ? palette.primaryAction : palette.surface)
                                        .clipShape(RoundedRectangle(cornerRadius: 14, style: .continuous))
                                        .overlay(
                                            RoundedRectangle(cornerRadius: 14, style: .continuous)
                                                .stroke(viewModel.isListening ? palette.primaryAction : palette.cardBorder, lineWidth: 1.5)
                                        )
                                    }

                                    // Submit Button
                                    Button(action: {
                                        isInputFocused = false
                                        viewModel.submitAnswer()
                                    }) {
                                        HStack(spacing: 6) {
                                            Image(systemName: "checkmark")
                                                .font(.system(size: 16, weight: .bold))
                                            Text("Submit")
                                                .font(CogniTypography.button(size: 17))
                                        }
                                        .foregroundColor(palette.primaryActionText)
                                        .frame(maxWidth: .infinity)
                                        .frame(height: 56)
                                        .background(palette.primaryAction)
                                        .clipShape(RoundedRectangle(cornerRadius: 14, style: .continuous))
                                    }
                                }
                            }
                            .padding(.top, 4)

                            Spacer(minLength: 16)
                        }
                        .padding(.horizontal, 16)
                        .frame(maxWidth: 680)
                    }

                    // Pinned Bottom Bar: "Show Answer" / "Next Card"
                    VStack(spacing: 0) {
                        Divider()
                            .background(palette.cardBorder.opacity(0.6))

                        Button(action: {
                            isInputFocused = false
                            if !viewModel.isAnswerRevealed {
                                viewModel.revealAnswer()
                            } else {
                                viewModel.nextCard()
                            }
                        }) {
                            HStack(spacing: 8) {
                                Image(systemName: !viewModel.isAnswerRevealed ? "eye.fill" : "arrow.right")
                                    .font(.system(size: 18, weight: .bold))
                                Text(!viewModel.isAnswerRevealed ? "Show Answer" : "Next Card")
                                    .font(CogniTypography.button(size: 19))
                            }
                            .foregroundColor(palette.primaryActionText)
                            .frame(maxWidth: .infinity)
                            .frame(height: 60)
                            .background(palette.primaryAction)
                            .clipShape(RoundedRectangle(cornerRadius: 16, style: .continuous))
                            .shadow(color: palette.primaryAction.opacity(0.3), radius: 6, x: 0, y: 3)
                        }
                        .padding(.horizontal, 16)
                        .padding(.vertical, 10)
                        .frame(maxWidth: 680)
                    }
                    .background(palette.background)
                }
            }
        }
        .sheet(isPresented: $isDeckPickerPresented) {
            DeckPickerDialog(viewModel: viewModel)
        }
    }
}
