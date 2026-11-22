# Design Specification: CogniApp

## 1. Visual Tone & Editorial Palettes (Light & Dark)
CogniApp is designed as a calm, human-crafted cognitive practice workbook tailored for older adults living with Alzheimer’s and their caregivers, elevated by a friendly, modern quiz-app aesthetic:

### Light Quiz & Editorial Palette
- **Background / Canvas (`WarmBoneCanvas` / `WarmPaper`):** `#EFEEE3` — warm, gentle bone/cream paper canvas inspired by approachable modern quiz apps, eliminating harsh display glare while providing a tactile reading surface.
- **Card Surface (`CardWhite`):** `#FFFFFF` with a crisp `1.5dp` outline `#D6D1C7` and friendly rounded corners (`14dp–18dp`) for an approachable, tactile card feel.
- **Primary Ink (`InkPrimary`):** `#070706` — crisp, ultra-readable high-contrast dark ink for effortless legibility.
- **Secondary Ink (`InkSecondary`):** `#4C5C5E` — balanced slate ink for counters, badges, and metadata.
- **Muted Slate Ink (`InkMuted`):** `#A3A39D` — soft tertiary slate for gentle guidance text.
- **Primary Action & Accents (`DeepPine`, `SkyBlue`, `DeepSlateTeal`):** `#1F5A46` Deep Pine primary brand with friendly quiz accents: `#2F98E8` Sky Blue for active selections and action pills, `#FAAF14` Warm Amber for highlights, `#41798C` Deep Slate Teal for secondary tags, and `#E84A26` Coral Orange for focal touches.
- **Gentle Reveal Surface (`SageStoneReveal`):** Soft sage/stone `#EFF3F0` with outline `#8FA69C` and dark ink `#070706`.
- **Soft Light-Red Unmatched Feedback (`LightRedSoftBg`):** `#FDECEC` with outline `#C95B5B` and `#A33838` ink—a gentle, non-neon light red that clearly identifies incorrect/unmatched answers without causing anxiety or punitive stress.

### Dark Quiz & Editorial Palette
- **Background / Canvas (`CharcoalSlateCanvas`):** `#171A1E` — deep, soft charcoal-slate canvas that prevents eye strain in low-light environments without pitch-black AMOLED harshness.
- **Elevated Card Surface (`SlateCardElevated`):** `#22262E` with a crisp `1.5dp` outline `#3A404B` and friendly `14dp–18dp` corner radii.
- **Primary Ink (`DarkInkPrimary`):** `#F3F1EC` — crisp, warm off-white for effortless legibility.
- **Secondary Ink (`DarkInkSecondary`):** `#A0A6B2` — soft silver-gray for secondary metadata.
- **Primary Action & Selection Accent (`DarkPineAction` / `DarkSkyAction`):** `#2E7D62` / `#2F98E8` with `#FFFFFF` text, and soft selection tint `#1E352B` with `#3D997A` border and `#8CD4B9` text.
- **Gentle Reveal Surface (`DarkSageReveal`):** Soft muted sage `#202D27` with outline `#446355` and off-white ink `#F3F1EC`.
- **Soft Rose / Light-Red Unmatched Feedback (`DarkRoseSoftBg`):** `#3A2224` with outline `#E07A7A` and `#F5A3A3` ink for calm, clear visual differentiation of unmatched answers in dark mode.

- **Typography & Display Presence (`CogniTypography`):**
  - Friendly display headings: `headlineMedium` (24sp/36sp bold), `titleLarge` (22sp/30sp extra bold), `titleMedium` (18sp/26sp bold).
  - Clear, readable labels and body: `labelLarge` (14sp/20sp semi-bold), `bodyLarge` (16sp/24sp normal).
- **Custom Branding & Icons:** Custom open workbook + gentle leaf-and-spark emblem (`ic_cogni_logo.xml` and adaptive launcher icon `@mipmap/ic_launcher`).

## 2. Responsive Viewport-Fit Layout & Spacing
- **Adaptive Layout for Phones and Tablets:**
  - Content bounded by `widthIn(max = 680.dp)` centered horizontally so content is never stretched or congested on wide viewports or tablets.
  - Multi-column deck grid: 2 columns on standard phones, 3 columns on wide screens/tablets.
