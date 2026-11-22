# Product Requirements Document (PRD): CogniApp

## 1. Overview & Purpose
**CogniApp** is an interactive Android cognitive-practice game inspired by the *"Guess the Fruit"* flashcard video (`@BeyondTheAwareness`, Easy deck, 10 cards). It adapts passive clue-and-countdown flashcards into a calm, button-driven practice experience tailored for people living with Alzheimer’s disease and their caregivers.

## 2. Target Users
- **Primary User (Person Living with Alzheimer’s):** Benefits from clear clues, large high-contrast touch targets, zero-pressure pacing, and warm, non-punitive feedback.
- **Secondary User (Caregiver / Family Member):** Helps set up a comfortable session, toggle optional timer support, and guide or encourage play.

## 3. Core Functional Requirements
1. **Interactive Button-Driven Play:**
   - Display the category header (`Guess the Fruit`), difficulty badge (`Easy`), and card counter (`Card X / 10`).
   - Show a clear clue card and large, well-spaced answer choice buttons (2–4 options per card).
   - Provide a clear `Show Answer` / `Next Card` action flow with accidental double-tap protection.
2. **Errorless & Calm Feedback:**
   - Selecting an option softly highlights the choice and reveals the answer card clearly without harsh red error states, buzzers, or shaming messages.
   - Reveal screen reinforces the answer (and optional visual/emoji cue) before advancing.
3. **Optional, Non-Rushed Timer:**
   - Unlike the 10-second auto-advancing video, the countdown timer defaults to **Off** (or gentle/pausable when enabled by a caregiver/user) so users are never abruptly cut off mid-thought.
4. **Offline-First Deck & Session Summary:**
   - All 10 fruit cards are bundled locally so the app works reliably offline.
   - At the end of Card 10, display a warm completion screen with a `Play Again` button.

## 4. Non-Goals (v1)
- No medical diagnosis, clinical assessment, or cognitive scoring claims.
- No complex account walls, social feeds, or distracting ads during play.
