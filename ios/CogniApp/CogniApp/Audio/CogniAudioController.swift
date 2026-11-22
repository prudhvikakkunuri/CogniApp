import Foundation
import AVFoundation
import UIKit

/// Manages soothing ambient background music, gentle haptics, and feedback chimes.
/// Parallels Android CogniAudioController with 100% feature and behavioral parity:
/// - Bundled seamless adult harmonic pad loop (ambient_calm_loop.wav) at volume ~0.30
/// - Automatic pause during active speech recognition and automatic resume
/// - Background/foreground lifecycle integration
/// - Gentle tactile feedback and match chime
public final class CogniAudioController: ObservableObject {
    public static let shared = CogniAudioController()

    private var audioPlayer: AVAudioPlayer?
    private var isMusicRequested: Bool = false
    private var isPausedForSpeech: Bool = false

    private let impactFeedback = UIImpactFeedbackGenerator(style: .medium)
    private let notificationFeedback = UINotificationFeedbackGenerator()

    public init() {
        setupAudioSession()
        setupAudioPlayer()
        setupLifecycleObservers()
    }

    private func setupAudioSession() {
        do {
            try AVAudioSession.sharedInstance().setCategory(.playAndRecord, mode: .default, options: [.defaultToSpeaker, .allowBluetooth, .mixWithOthers])
            try AVAudioSession.sharedInstance().setActive(true)
        } catch {
            print("Failed to set audio session category: \(error.localizedDescription)")
        }
    }

    private func setupAudioPlayer() {
        guard let url = Bundle.main.url(forResource: "ambient_calm_loop", withExtension: "wav") else {
            print("ambient_calm_loop.wav not found in main bundle")
            return
        }

        do {
            audioPlayer = try AVAudioPlayer(contentsOf: url)
            audioPlayer?.numberOfLoops = -1 // Infinite seamless loop
            audioPlayer?.volume = 0.30      // Gentle, non-intrusive volume
            audioPlayer?.prepareToPlay()
        } catch {
            print("Failed to initialize AVAudioPlayer: \(error.localizedDescription)")
        }
    }

    private func setupLifecycleObservers() {
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleDidEnterBackground),
            name: UIApplication.didEnterBackgroundNotification,
            object: nil
        )
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleWillEnterForeground),
            name: UIApplication.willEnterForegroundNotification,
            object: nil
        )
    }

    @objc private func handleDidEnterBackground() {
        if audioPlayer?.isPlaying == true {
            audioPlayer?.pause()
        }
    }

    @objc private func handleWillEnterForeground() {
        if isMusicRequested && !isPausedForSpeech {
            audioPlayer?.play()
        }
    }

    // MARK: - Public Music Controls

    public func setMusicEnabled(_ enabled: Bool) {
        isMusicRequested = enabled
        if enabled {
            isPausedForSpeech = false
            if audioPlayer == nil {
                setupAudioPlayer()
            }
            audioPlayer?.play()
        } else {
            audioPlayer?.pause()
        }
    }

    public func pauseForSpeech() {
        if isMusicRequested && audioPlayer?.isPlaying == true {
            isPausedForSpeech = true
            audioPlayer?.pause()
        }
    }

    public func resumeAfterSpeech() {
        if isMusicRequested && isPausedForSpeech {
            isPausedForSpeech = false
            audioPlayer?.play()
        }
    }

    // MARK: - Haptic & Sound Effects

    public func playMatchSuccess(hapticsAndSoundEnabled: Bool) {
        guard hapticsAndSoundEnabled else { return }
        notificationFeedback.notificationOccurred(.success)
        AudioServicesPlaySystemSound(1057) // Gentle system chime
    }

    public func playSubtleTap(hapticsAndSoundEnabled: Bool) {
        guard hapticsAndSoundEnabled else { return }
        impactFeedback.impactOccurred()
    }

    public func playTryAgain(hapticsAndSoundEnabled: Bool) {
        guard hapticsAndSoundEnabled else { return }
        notificationFeedback.notificationOccurred(.warning)
    }

    deinit {
        NotificationCenter.default.removeObserver(self)
        audioPlayer?.stop()
    }
}
