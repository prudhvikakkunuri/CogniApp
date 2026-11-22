import Foundation

/// Outcome recorded for a card in the practice session.
public enum CardOutcome: String, Codable, Equatable {
    case matched
    case revealed
    case skipped
}
