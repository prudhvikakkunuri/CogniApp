# Design Specification: CogniApp

## 1. Visual Tone & Palette
The visual identity draws inspiration from the high-contrast card layout in the reference video while softening transitions for older adults and low-glare readability:
- **Background (`MidnightNavy`):** Deep navy (`#14162E`) for low glare and calm focus around the central card.
- **Clue Card Surface (`WarmCreamCard`):** Soft warm cream (`#FAF4E4`) with crisp dark ink (`#14162E`) for high text contrast.
- **Answer Reveal Surface (`GoldenAmberReveal`):** Warm golden amber (`#E2B038`) matching the answer card reveal in the video, paired with dark text (`#14162E`).
- **Gentle Selection / Confirmation:** Soft sage/teal or warm highlight states—avoiding harsh bright red error flashes.

## 2. Typography & Spacing
- **Clue Text:** `24sp–28sp`, generous line height (`1.35x–1.45x`), clear sans-serif/readable styling.
- **Card Counter & Badges:** `16sp–18sp`, easy to scan at a glance.
- **Touch Targets:** Minimum `60dp–64dp` button height, full/wide pill or rounded rectangle shapes (`16dp` corner radius), and at least `12dp–16dp` vertical spacing between buttons to prevent mis-taps.

## 3. Screen Layout
1. **Top Bar:** Category title (`Guess the Fruit`), difficulty tag (`Easy`), and progress indicator (`Card X / 10`), plus an optional subtle settings/timer icon.
2. **Primary Card Zone:** Displays either the **Question Clue** (and optional timer bar if enabled) or the **Answer Reveal** card.
3. **Action / Choice Zone:** Large interactive option buttons and a prominent `Next Card` / `Play Again` button at the bottom.
