import Foundation
import Speech
import AVFoundation

/// Manages Speech-to-Text (STT) voice recognition using Apple's SFSpeechRecognizer framework.
/// Implements live voice capturing, error handling, and auto-pause/resume of ambient background music.
public final class SpeechRecognizerManager: ObservableObject {
    public static let shared = SpeechRecognizerManager()

    private let speechRecognizer = SFSpeechRecognizer(locale: Locale(identifier: "en-US"))
    private var recognitionRequest: SFSpeechAudioBufferRecognitionRequest?
    private var recognitionTask: SFSpeechRecognitionTask?
    private let audioEngine = AVAudioEngine()

    @Published public var isListening: Bool = false
    @Published public var transcribedText: String = ""
    @Published public var errorMessage: String? = nil

    public init() {}

    public func requestAuthorization(completion: @escaping (Bool) -> Void) {
        SFSpeechRecognizer.requestAuthorization { authStatus in
            DispatchQueue.main.async {
                switch authStatus {
                case .authorized:
                    AVAudioSession.sharedInstance().requestRecordPermission { granted in
                        DispatchQueue.main.async {
                            completion(granted)
                        }
                    }
                default:
                    completion(false)
                }
            }
        }
    }

    public func startListening(
        onResult: @escaping (String) -> Void,
        onError: @escaping (String?) -> Void
    ) {
        // Cancel existing task if any
        stopListening()

        // Pause ambient background music
        CogniAudioController.shared.pauseForSpeech()

        let audioSession = AVAudioSession.sharedInstance()
        do {
            try audioSession.setCategory(.playAndRecord, mode: .measurement, options: [.defaultToSpeaker, .allowBluetooth])
            try audioSession.setActive(true, options: .notifyOthersOnDeactivation)
        } catch {
            onError("Audio session error: \(error.localizedDescription)")
            CogniAudioController.shared.resumeAfterSpeech()
            return
        }

        recognitionRequest = SFSpeechAudioBufferRecognitionRequest()
        guard let recognitionRequest = recognitionRequest else {
            onError("Unable to create speech recognition request")
            CogniAudioController.shared.resumeAfterSpeech()
            return
        }

        recognitionRequest.shouldReportPartialResults = true

        let inputNode = audioEngine.inputNode
        recognitionTask = speechRecognizer?.recognitionTask(with: recognitionRequest) { [weak self] result, error in
            guard let self = self else { return }

            if let result = result {
                let text = result.bestTranscription.formattedString
                DispatchQueue.main.async {
                    self.transcribedText = text
                    onResult(text)
                }
            }

            if error != nil || (result?.isFinal ?? false) {
                self.stopListening()
                if let error = error {
                    DispatchQueue.main.async {
                        onError(error.localizedDescription)
                    }
                }
            }
        }

        let recordingFormat = inputNode.outputFormat(forBus: 0)
        inputNode.removeTap(onBus: 0)
        inputNode.installTap(onBus: 0, bufferSize: 1024, format: recordingFormat) { buffer, _ in
            self.recognitionRequest?.append(buffer)
        }

        audioEngine.prepare()
        do {
            try audioEngine.start()
            DispatchQueue.main.async {
                self.isListening = true
                self.errorMessage = nil
            }
        } catch {
            onError("Could not start audio engine: \(error.localizedDescription)")
            stopListening()
        }
    }

    public func stopListening() {
        if audioEngine.isRunning {
            audioEngine.stop()
            audioEngine.inputNode.removeTap(onBus: 0)
            recognitionRequest?.endAudio()
        }
        recognitionTask?.cancel()
        recognitionTask = nil
        recognitionRequest = nil

        DispatchQueue.main.async {
            self.isListening = false
        }

        // Resume ambient background music if active
        CogniAudioController.shared.resumeAfterSpeech()
    }
}
