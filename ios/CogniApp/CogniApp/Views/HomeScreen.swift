import SwiftUI

/// Responsive Launch / Home Screen for CogniApp on iOS & iPadOS.
/// Features warm bone canvas, rounded cards, clean slate typography, and pinned bottom Play button.
public struct HomeScreen: View {
    @ObservedObject public var viewModel: PracticeViewModel
    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    private var palette: CogniThemePalette {
        viewModel.palette
    }

    private var columns: [GridItem] {
        if horizontalSizeClass == .regular {
            // iPad: 3 columns
            return [
                GridItem(.flexible(), spacing: 12),
                GridItem(.flexible(), spacing: 12),
                GridItem(.flexible(), spacing: 12)
            ]
        } else {
            // iPhone: 2 columns
            return [
                GridItem(.flexible(), spacing: 10),
                GridItem(.flexible(), spacing: 10)
            ]
        }
    }

    public var body: some View {
        ZStack {
            palette.background
                .ignoresSafeArea()

            VStack(spacing: 0) {
                // Scrollable Content Zone
                ScrollView {
                    VStack(alignment: .leading, spacing: 14) {
                        // Compact Header
                        HStack(alignment: .center, spacing: 12) {
                            ZStack {
                                RoundedRectangle(cornerRadius: 14, style: .continuous)
                                    .fill(palette.surface)
                                    .frame(width: 46, height: 46)
                                    .overlay(
                                        RoundedRectangle(cornerRadius: 14, style: .continuous)
                                            .stroke(palette.cardBorder, lineWidth: 1.5)
                                    )
                                Text("📖")
                                    .font(.system(size: 24))
                            }

                            VStack(alignment: .leading, spacing: 2) {
                                Text("CogniApp")
                                    .font(CogniTypography.headline(size: 22))
                                    .foregroundColor(palette.primaryInk)

                                Text("Calm practice for everyday memory and focus")
                                    .font(CogniTypography.caption(size: 13))
                                    .foregroundColor(palette.secondaryInk)
                                    .lineLimit(1)
                            }

                            Spacer()

                            // Light / Dark Mode Toggle Button
                            Button(action: {
                                viewModel.toggleDarkMode()
                            }) {
                                Image(systemName: viewModel.isDarkMode ? "sun.max.fill" : "moon.stars.fill")
                                    .font(.system(size: 18, weight: .semibold))
                                    .foregroundColor(palette.primaryInk)
                                    .frame(width: 44, height: 44)
                                    .background(palette.surface)
                                    .clipShape(RoundedRectangle(cornerRadius: 12, style: .continuous))
                                    .overlay(
                                        RoundedRectangle(cornerRadius: 12, style: .continuous)
                                            .stroke(palette.cardBorder, lineWidth: 1.5)
                                    )
                            }
                            .accessibilityLabel(viewModel.isDarkMode ? "Switch to Light Mode" : "Switch to Dark Mode")
                        }
                        .padding(.top, 4)

                        // Section Title: Categories
                        Text("Choose a Category")
                            .font(CogniTypography.title(size: 18))
                            .foregroundColor(palette.primaryInk)
                            .padding(.top, 4)

                        // 10-Deck Grid
                        LazyVGrid(columns: columns, spacing: 10) {
                            ForEach(viewModel.availableDecks) { deck in
                                DeckCardView(
                                    deck: deck,
                                    isSelected: viewModel.currentDeck.id == deck.id,
                                    palette: palette
                                ) {
                                    viewModel.selectDeck(id: deck.id)
                                }
                            }
                        }

                        // Compact Preferences & Assist Card
                        VStack(spacing: 12) {
                            // 15-Second Timer Toggle
                            HStack {
                                VStack(alignment: .leading, spacing: 2) {
                                    HStack(spacing: 6) {
                                        Image(systemName: "timer")
                                            .foregroundColor(palette.primaryInk)
                                        Text("Optional 15-Second Timer")
                                            .font(CogniTypography.bodyBold(size: 15))
                                            .foregroundColor(palette.primaryInk)
                                    }
                                    Text("Defaults to off so you are never rushed.")
                                        .font(CogniTypography.caption(size: 12))
                                        .foregroundColor(palette.secondaryInk)
                                }
                                Spacer()
                                Toggle("", isOn: Binding(
                                    get: { viewModel.timerEnabled },
                                    set: { _ in viewModel.toggleTimer() }
                                ))
                                .labelsHidden()
                                .tint(palette.primaryAction)
                            }

                            Divider()
                                .background(palette.cardBorder)

                            // Background Music Toggle
                            HStack {
                                VStack(alignment: .leading, spacing: 2) {
                                    HStack(spacing: 6) {
                                        Image(systemName: viewModel.isMusicEnabled ? "music.note" : "speaker.slash")
                                            .foregroundColor(palette.primaryInk)
                                        Text("Soothing Background Music")
                                            .font(CogniTypography.bodyBold(size: 15))
                                            .foregroundColor(palette.primaryInk)
                                    }
                                    Text("Gentle ambient piano loop; pauses during speech.")
                                        .font(CogniTypography.caption(size: 12))
                                        .foregroundColor(palette.secondaryInk)
                                }
                                Spacer()
                                Toggle("", isOn: Binding(
                                    get: { viewModel.isMusicEnabled },
                                    set: { _ in viewModel.toggleMusic() }
                                ))
                                .labelsHidden()
                                .tint(palette.primaryAction)
                            }

                            Divider()
                                .background(palette.cardBorder)

                            // Haptics & Feedback Sounds Toggle
                            HStack {
                                VStack(alignment: .leading, spacing: 2) {
                                    HStack(spacing: 6) {
                                        Image(systemName: viewModel.isHapticsAndSoundEnabled ? "speaker.wave.2" : "speaker.slash")
                                            .foregroundColor(palette.primaryInk)
                                        Text("Haptics & Feedback Sounds")
                                            .font(CogniTypography.bodyBold(size: 15))
                                            .foregroundColor(palette.primaryInk)
                                    }
                                    Text("Gentle chimes and pleasant tactile cues.")
                                        .font(CogniTypography.caption(size: 12))
                                        .foregroundColor(palette.secondaryInk)
                                }
                                Spacer()
                                Toggle("", isOn: Binding(
                                    get: { viewModel.isHapticsAndSoundEnabled },
                                    set: { _ in viewModel.toggleHapticsAndSound() }
                                ))
                                .labelsHidden()
                                .tint(palette.primaryAction)
                            }
                        }
                        .padding(14)
                        .background(palette.surface)
                        .clipShape(RoundedRectangle(cornerRadius: 16, style: .continuous))
                        .overlay(
                            RoundedRectangle(cornerRadius: 16, style: .continuous)
                                .stroke(palette.cardBorder, lineWidth: 1.5)
                        )
                        .padding(.top, 4)

                        Spacer(minLength: 16)
                    }
                    .padding(.horizontal, 16)
                    .frame(maxWidth: 680)
                }

                // Pinned Bottom Bar: Prominent Full-Width "Play" Button
                VStack(spacing: 0) {
                    Divider()
                        .background(palette.cardBorder.opacity(0.6))

                    Button(action: {
                        viewModel.startPractice()
                    }) {
                        HStack(spacing: 8) {
                            Image(systemName: "play.fill")
                                .font(.system(size: 20, weight: .bold))
                            Text("Play")
                                .font(CogniTypography.button(size: 20))
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
}

/// Category Card View for HomeScreen deck picker.
struct DeckCardView: View {
    let deck: PracticeDeck
    let isSelected: Bool
    let palette: CogniThemePalette
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            HStack(spacing: 10) {
                // Circular Emoji Badge Container
                ZStack {
                    Circle()
                        .fill(isSelected ? palette.selectedOptionBg : palette.badgeBg)
                        .frame(width: 44, height: 44)
                        .overlay(
                            Circle()
                                .stroke(isSelected ? palette.selectedOptionBorder : palette.badgeBorder, lineWidth: 1)
                        )
                    Text(deck.emoji)
                        .font(.system(size: 22))
                }

                VStack(alignment: .leading, spacing: 2) {
                    Text(deck.categoryLabel)
                        .font(CogniTypography.bodyBold(size: 15))
                        .foregroundColor(palette.primaryInk)
                        .lineLimit(2)
                        .fixedSize(horizontal: false, vertical: true)

                    Text("\(deck.cardCount) cards")
                        .font(CogniTypography.caption(size: 12))
                        .foregroundColor(palette.secondaryInk)
                }

                Spacer(minLength: 4)

                if isSelected {
                    Image(systemName: "checkmark.circle.fill")
                        .font(.system(size: 20))
                        .foregroundColor(palette.primaryAction)
                }
            }
            .padding(.horizontal, 12)
            .padding(.vertical, 10)
            .frame(maxWidth: .infinity, alignment: .leading)
            .frame(minHeight: 64)
            .background(isSelected ? palette.selectedOptionBg : palette.surface)
            .clipShape(RoundedRectangle(cornerRadius: 14, style: .continuous))
            .overlay(
                RoundedRectangle(cornerRadius: 14, style: .continuous)
                    .stroke(isSelected ? palette.selectedOptionBorder : palette.cardBorder, lineWidth: isSelected ? 2 : 1.5)
            )
        }
        .buttonStyle(.plain)
        .accessibilityLabel("\(deck.categoryLabel), \(deck.cardCount) cards\(isSelected ? ", selected" : "")")
    }
}
