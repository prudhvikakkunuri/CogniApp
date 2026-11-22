import Foundation

/// Immutable model representing a deck of practice cards.
public struct PracticeDeck: Identifiable, Hashable, Codable {
    public let id: String
    public let title: String
    public let categoryLabel: String
    public let difficulty: String
    public let emoji: String
    public let cards: [PracticeCard]

    public var cardCount: Int {
        cards.count
    }

    public init(
        id: String,
        title: String,
        categoryLabel: String? = nil,
        difficulty: String = "Easy",
        emoji: String,
        cards: [PracticeCard]
    ) {
        self.id = id
        self.title = title
        self.categoryLabel = categoryLabel ?? title
        self.difficulty = difficulty
        self.emoji = emoji
        self.cards = cards
    }
}
