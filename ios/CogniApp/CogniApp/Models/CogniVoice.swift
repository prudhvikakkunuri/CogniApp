import Foundation

/// Model representing an offline Text-to-Speech voice option on iOS & iPadOS.
public struct CogniVoice: Identifiable, Hashable, Codable {
    public let id: String
    public let name: String
    public let language: String
    public let isDefault: Bool

    public init(id: String, name: String, language: String, isDefault: Bool = false) {
        self.id = id
        self.name = name
        self.language = language
        self.isDefault = isDefault
    }
}
