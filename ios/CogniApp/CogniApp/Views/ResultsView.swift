import SwiftUI

/// Calm, respectful game results summary displayed when completing a deck.
/// Fully matches Android SessionCompleteView with:
/// - Completed deck title and card count
/// - Gentle outcome stats ("Recalled Directly", "Answer Viewed", "Skipped")
/// - Warm encouraging appreciation note acknowledging effort and focus
/// - Adult educational reinforcement
/// - Card-by-card recap with "Your answer" label and soft light-red styling on unmatched answers
/// - 3 large action buttons: "Play Again", "Choose Another Category", "Return to Home"
public struct ResultsView: View {
    @ObservedObject public var viewModel: PracticeViewModel
    @Binding public var isDeckPickerPresented: Bool

    private var palette: CogniThemePalette {
        viewModel.palette
    }

    public var body: some View {
        ScrollView {
            VStack(spacing: 16) {
                // Header Block
                VStack(spacing: 6) {
                    Text("Deck Completed! 🎉")
                        .font(CogniTypography.headline(size: 24))
                        .foregroundColor(palette.primaryInk)

                    Text("You finished all \(viewModel.totalCards) cards in \(viewModel.deckTitle)")
                        .font(CogniTypography.body(size: 15))
                        .foregroundColor(palette.secondaryInk)
                        .multilineTextAlignment(.center)
                }
                .padding(.top, 8)

                // Outcome Stat Chips
                HStack(spacing: 10) {
                    // Recalled Directly
                    VStack(spacing: 2) {
                        Text("\(viewModel.matchedCount)")
                            .font(CogniTypography.headline(size: 22))
                            .foregroundColor(palette.selectedOptionText)
                        Text("Recalled Directly")
                            .font(CogniTypography.caption(size: 11))
                            .foregroundColor(palette.selectedOptionText)
                            .lineLimit(1)
                    }
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 10)
                    .background(palette.selectedOptionBg)
                    .clipShape(RoundedRectangle(cornerRadius: 10, style: .continuous))
                    .overlay(
                        RoundedRectangle(cornerRadius: 10, style: .continuous)
                            .stroke(palette.selectedOptionBorder, lineWidth: 1.5)
                    )

                    // Answer Viewed
                    VStack(spacing: 2) {
                        Text("\(viewModel.revealedCount)")
                            .font(CogniTypography.headline(size: 22))
                            .foregroundColor(palette.primaryInk)
                        Text("Answer Viewed")
                            .font(CogniTypography.caption(size: 11))
                            .foregroundColor(palette.secondaryInk)
                            .lineLimit(1)
                    }
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 10)
                    .background(palette.revealCardSurface)
                    .clipShape(RoundedRectangle(cornerRadius: 10, style: .continuous))
                    .overlay(
                        RoundedRectangle(cornerRadius: 10, style: .continuous)
                            .stroke(palette.revealCardBorder, lineWidth: 1.5)
                    )

                    // Skipped (if any)
                    if viewModel.skippedCount > 0 {
                        VStack(spacing: 2) {
                            Text("\(viewModel.skippedCount)")
                                .font(CogniTypography.headline(size: 22))
                                .foregroundColor(palette.primaryInk)
                            Text("Skipped")
                                .font(CogniTypography.caption(size: 11))
                                .foregroundColor(palette.secondaryInk)
                                .lineLimit(1)
                        }
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 10)
                        .background(palette.badgeBg)
                        .clipShape(RoundedRectangle(cornerRadius: 10, style: .continuous))
                        .overlay(
                            RoundedRectangle(cornerRadius: 10, style: .continuous)
                                .stroke(palette.cardBorder, lineWidth: 1.5)
                        )
                    }
                }

                // Warm Encouraging Appreciation Note
                HStack {
                    Text("Thank you for practicing today! Your effort and focus in completing this deck are wonderful for keeping your memory active.")
                        .font(CogniTypography.body(size: 14))
                        .foregroundColor(palette.selectedOptionText)
                        .multilineTextAlignment(.center)
                        .padding(14)
                }
                .frame(maxWidth: .infinity)
                .background(palette.selectedOptionBg)
                .clipShape(RoundedRectangle(cornerRadius: 12, style: .continuous))
                .overlay(
                    RoundedRectangle(cornerRadius: 12, style: .continuous)
                        .stroke(palette.selectedOptionBorder, lineWidth: 1)
                )

