import Foundation

/// Result record for an individual card in a completed session.
public struct SessionCardResult: Identifiable, Hashable, Codable {
    public let id: UUID
    public let card: PracticeCard
    public let outcome: CardOutcome
    public let userAnswer: String
    public let correctAnswer: String

    public init(
        id: UUID = UUID(),
        card: PracticeCard,
        outcome: CardOutcome,
        userAnswer: String,
        correctAnswer: String
    ) {
        self.id = id
        self.card = card
        self.outcome = outcome
        self.userAnswer = userAnswer
        self.correctAnswer = correctAnswer
    }
}
