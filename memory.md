# Workspace Memory: CogniApp

## Project Context
- **App Name:** CogniApp
- **Goal:** Button-driven Android cognitive practice app for Alzheimer's patients, adapted from a 10-card *"Guess the Fruit"* video.
- **Environment:** Built with Antigravity using Kotlin and Jetpack Compose.

## Key Guardrails for Implementation
- Keep interactions calm, unhurried, and errorless—never use harsh red error states or sudden forced auto-advances by default.
- Keep buttons large (`>= 60dp` height) with comfortable spacing.
- Ensure offline execution with the bundled 10 fruit cards.
- Preserve state across configuration changes (such as screen rotation) and guard against accidental double-taps when advancing cards.

## Build Milestones
- [ ] **Milestone 1:** Project setup, theme tokens (`design.md`), and `FruitCard` + 10-card `FruitDeckRepository` (`architecture.md`).
- [ ] **Milestone 2:** `PracticeViewModel` with option selection, answer reveal, next card, restart, and optional toggleable timer.
- [ ] **Milestone 3:** `GameScreen` UI with large accessible buttons, clue/answer card states, and session completion screen.
- [ ] **Milestone 4:** Accessibility audit (TalkBack `contentDescription`, touch target sizes, double-tap guard, rotation state survival) and device testing.
