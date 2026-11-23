# Workspace Memory: CogniApp

## Project Context
- **App Name:** CogniApp
- **Goal:** Calm, human-crafted cognitive practice workbook for Alzheimer's patients and caregivers.
- **Environment:** Built with Antigravity using Kotlin and Jetpack Compose.

## Key Guardrails for Implementation
- Keep interactions calm, unhurried, and errorless—never use harsh red error states or sudden forced auto-advances by default.
- Keep buttons large (`>= 58dp–60dp` height) with comfortable spacing (`10dp–12dp`).
- Ensure 100% offline execution with bundled thematic decks.
- Preserve state across configuration changes (such as screen rotation) and guard against accidental double-taps when advancing cards.
- Use respectful, direct copy without patronizing praise.
- Ensure viewport responsiveness: key action buttons are pinned via `Scaffold.bottomBar` so users never need to hunt or scroll.
- Zero text clipping and no mid-word wrapping on navigation controls and category labels.
- Support both typed and Speech-to-Text (STT) answer input with forgiving, normalized matching.
- Provide independent controls for soothing background music, gentle haptic/sound cues, and Light/Dark theme.
- Responsive layout across phones, tablets, and wide viewports.
- Intercept system Back button during gameplay (`BackHandler`) to return to Home rather than immediately exiting the app.
- Provide soft, non-neon light red highlights for unmatched/incorrect answers (`#FDECEC` / `#3A2224`) during gameplay and in the card-by-card recap.

## Build Milestones
- [x] **Milestone 1:** Project setup, theme tokens (`design.md`), and `FruitCard` + 10-card `FruitDeckRepository` (`architecture.md`).
- [x] **Milestone 2:** `PracticeViewModel` with option selection, answer reveal, next card, restart, and optional toggleable timer.
- [x] **Multi-Deck Library Expansion:**
  - Expanded data models: `PracticeCard` and `PracticeDeck`.
  - Bundled 5 initial offline familiar decks (Fruits, Vegetables, Animals, Everyday Things, Food & Drinks).
- [x] **Milestone 3:** `GameScreen` UI with large accessible buttons, deck selector, clue/answer card states, and session completion screen.
- [x] **Milestone 4:** Accessibility audit (TalkBack `contentDescription`, touch target sizes, double-tap guard, rotation state survival) and device testing.
- [x] **Launch Screen, Reading Time & In-App Logo Improvements:**
  - Created `HomeScreen.kt` with custom CogniApp vector emblem (`ic_cogni_logo.xml`), title, selectable category cards, optional timer setting, and prominent "Play" button.
- [x] **Mobile Responsiveness & Viewport-Fit Layout:**
  - Pinned full-width "Play" button in `HomeScreen` via `Scaffold.bottomBar`.
  - Pinned primary action button ("Show Answer" / "Next Card") in `GameScreen` via `Scaffold.bottomBar`.
- [x] **Anti-Wrapping, Unclipped Category Labels & In-Game Timer Controls:**
  - Enforced `maxLines = 1`, `softWrap = false` on top navigation buttons.
  - Added `deck.categoryLabel` with `maxLines = 2` preventing label clipping.
- [x] **Typed & Speech-to-Text (STT) Answer Input & 15-Second Timer Update:**
  - High-contrast text input box and dedicated voice input button (`>= 60dp`, "Speak" / "Listening...").
  - Normalized, forgiving matching and non-punitive feedback states (`MATCHED`, `TRY_AGAIN`).
  - Added `RECORD_AUDIO` permission and runtime speech recognition with `SpeechRecognizer`.
  - Changed timer countdown from 30s to 15s.
- [x] **Expanded Deck Library, Mixed Deck, Randomized Shuffling, Soothing Audio & Tablet Support:**
  - Added 4 new everyday decks: **Colors & Shapes**, **Clothes & Wearables**, **Nature & Weather**, and **Daily Routines & Time**, plus expanded variety in existing decks (totaling 86 cards).
  - Added **"Mixed All Decks"** (`All Decks Mix`) aggregating cards across all individual decks.
  - Shuffled card ordering on session start (`startPractice()`), deck switch (`selectDeck()`), and restart (`restartDeck()`), stored in `PracticeUiState.cards` so screen rotation retains current session order.
  - Added independent controls for **Background Music** and **Haptic & Feedback Sounds** on both `HomeScreen` and `GameScreen` (control strip).
  - Adaptive layout with `Modifier.widthIn(max = 680.dp)` content constraints and 2-to-3 column grids for phones and tablets.
