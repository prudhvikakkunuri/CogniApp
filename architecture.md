# Architecture Specification: CogniApp

## 1. Tech Stack
- **Platforms:**
  - **Android:** Kotlin + Jetpack Compose + Material 3 (Target SDK 34, Min SDK 24)
  - **iOS & iPadOS:** Swift 5.9 + SwiftUI (iOS 16.0+, Universal iPhone & iPad)
- **State Management:**
  - Android: Unidirectional Data Flow (`StateFlow` + `ViewModel`)
  - iOS: Unidirectional Data Flow (`ObservableObject` + `@Published` + `PracticeViewModel`)
- **Navigation & Back Handling:**
  - Android: `BackHandler` interception returning to Home screen
  - iOS: SwiftUI navigation hierarchy returning to Home screen
- **Speech Recognition:**
  - Android: `SpeechRecognizer` + `RecognizerIntent` with `RECORD_AUDIO` permission
  - iOS: Apple `SFSpeechRecognizer` + `AVAudioEngine` with `NSMicrophoneUsageDescription` & `NSSpeechRecognitionUsageDescription`
- **Audio & Haptics:**
  - Bundled seamless ambient loop (`ambient_calm_loop.wav`) at volume ~0.30, auto-pausing during voice input and on app backgrounding
  - Android: `MediaPlayer` + `Vibrator`
  - iOS: `AVAudioPlayer` + `UIImpactFeedbackGenerator` + `AudioServicesPlaySystemSound`
- **Theming:** Dual editorial themes (Light `WarmBoneCanvas` / `#EFEEE3` and Dark `CharcoalSlateCanvas` / `#171A1E`) with soft light-red highlights for unmatched answers
- **Data Source:** Bundled multi-deck local repository (`PracticeDeckRepository`) providing 10 decks (86 cards total) for 100% offline reliability
- **Packaging & CI/CD:**
  - Android: Gradle debug APK (`gradlew assembleDebug`)
  - iOS: `ios/build_ipa.sh` and GitHub Actions workflow (`.github/workflows/build-ios-ipa.yml`) for sideloadable `.ipa` packaging

## 2. Package Structure

### Android (`app/src/main/java/com/cogniapp/`)
- `model.PracticeCard` — Immutable card model (`id`, `deckId`, `category`, `difficulty`, `clue`, `options`, `correctAnswer`, `emoji`)
- `model.PracticeDeck` — Deck metadata and card collection (`id`, `title`, `categoryLabel`, `difficulty`, `emoji`, `cards`)
- `data.PracticeDeckRepository` — Offline repository providing 10 familiar practice decks
- `audio.CogniAudioController` — Soothing ambient music controller (`MediaPlayer`) and gentle tactile/sound feedback
- `viewmodel.PracticeViewModel` — Manages answer input, forgiving matching, voice recognition, card shuffling, dark mode, session result tracking, music/sound toggles, and 15s timer
- `ui.theme` — Light and dark editorial palettes, typography scale, unmatched light-red tokens
- `ui.screens.HomeScreen` — Launch, category picker, Light/Dark mode toggle, and preferences screen
- `ui.screens.GameScreen` — Practice screen with typing, voice input, BackHandler, in-game controls, and calm Results Summary

### iOS (`ios/CogniApp/CogniApp/`)
- `App/CogniAppApp.swift` — Application entry point, window scene, and dark mode configuration
- `Models/` — `PracticeCard.swift`, `PracticeDeck.swift`, `FeedbackState.swift`, `CardOutcome.swift`, `SessionCardResult.swift`
- `Data/PracticeDeckRepository.swift` — 10 offline decks (86 cards total) with exact card data parity
- `Audio/` — `CogniAudioController.swift` (`AVAudioPlayer` loop + haptics) and `SpeechRecognizerManager.swift` (`SFSpeechRecognizer`)
- `ViewModels/PracticeViewModel.swift` — Observable ViewModel with state, timer, debounce, and forgiving answer matching
- `Theme/` — `CogniColors.swift` (light/dark quiz tokens) and `CogniTypography.swift` (friendly rounded system fonts)
- `Views/` — `SplashView.swift`, `HomeScreen.swift`, `GameScreen.swift`, `DeckPickerDialog.swift`, `ResultsView.swift`
- `Resources/` — `ambient_calm_loop.wav`, `Info.plist`, `Assets.xcassets`
- `CogniAppTests/CogniAppTests.swift` — Unit tests for decks, answer matching, and debounce protection

