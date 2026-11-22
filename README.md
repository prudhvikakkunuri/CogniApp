# CogniApp

**CogniApp** is a calm, human-crafted, offline-first cognitive practice workbook application built natively for both **Android** (Kotlin + Jetpack Compose) and **iOS & iPadOS** (Swift + SwiftUI). Designed specifically for people living with Alzheimer’s disease and their caregivers, it replaces rushed countdown videos with unhurried, accessible practice supporting typed and Speech-to-Text (STT) voice answer input, a soothing ambient electric piano loop, a calm Light/Dark mode, and a respectful end-of-deck results summary across 10 familiar offline decks.

---

## Key Features & UX Design

- **10 Familiar Offline Decks (86 Cards Total):**
  - **Fruits** (`Guess the Fruit` — 12 cards)
  - **Vegetables** (`Guess the Vegetable` — 10 cards)
  - **Animals** (`Guess the Animal` — 10 cards)
  - **Everyday Things** (`Daily Life Objects` — 10 cards)
  - **Food & Drinks** (`Food & Drinks` — 10 cards)
  - **Colors & Shapes** (`Colors & Shapes` — 8 cards)
  - **Clothes & Wearables** (`Clothes & Wearables` — 8 cards)
  - **Nature & Weather** (`Nature & Weather` — 8 cards)
  - **Daily Routines & Time** (`Daily Routines` — 8 cards)
  - **Mixed All Decks** (`All Decks Mix` — 86 cards aggregating all categories with randomized shuffling)
- **Accessible Typing & Speech-to-Text (Voice) Input:**
  - High-contrast text input field with single-tap clearing.
  - Dedicated large Microphone button ("Speak" / "Listening...") powered by native platform speech recognition (`SpeechRecognizer` on Android, `SFSpeechRecognizer` on iOS).
  - Forgiving, normalized answer matching: case-insensitive, trimmed, ignores leading articles ("a"/"an"/"the"), and tolerates minor singular/plural variations and conversational spoken phrases.
- **Calm, High-Contrast Editorial Themes:**
  - **Light Mode:** Warm bone paper canvas (`#EFEEE3`), crisp dark ink (`#070706`), balanced slate secondary text (`#4C5C5E`), and friendly sky blue/amber accents.
  - **Dark Mode:** Deep charcoal-slate canvas (`#171A1E`), elevated slate cards (`#222830`), and soft off-white ink (`#F3F4F6`).
  - Accessible Light/Dark toggle in the Home header with synchronized system window/status bars.
- **Soothing Ambient Audio & Cues:**
  - Bundled seamless 16-bit PCM ambient electric piano / harmonic pad chord loop (`ambient_calm_loop.wav`) at gentle volume (`~0.30f`).
  - Automatically pauses when Speech-to-Text begins listening and resumes upon completion.
  - Independent toggles for Background Music and Haptic / Feedback Sound cues.
- **Non-Punitive Feedback & Calm Results Summary:**
  - Soft light-red styling on wrong/unmatched answers (`#FDECEC` light / `#3A2224` dark) avoiding anxiety or harsh alarms.
  - Gentle outcome stats ("Recalled Directly", "Answer Viewed", "Skipped").
  - Warm appreciation message acknowledging effort and focus.
  - Card-by-card recap with `"Your answer"` and outcome badges.
- **Viewport-Fit Responsive Layout:**
  - Pinned bottom action bars (`Scaffold.bottomBar` / `VStack`) ensuring primary buttons ("Play", "Show Answer", "Next Card") are always accessible without scrolling.
  - Constrained max-width layout (`680dp` / `680pt`) adapting gracefully from compact phones to large tablets.
  - 400ms rapid-click / tremor debounce protection on card navigation.

---

## Project Structure

```
CogniApp/
├── app/                                        # Native Android Application (Kotlin + Jetpack Compose)
│   ├── src/main/java/com/cogniapp/
│   │   ├── audio/CogniAudioController.kt       # Ambient audio loop & haptics controller
│   │   ├── data/PracticeDeckRepository.kt      # 10 offline decks (86 cards total)
│   │   ├── model/PracticeCard.kt & PracticeDeck.kt
│   │   ├── ui/screens/HomeScreen.kt & GameScreen.kt
│   │   ├── ui/theme/Color.kt, Type.kt, Theme.kt
│   │   └── viewmodel/PracticeViewModel.kt & PracticeUiState.kt
│   └── src/test/java/com/cogniapp/             # Unit tests (50 passing tests)
├── ios/                                        # Native iOS & iPadOS Application (Swift + SwiftUI)
│   ├── build_ipa.sh                            # Script to archive & package CogniApp.ipa
│   └── CogniApp/
│       ├── project.yml                         # XcodeGen declarative project configuration
│       ├── ExportOptions.plist                 # Ad-hoc / development IPA export options
│       ├── CogniApp.xcodeproj/                 # Native Xcode project (iOS 16.0+)
│       ├── CogniAppTests/CogniAppTests.swift   # Swift unit tests (10 decks, matching, debounce)
│       └── CogniApp/
│           ├── App/CogniAppApp.swift
│           ├── Audio/CogniAudioController.swift & SpeechRecognizerManager.swift
│           ├── Data/PracticeDeckRepository.swift
│           ├── Models/PracticeCard.swift & PracticeDeck.swift
│           ├── Theme/CogniColors.swift & CogniTypography.swift
│           └── Views/HomeScreen.swift, GameScreen.swift, ResultsView.swift, SplashView.swift
├── .github/workflows/
│   └── build-ios-ipa.yml                       # CI/CD workflow testing & packaging CogniApp.ipa
├── architecture.md                             # Technical architecture specification
├── design.md                                   # Visual styling & theme tokens
├── memory.md                                   # Milestones & build/sideloading instructions
└── prd.md                                      # Product requirements document
```

---

## Build, Test & Run

### Android
```powershell
# Run unit tests
.\gradlew.bat testDebugUnitTest

# Build debug APK
.\gradlew.bat assembleDebug
# Output: app\build\outputs\apk\debug\app-debug.apk
```

### iOS & iPadOS
- **Automated CI/CD:** Trigger the GitHub Actions workflow [`.github/workflows/build-ios-ipa.yml`](.github/workflows/build-ios-ipa.yml) to run tests on macOS and download the packaged `CogniApp.ipa` artifact for sideloading via AltStore, SideStore, or Sideloadly.
- **Local macOS Build:**
  ```bash
  cd ios
  chmod +x build_ipa.sh
  ./build_ipa.sh --test
  # Output: ios/output/CogniApp.ipa
  ```
- **Xcode:** Open `ios/CogniApp/CogniApp.xcodeproj` in Xcode, select your connected device or simulator, and press **Cmd + R**.