- [x] **Calm Game Results Summary, Light/Dark Mode Toggle & Real Ambient Music Loop:**
  - Added calm, respectful `SessionCompleteView` with completed deck title, gentle outcome statistics ("Recalled Directly", "Answer Viewed", "Skipped"), adult cognitive reinforcement, card-by-card recap, and large `>= 60dp` action buttons ("Play Again", "Choose Another Category", "Return to Home").
  - Added Light / Dark mode toggle button in `HomeScreen` header with `>= 48dp` touch target, TalkBack accessibility descriptions, and a high-contrast dark palette (`CharcoalSlateCanvas` `#171A1F`, elevated slate cards `#22262E` with `#3A404B` borders, off-white ink `#F3F1EC`, and dark pine/sage accents).
  - Bundled 16-bit PCM ambient electric piano / harmonic pad chord loop (`ambient_calm_loop.wav` in `res/raw`) at gentle comfortable volume (`~0.30f`) with automatic pause during voice input and lifecycle management.
- [x] **System Back Navigation, Results "Your answer" Label, Appreciation Note & Light-Red Unmatched Highlights:**
  - Intercepted system Back button during gameplay (`BackHandler`) to return to Home screen instead of abruptly exiting app.
  - Renamed results screen label to `"Your answer"`.
  - Added warm, encouraging appreciation message on session completion thanking user for practicing and acknowledging effort and focus.
  - Added soft light-red styling for unmatched / incorrect answers in-game (`TRY_AGAIN`) and in results card recap (`#FDECEC` / `#3A2224` with `#C95B5B` / `#E07A7A` border).
- [x] **Visual & Layout Refresh (Quiz App Aesthetic Inspired):**
  - Updated color tokens in `Color.kt` and `Theme.kt`: warm bone canvas (`#EFEEE3`), crisp dark ink (`#070706`), slate secondary ink (`#4C5C5E`), muted slate (`#A3A39D`), warm amber (`#FAAF14`), sky blue (`#2F98E8`), deep teal (`#41798C`), and coral orange (`#E84A26`). Preserved soft light-red feedback tokens (`#FDECEC` / `#3A2224`) for unmatched answers.
  - Enhanced typography in `Type.kt` (`CogniTypography`) with friendly display presence and bold, readable weights.
  - Added smooth entrance fade animation to `HomeScreen`.
  - Refined card shapes, rounded corner radii (`14dp–18dp`), circular emoji badge containers, and clean active selection indicators.
  - Preserved 100% of functional requirements: launch splash animation, 10 decks, card shuffling on start/select/restart, 15s timer toggle, ambient music loop with voice pause/resume, haptic/sound toggle, typing + STT voice input, back handler returning to Home, "Your answer" label, warm appreciation note, soft light-red error highlights, and rotation survival.
  - Unit tests verified and debug APK assembled.

- [x] **Native iOS & iPadOS SwiftUI App & Automated .ipa Packaging:**
  - Created complete native iOS & iPadOS application in `ios/CogniApp/` using Swift & SwiftUI for iOS 16.0+ targeting iPhone and iPad.
  - **100% Feature & Data Parity:**
    - Ported all 10 offline decks (86 cards total) in `PracticeDeckRepository.swift` including the shuffled "Mixed All Decks" collection.
    - Launch splash animation fading smoothly into the Home screen.
    - Home screen with deck selection cards, Light / Dark mode toggle, Background Music toggle, Haptic & Sound toggle, optional 15s timer toggle, and pinned `>= 60pt` "Play" button fitting phone viewports without scrolling.
    - Gameplay screen with "Home" and "Decks" navigation, card counter, in-game control strip (timer badge, music toggle, sound toggle), workbook clue card, text input + Speech-to-Text (`SFSpeechRecognizer`) microphone button, forgiving matching, `Show Answer` / `Next Card` controls, and system/swipe back returning to Home.
    - Soft light-red highlight on wrong/unmatched answers, `"Your answer"` label on results, and warm appreciation message upon deck completion.
  - **Audio, Haptics & Permissions:**
    - Bundled `ambient_calm_loop.wav` played by `AVAudioPlayer` on an infinite loop at volume `~0.30`, automatically pausing during speech listening and on app backgrounding.
    - Integrated gentle tactile haptics (`UIImpactFeedbackGenerator`) and chime sound cues (`AudioServicesPlaySystemSound`).
    - Added `NSMicrophoneUsageDescription` and `NSSpeechRecognitionUsageDescription` to `Info.plist`.
  - **Project & .ipa Packaging Automation:**
    - Generated Xcode project (`CogniApp.xcodeproj/project.pbxproj`), XcodeGen configuration (`project.yml`), and `ExportOptions.plist`.
    - Created `ios/build_ipa.sh` script to archive and package `CogniApp.ipa`.
    - Created `.github/workflows/build-ios-ipa.yml` GitHub Actions workflow running on `macos-14` to build, package, and upload `CogniApp.ipa` as a downloadable artifact.
  - **Unit Testing:**
    - Added `CogniAppTests.swift` testing all 10 decks (86 cards), forgiving answer normalization, and 400ms debounce protection.

