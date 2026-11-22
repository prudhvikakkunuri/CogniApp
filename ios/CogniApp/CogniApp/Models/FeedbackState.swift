import Foundation

/// Non-punitive feedback state after submitting an answer.
public enum FeedbackState: String, Codable, Equatable {
    case idle
    case matched
    case tryAgain
}
