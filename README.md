# CogniApp

**CogniApp** is a calm, offline-first Android cognitive-practice application built with **Kotlin** and **Jetpack Compose (Material 3)** for people living with Alzheimer’s disease and their caregivers. Inspired by a 10-card *"Guess the Fruit"* flashcard format, it replaces rushed auto-advancing timers with button-driven play, large high-contrast touch targets, and warm, non-punitive feedback.

---

## Key Features & UX Design

- **Button-Driven, Errorless Practice:** Users tap large answer choice buttons (`>= 64dp` height with `16dp` spacing) and advance with explicit `Show Answer` and `Next Card` controls. Selections use soft, encouraging visual highlights rather than harsh red error states or punitive buzzers.
- **5 Offline Practice Decks (42 Cards):** Bundled locally so sessions run smoothly without network delays:
  - **Fruits** (`Guess the Fruit` — 10 cards)
  - **Vegetables** (`Guess the Vegetable` — 8 cards)
  - **Animals** (`Guess the Animal` — 8 cards)
  - **Everyday Things** (`Daily Life Objects` — 8 cards)
  - **Food & Drinks** (`Food & Drinks` — 8 cards)
- **Optional, Non-Rushed Timer:** A countdown timer is **off by default** so users are never cut off mid-thought, with an accessible toggle if a caregiver wants gentle timing support.
- **Calm Low-Glare Palette:** Uses `MidnightNavy` (`#14162E`) as a low-glare backdrop, `WarmCreamCard` (`#FAF4E4`) for clues, and `GoldenAmberReveal` (`#E2B038`) for answer reveals with large emoji cues.
- **Resilience & Accessibility:** Includes 400ms rapid-click debounce protection, TalkBack `contentDescription` labels, and `ViewModel` state preservation across configuration changes such as screen rotation.

---

## Project Structure

- `prd.md`, `design.md`, `architecture.md`, `memory.md` — Product requirements, visual/accessibility guidelines, architecture notes, and workspace milestone tracking.
- `app/src/main/java/com/cogniapp/model/` — `PracticeCard` (with `FruitCard` alias) and `PracticeDeck`.
- `app/src/main/java/com/cogniapp/data/` — `PracticeDeckRepository` (and `FruitDeckRepository` compatibility wrapper).
- `app/src/main/java/com/cogniapp/viewmodel/` — `PracticeUiState` and `PracticeViewModel` (`StateFlow`).
- `app/src/main/java/com/cogniapp/ui/theme/` — Material 3 color tokens, typography scale, and `CogniAppTheme`.
- `app/src/main/java/com/cogniapp/ui/screens/GameScreen.kt` — Main practice screen, deck switcher, reveal state, and completion view.
- `app/src/main/java/com/cogniapp/MainActivity.kt` — Activity entry point wired to `PracticeViewModel` and `CogniAppTheme`.
- `app/src/test/java/com/cogniapp/` — Unit and accessibility/state tests.

---

## Build & Run Tests

Run from the project root directory:

```powershell
# Run unit and accessibility tests
.\gradlew.bat testDebugUnitTest

# Assemble debug APK (outputs to app\build\outputs\apk\debug\app-debug.apk)
.\gradlew.bat assembleDebug