- **Scaffold with Pinned Action Bars & Navigation Handling:**
  - `HomeScreen`: Pinned `bottomBar` (`navigationBarsPadding()`) containing the full-width **"Play"** button, always visible without scrolling.
  - `GameScreen`: Pinned `bottomBar` (`navigationBarsPadding()`) containing the primary action button (`Show Answer` / `Next Card`).
  - **System Back Navigation Guard:** `BackHandler(enabled = isSessionStarted)` intercepts hardware/system back during gameplay or results, navigating back to Home (`returnToHome()`) rather than exiting the application. Normal Android exit is only allowed on the launch Home screen.
- **Screen Margins:** Consistent `16dp` horizontal padding prevents edge clipping on all phone aspect ratios.
- **Answer Input Zone & Touch Targets:**
  - High-contrast text input box: `>= 58dp` height, `20sp` text, single line with clear button.
  - Large **Microphone** ("Speak" / "Listening...") button: `>= 60dp` height, `#1F5A46` / `#2E7D62` accent, clear TalkBack content description.
  - Large **Submit** button: `>= 60dp` height, solid primary container, `#FFFFFF` text.
  - Vertical spacing: `10dp–12dp` between controls.
- **Zero Text Clipping & Anti-Wrapping:**
  - All navigation buttons ("Home", "Decks") enforce `maxLines = 1` and `softWrap = false` with adequate padding, preventing mid-word breaks like `"Deck\ns"`.
  - Category cards display full concise labels (`deck.categoryLabel`) with `maxLines = 2` and `softWrap = true`.

## 3. Screen Architecture
1. **Launch / Home Screen (`HomeScreen.kt`):**
   - Compact Header: Emblem (44dp), app title, calm subtitle, and accessible Light / Dark mode toggle button (`>= 48dp` touch target) on a single clean row.
   - 10-Deck Responsive Grid: Large cards for all 10 decks (9 thematic decks + "All Decks Mix").
   - Preferences & Assist Card:
     - 15-Second Timer toggle (default off).
     - Soothing Background Music toggle (default off).
     - Haptics & Feedback Sounds toggle (default on).
   - Pinned Bottom Bar: Full-width **"Play"** button (`60dp`).
2. **Practice Screen (`GameScreen.kt`):**
   - Single-Row Top Bar: "Home" button, centered deck title with `Card X of Y` progress, and "Decks" modal button (all non-wrapping).
   - In-Game Control Strip:
     - 15-second countdown timer badge/toggle with pause and resume.
     - Compact **Music** toggle button (with active visual highlight and TalkBack state).
     - Compact **Sound** toggle button (with active visual highlight and TalkBack state).
   - Primary Workbook Card: Crisp clue card (160dp minHeight) or gentle soft sage reveal card with emoji and status text.
   - Answer Input Zone: High-contrast text field + large Voice ("Speak") button + Submit button (`>= 60dp`, `10dp` spacing).
   - Calm Feedback States:
     - `MATCHED`: Soft sage/pine highlight (`selectedOptionBg`) with checkmark and gentle two-tone chime.
     - `TRY_AGAIN`: Soft light-red highlight (`unmatchedBg` / `unmatchedBorder`): "Not quite yet — try again, or tap Show Answer below."
   - Pinned Bottom Bar: `Show Answer` / `Next Card` button (`60dp`).
3. **Calm Game Results Summary (`SessionCompleteView`):**
   - Displayed upon completing all cards in a deck.
   - Deck title and completion notice ("You finished all X cards in Y").
   - Outcome Stat Chips: "Recalled Directly" and "Answer Viewed" (plus "Skipped" if any), with respectful, non-punitive tone.
   - **Warm Appreciation Message:** Calm, encouraging card thanking the user for practicing and acknowledging their focus and effort in completing the deck.
   - Educational reinforcement: "Viewing clues and recalling familiar items strengthens memory pathways and everyday focus."
   - **Card-by-Card Recap:** Clue, correct answer, **"Your answer"** entry (highlighted in soft light red if unmatched, or green/pine if matched), and outcome badges.
   - Action Buttons: Large (`>= 60dp`), non-wrapping buttons for **"Play Again"**, **"Choose Another Category"**, and **"Return to Home"**.
4. **Modal Deck Picker:**
   - Accessible dialog listing all 10 offline decks with card counts and active indicator.
