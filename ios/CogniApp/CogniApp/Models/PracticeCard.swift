import Foundation

/// Immutable model representing a single cognitive practice card.
public struct PracticeCard: Identifiable, Hashable, Codable {
    public let id: Int
    public let deckId: String
    public let category: String
    public let difficulty: String
    public let clue: String
    public let options: [String]
    public let correctAnswer: String
    public let emoji: String

    public init(
        id: Int,
        deckId: String,
        category: String,
        difficulty: String = "Easy",
        clue: String,
        options: [String],
        correctAnswer: String,
        emoji: String
    ) {
        self.id = id
        self.deckId = deckId
        self.category = category
        self.difficulty = difficulty
        self.clue = clue
        self.options = options
        self.correctAnswer = correctAnswer
        self.emoji = emoji
    }
}
