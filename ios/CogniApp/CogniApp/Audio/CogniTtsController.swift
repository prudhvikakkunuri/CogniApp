import Foundation
import AVFoundation

/// Offline Text-to-Speech controller for CogniApp on iOS & iPadOS.
/// Provides offline English voice querying, calm speech rate (~0.45), gentle pitch (0.98),
/// and sample phrase preview.
public final class CogniTtsController: NSObject, AVSpeechSynthesizerDelegate {
    public static let shared = CogniTtsController()

    public static let previewPhrase = "Hello, this is CogniApp. Practice memory with calm focus."
    public static let calmRate: Float = 0.45
    public static let calmPitch: Float = 0.98

    private let synthesizer = AVSpeechSynthesizer()

    public override init() {
        super.init()
        synthesizer.delegate = self
    }

    /// Queries installed offline English voices formatted with clean display names.
    public func queryAvailableVoices() -> [CogniVoice] {
        let allVoices = AVSpeechSynthesisVoice.speechVoices()
        let defaultVoice = AVSpeechSynthesisVoice(language: "en-US")
        let defaultId = defaultVoice?.identifier

        let englishVoices = allVoices.filter { voice in
            voice.language.lowercased().hasPrefix("en")
        }

        // Deduplicate and format cleanly
        var result: [CogniVoice] = []
        var seenIds = Set<String>()

        for voice in englishVoices {
            let localeLabel: String
            if voice.language.contains("-US") {
                localeLabel = "English (US)"
            } else if voice.language.contains("-GB") {
                localeLabel = "English (UK)"
            } else if voice.language.contains("-AU") {
                localeLabel = "English (AU)"
            } else if voice.language.contains("-IN") {
                localeLabel = "English (IN)"
            } else if voice.language.contains("-CA") {
                localeLabel = "English (CA)"
            } else {
                localeLabel = "English (\(voice.language))"
            }

            let cleanName = "\(voice.name) (\(localeLabel))"

            if !seenIds.contains(voice.identifier) {
                seenIds.insert(voice.identifier)
                result.append(CogniVoice(
                    id: voice.identifier,
                    name: cleanName,
                    language: localeLabel,
                    isDefault: voice.identifier == defaultId
                ))
            }
        }

        // Ensure at least system default exists if empty
        if result.isEmpty {
            result.append(CogniVoice(
                id: defaultId ?? "en-US",
                name: "System Default (English)",
                language: "English",
                isDefault: true
            ))
        }

        return result
    }

    /// Speaks the given text using the specified voice identifier with calm pacing.
    public func speak(text: String, voiceIdentifier: String? = nil) {
        stop()

        let utterance = AVSpeechUtterance(string: text)
        utterance.rate = Self.calmRate
        utterance.pitchMultiplier = Self.calmPitch

        if let voiceId = voiceIdentifier, let voice = AVSpeechSynthesisVoice(identifier: voiceId) {
            utterance.voice = voice
        } else {
            utterance.voice = AVSpeechSynthesisVoice(language: "en-US")
        }

        synthesizer.speak(utterance)
    }

    /// Previews the calm sample phrase with the specified voice.
    public func previewVoice(identifier: String?) {
        speak(text: Self.previewPhrase, voiceIdentifier: identifier)
    }

    /// Stops any active speech.
    public func stop() {
        if synthesizer.isSpeaking {
            synthesizer.stopSpeaking(at: .immediate)
        }
    }
}