## 3. UI State Model (`PracticeUiState`)
- `isSessionStarted: Boolean` (default `false`)
- `isDarkMode: Boolean` (default `false`)
- `currentDeck: PracticeDeck?` (defaults to Guess the Fruit)
- `availableDecks: List<PracticeDeck>` (10 offline decks)
- `currentIndex: Int` (`0 until cards.size`)
- `cards: List<PracticeCard>` (active deck cards, shuffled on session start/deck switch/restart)
- `userAnswerInput: String` (current typed or spoken text)
- `isListening: Boolean` (whether speech recognition is active)
- `speechStatusMessage: String?` (calm speech status or error message)
- `feedbackState: FeedbackState` (`IDLE`, `MATCHED`, `TRY_AGAIN`)
- `selectedOption: String?` (kept for backward compatibility)
- `isAnswerRevealed: Boolean`
- `timerEnabled: Boolean` (default `false`)
- `secondsRemaining: Int` (default `15` when timer is enabled)
- `isTimerPaused: Boolean`
- `isMusicEnabled: Boolean` (default `false`, alias: `backgroundMusicEnabled`)
- `isHapticsAndSoundEnabled: Boolean` (default `true`, alias: `hapticSoundEnabled`)
- `isSessionComplete: Boolean`
- `sessionResults: List<SessionCardResult>` (recorded card-by-card outcomes for the active session)
- `availableVoices: List<CogniVoice>` (queried offline English TTS voices)
- `selectedVoiceName: String?` (persistent voice selection identifier)

## 4. Text-to-Speech (TTS) Architecture
- `CogniVoice`: Immutable model representing an offline voice (`name`, `displayName`, `locale`, `isDefault`).
- `CogniTtsController`: Wraps `TextToSpeech` on Android (`AVSpeechSynthesizer` on iOS). Filters for offline English voices (`!voice.isNetworkConnectionRequired` and locale matching `en`), sets calm speech rate (`0.88f` on Android, `0.45` on iOS) and pitch (`0.98f`), and supports sample preview phrases.
- `VoicePreferenceStorage`: Interface for persisting selected voice preference across process restarts with `SharedPreferencesVoiceStorage` for Android runtime, `UserDefaults` on iOS, and `InMemoryVoiceStorage` for JVM unit tests.

## 5. Bundled Offline Decks (10 Decks Total)
1. **Fruits (`Guess the Fruit` — 12 cards):** Pear, Apple, Banana, Orange, Strawberry, Guava, Lemon, Grape, Kiwi, Watermelon, Mango, Peach
2. **Vegetables (`Guess the Vegetable` — 10 cards):** Carrot, Potato, Broccoli, Onion, Tomato, Spinach, Corn, Cucumber, Pumpkin, Peas
3. **Animals (`Guess the Animal` — 10 cards):** Dog, Cat, Elephant, Giraffe, Cow, Lion, Bird, Fish, Horse, Duck
4. **Everyday Things (`Everyday Things` — 10 cards):** Car, Bike, Bottle, Key, Spoon, Chair, Umbrella, Clock, Book, Cup
5. **Food & Drinks (`Food & Drinks` — 10 cards):** Noodles, Tea, Coffee, Rice, Bread, Milk, Soup, Egg, Juice, Pizza
6. **Colors & Shapes (`Colors & Shapes` — 8 cards):** Red, Blue, Green, Yellow, Circle, Square, Star, Triangle
7. **Clothes & Wearables (`Clothes & Wearables` — 8 cards):** Hat, Shirt, Shoes, Socks, Jacket, Watch, Glasses, Scarf
8. **Nature & Weather (`Nature & Weather` — 8 cards):** Sun, Rain, Snow, Rainbow, Tree, River, Cloud, Moon
9. **Daily Routines & Time (`Daily Routines` — 8 cards):** Morning, Bedtime, Breakfast, Bath, Walking, Reading, Cooking, Gardening
10. **Mixed All Decks (`Mixed All Decks` — 86 cards):** Aggregates all cards across all individual thematic decks
