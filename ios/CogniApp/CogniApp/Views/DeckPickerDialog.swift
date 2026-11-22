import SwiftUI

/// Accessible modal sheet displaying all 10 offline decks for quick switching during gameplay.
public struct DeckPickerDialog: View {
    @ObservedObject public var viewModel: PracticeViewModel
    @Environment(\.dismiss) private var dismiss

    private var palette: CogniThemePalette {
        viewModel.palette
    }

    public var body: some View {
        NavigationView {
            ZStack {
                palette.background
                    .ignoresSafeArea()

                ScrollView {
                    VStack(spacing: 10) {
                        ForEach(viewModel.availableDecks) { deck in
                            let isSelected = viewModel.currentDeck.id == deck.id
                            Button(action: {
                                viewModel.selectDeck(id: deck.id)
                                dismiss()
                            }) {
                                HStack(spacing: 12) {
                                    ZStack {
                                        Circle()
                                            .fill(isSelected ? palette.selectedOptionBg : palette.badgeBg)
                                            .frame(width: 44, height: 44)
                                        Text(deck.emoji)
                                            .font(.system(size: 22))
                                    }

                                    VStack(alignment: .leading, spacing: 2) {
                                        Text(deck.title)
                                            .font(CogniTypography.bodyBold(size: 16))
                                            .foregroundColor(palette.primaryInk)
                                        Text("\(deck.cardCount) cards")
                                            .font(CogniTypography.caption(size: 13))
                                            .foregroundColor(palette.secondaryInk)
                                    }

                                    Spacer()

                                    if isSelected {
                                        Image(systemName: "checkmark.circle.fill")
                                            .font(.system(size: 22))
                                            .foregroundColor(palette.primaryAction)
                                    }
                                }
                                .padding(12)
                                .background(isSelected ? palette.selectedOptionBg : palette.surface)
                                .clipShape(RoundedRectangle(cornerRadius: 14, style: .continuous))
                                .overlay(
                                    RoundedRectangle(cornerRadius: 14, style: .continuous)
                                        .stroke(isSelected ? palette.selectedOptionBorder : palette.cardBorder, lineWidth: 1.5)
                                )
                            }
                            .buttonStyle(.plain)
                        }
                    }
                    .padding(16)
                }
            }
            .navigationTitle("Choose a Deck")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button("Done") {
                        dismiss()
                    }
                    .foregroundColor(palette.primaryAction)
                    .font(CogniTypography.bodyBold(size: 16))
                }
            }
        }
    }
}