- [x] **Free Offline Read Aloud Voice Selection (Android & iOS):**
  - **Android:**
    - Implemented `CogniTtsController` querying offline English voices via `TextToSpeech.getVoices()` (`!voice.isNetworkConnectionRequired` and locale matching `en`).
    - Calm speech rate (`0.88f`) and gentle pitch (`0.98f`) with sample preview phrase: *"Hello, this is CogniApp. Practice memory with calm focus."*
    - Persistent selection via `SharedPreferencesVoiceStorage` and `PracticeViewModel.selectVoice()`.
    - Added clean Voice Picker row in `HomeScreen.kt` with a sample "Preview" button and accessible `VoicePickerDialog`.
    - Unit tests in `PracticeViewModelTest.kt` verifying voice state updates and storage restoration.
  - **iOS & iPadOS:**
    - Implemented `CogniTtsController.swift` querying offline English voices via `AVSpeechSynthesisVoice.speechVoices()`.
    - Calm speech rate (`0.45`), gentle pitch (`0.98`), and sample preview.
    - Persistent selection via `UserDefaults` and `PracticeViewModel.swift`.
    - Added clean Voice Picker in `HomeScreen.swift` with sample preview button and menu selector.
    - Unit tests in `CogniAppTests.swift` verifying voice query and persistence.

## How to Build & Sideload CogniApp on iOS / iPadOS
### Option A: Automated Build via GitHub Actions (Recommended from Windows)
1. Push the code or trigger the **Build CogniApp iOS & iPadOS (.ipa)** workflow from GitHub Actions.
2. Once the workflow completes, download the `CogniApp-iOS-IPA` artifact containing `CogniApp.ipa`.
3. Sideload onto iPhone or iPad using **AltStore**, **SideStore**, or **Sideloadly**:
   - Open AltStore / SideStore on your device or Sideloadly on your PC.
   - Select `CogniApp.ipa` and sign with your free Apple ID.
   - Trust your developer certificate under iOS *Settings > General > VPN & Device Management*.

### Option B: Local Build on macOS via Terminal / Shell
```bash
cd ios
chmod +x build_ipa.sh
./build_ipa.sh
# Output will be generated at ios/output/CogniApp.ipa
```

### Option C: Direct Run in Xcode on macOS
1. Open `ios/CogniApp/CogniApp.xcodeproj` in Xcode (or run `xcodegen generate` in `ios/CogniApp`).
2. Select your connected iPhone, iPad, or Simulator.
3. In *Signing & Capabilities*, select your Personal Team (free Apple ID).
4. Press **Cmd + R** to run.

## Current Progress & Verified State
- Android: Full application compiled and verified with passing unit tests (`testDebugUnitTest` - 50 passing tests) and debug APK build (`assembleDebug`).
- iOS: Complete native SwiftUI project, comprehensive unit & logic test suite (`CogniAppTests.swift`), build script (`build_ipa.sh`), and GitHub Actions workflow (`build-ios-ipa.yml` with CI simulator testing) established with 100% feature and visual parity.
- Resource Verification: `ambient_calm_loop.wav` confirmed present in `ios/CogniApp/CogniApp/Resources/ambient_calm_loop.wav` (1.41 MB) and mapped in target resources.
