# Product Requirements Document (PRD): CogniApp

## 1. Overview & Purpose
**CogniApp** is a calm, human-crafted Android cognitive practice workbook designed for older adults living with Alzheimer’s disease and their caregivers. It replaces passive, rushed countdown videos with an unhurried, accessible practice experience supporting typed and speech-to-text (STT) voice answer input, an adult soothing ambient music loop, a calm Light/Dark mode, and a respectful end-of-deck results summary across 10 familiar thematic and mixed decks.

## 2. Target Users
- **Primary User (Person Living with Alzheimer’s):** Benefits from clear clues, large high-contrast touch targets, zero-pressure pacing, natural voice input, randomized card order, soothing background audio, calm dark or light mode, and warm, non-punitive feedback.
- **Secondary User (Caregiver / Family Member):** Helps launch the app, choose a comfortable category, toggle optional 15-second timer support, switch between light and dark themes, adjust ambient audio and gentle haptics, and review the calm end-of-deck results summary.

## 3. Core Functional Requirements
1. **Dedicated Launch / Home Screen:**
   - App opens on a welcoming launch screen with the CogniApp emblem, title, calm introduction, and accessible **Light / Dark mode toggle** (`>= 48dp` touch target).
   - Large selectable category cards for 10 familiar offline decks (Fruits, Vegetables, Animals, Everyday Things, Food & Drinks, Colors & Shapes, Clothes & Wearables, Nature & Weather, Daily Routines & Time, and Mixed All Decks).
   - Independent toggles for:
     - Optional 15-second timer (defaulting to OFF).
     - Soothing background music (defaulting to OFF).
     - Haptic & feedback sound cues (defaulting to ON).
   - Prominent `>= 60dp` "Play" button pinned to the bottom.
2. **Accessible Answer Input (Typing & Speech-to-Text):**
   - High-contrast text input box (`>= 58dp` height, `20sp` text) for effortless typing.
   - Large (`>= 60dp`) dedicated **Microphone** ("Speak" / "Listening...") button for natural voice input.
   - Forgiving, normalized matching: case-insensitive, trimmed, ignores leading articles ("a"/"an"/"the"), and tolerates minor singular/plural variations and conversational spoken phrases.
   - Accidental double-tap / tremor debounce protection (400ms guard).
3. **Calm Game Results Summary (`SessionCompleteView`):**
   - When finishing all cards in a deck, displays a calm, respectful summary showing the completed deck title and total cards completed.
   - Gentle count of matched vs. revealed/skipped cards using calm, non-punitive wording ("Recalled Directly", "Answer Viewed", "Skipped" if any)—no harsh red error states or patronizing cheerleader text.
   - Clean scrollable card-by-card recap of clues, answers, and what was typed or spoken.
   - Primary action buttons (**"Play Again"**, **"Choose Another Category"**, **"Return to Home"**)—all `>= 60dp` tall, non-wrapping, and fitting within the visible viewport across phones and tablets.
4. **Light & Dark Mode Support:**
   - Clear, compact Light / Dark mode toggle button in the header of `HomeScreen`.
   - Calm, high-contrast dark theme: soft charcoal-slate canvas `#171A1F`, elevated slate cards `#22262E` with `#3A404B` borders, crisp off-white ink `#F3F1EC`, and calm sage/pine accents without harsh red error states.
   - Seamless palette synchronization across `HomeScreen`, `GameScreen`, and the results summary.
5. **Real, Soothing Ambient Background Music:**
   - Bundled seamless, warm ambient harmonic pad / soft electric piano chord progression (`Fmaj7` -> `Em7` -> `Dm7` -> `Cmaj7` in a warm lower-mid register) in `app/src/main/res/raw/ambient_calm_loop.wav`.
   - Gentle default volume (`~0.30f`), looping smoothly without clicks or pops.
   - Automatic pause during active voice listening (`isListening == true`) and automatic resume.
   - Full Android lifecycle integration (`onPause`, `onResume`, `onDestroy`).
6. **Randomized Card Order:**
   - Deck cards are automatically shuffled on session start, deck switch, and deck restart.
   - The active shuffled order is stored in `PracticeUiState.cards` so screen rotation retains current progress without reshuffling mid-session.
7. **Responsive Phone & Tablet Layout:**
   - Adapts cleanly from compact phones to larger tablets with `Modifier.widthIn(max = 680.dp)` content constraints, 2-to-3 column responsive grids, and pinned action bars (`Scaffold` + `bottomBar` + `navigationBarsPadding()`).
8. **Offline-First Library (86 Cards Total Across 10 Decks):**
   - 100% offline execution with zero network dependency.

## 4. Non-Goals (v1)
- No medical diagnosis, clinical assessment, or cognitive scoring claims.
- No complex account walls, social feeds, or distracting ads during play.