                // Educational reinforcement
                Text("Viewing clues and recalling familiar items strengthens memory pathways and everyday focus.")
                    .font(CogniTypography.caption(size: 13))
                    .foregroundColor(palette.secondaryInk)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 8)

                // Card-by-Card Recap
                if !viewModel.sessionResults.isEmpty {
                    VStack(alignment: .leading, spacing: 10) {
                        Text("Card-by-Card Recap")
                            .font(CogniTypography.title(size: 16))
                            .foregroundColor(palette.primaryInk)
                            .padding(.top, 4)

                        ForEach(Array(viewModel.sessionResults.enumerated()), id: \.element.id) { index, result in
                            VStack(alignment: .leading, spacing: 6) {
                                HStack {
                                    Text(result.card.emoji)
                                        .font(.system(size: 20))
                                    Text("Card \(index + 1): \(result.correctAnswer)")
                                        .font(CogniTypography.bodyBold(size: 15))
                                        .foregroundColor(palette.primaryInk)
                                    Spacer()

                                    // Badge
                                    let badgeTuple: (text: String, bg: Color, border: Color, textColor: Color) = {
                                        switch result.outcome {
                                        case .matched:
                                            return ("Recalled", palette.selectedOptionBg, palette.selectedOptionBorder, palette.selectedOptionText)
                                        case .revealed:
                                            return ("Answer Viewed", palette.revealCardSurface, palette.revealCardBorder, palette.primaryInk)
                                        case .skipped:
                                            return ("Skipped", palette.badgeBg, palette.cardBorder, palette.secondaryInk)
                                        }
                                    }()

                                    Text(badgeTuple.text)
                                        .font(CogniTypography.caption(size: 11))
                                        .foregroundColor(badgeTuple.textColor)
                                        .padding(.horizontal, 8)
                                        .padding(.vertical, 3)
                                        .background(badgeTuple.bg)
                                        .clipShape(RoundedRectangle(cornerRadius: 6, style: .continuous))
                                        .overlay(
                                            RoundedRectangle(cornerRadius: 6, style: .continuous)
                                                .stroke(badgeTuple.border, lineWidth: 1)
                                        )
                                }

                                Text(result.card.clue)
                                    .font(CogniTypography.body(size: 13))
                                    .foregroundColor(palette.secondaryInk)

                                // User Answer Row with "Your answer" label
                                if !result.userAnswer.isEmpty {
                                    if result.outcome == .matched {
                                        Text("Your answer: \"\(result.userAnswer)\"")
                                            .font(CogniTypography.caption(size: 12))
                                            .foregroundColor(palette.selectedOptionText)
                                    } else {
                                        // Soft light-red styling for unmatched / incorrect answer
                                        HStack {
                                            Text("Your answer: \"\(result.userAnswer)\"")
                                                .font(CogniTypography.caption(size: 12))
                                                .foregroundColor(palette.unmatchedText)
                                        }
                                        .padding(.horizontal, 8)
                                        .padding(.vertical, 4)
                                        .background(palette.unmatchedBg)
                                        .clipShape(RoundedRectangle(cornerRadius: 6, style: .continuous))
                                        .overlay(
                                            RoundedRectangle(cornerRadius: 6, style: .continuous)
                                                .stroke(palette.unmatchedBorder, lineWidth: 1)
                                        )
                                    }
                                }
                            }
                            .padding(12)
                            .background(palette.surface)
                            .clipShape(RoundedRectangle(cornerRadius: 12, style: .continuous))
                            .overlay(
                                RoundedRectangle(cornerRadius: 12, style: .continuous)
                                    .stroke(palette.cardBorder, lineWidth: 1)
                            )
                        }
                    }
                }

                // 3 Primary Action Buttons
                VStack(spacing: 10) {
                    // Play Again
                    Button(action: {
                        viewModel.restartDeck()
                    }) {
                        HStack(spacing: 8) {
                            Image(systemName: "arrow.counterclockwise")
                            Text("Play Again")
                                .font(CogniTypography.button(size: 17))
                        }
                        .foregroundColor(palette.primaryActionText)
                        .frame(maxWidth: .infinity)
                        .frame(height: 56)
                        .background(palette.primaryAction)
                        .clipShape(RoundedRectangle(cornerRadius: 14, style: .continuous))
                    }

                    // Choose Another Category
                    Button(action: {
                        isDeckPickerPresented = true
                    }) {
                        HStack(spacing: 8) {
                            Image(systemName: "square.grid.2x2")
                            Text("Choose Another Category")
                                .font(CogniTypography.button(size: 17))
                        }
                        .foregroundColor(palette.primaryInk)
                        .frame(maxWidth: .infinity)
                        .frame(height: 56)
                        .background(palette.surface)
                        .clipShape(RoundedRectangle(cornerRadius: 14, style: .continuous))
                        .overlay(
                            RoundedRectangle(cornerRadius: 14, style: .continuous)
                                .stroke(palette.cardBorder, lineWidth: 1.5)
                        )
                    }

                    // Return to Home
                    Button(action: {
                        viewModel.returnToHome()
                    }) {
                        HStack(spacing: 8) {
                            Image(systemName: "house")
                            Text("Return to Home")
                                .font(CogniTypography.button(size: 17))
                        }
                        .foregroundColor(palette.secondaryInk)
                        .frame(maxWidth: .infinity)
                        .frame(height: 56)
                        .background(palette.surface)
                        .clipShape(RoundedRectangle(cornerRadius: 14, style: .continuous))
                        .overlay(
                            RoundedRectangle(cornerRadius: 14, style: .continuous)
                                .stroke(palette.cardBorder, lineWidth: 1.5)
                        )
                    }
                }
                .padding(.top, 8)
                .padding(.bottom, 24)
            }
            .padding(.horizontal, 16)
            .frame(maxWidth: 680)
        }
    }
}
