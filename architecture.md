# Architecture Specification: CogniApp

## 1. Tech Stack
- **Platform:** Android (Kotlin + Jetpack Compose + Material 3)
- **State Management:** Unidirectional Data Flow (`StateFlow` + `ViewModel`)
- **Data Source:** Bundled local deck repository (`FruitDeckRepository`) for offline reliability

## 2. Package Structure
- `com.cogniapp.model.FruitCard` — Immutable data model (`id`, `category`, `difficulty`, `clue`, `options`, `correctAnswer`, `emoji`)
- `com.cogniapp.data.FruitDeckRepository` — Provides the 10-card *Guess the Fruit* deck
- `com.cogniapp.viewmodel.PracticeViewModel` — Holds `PracticeUiState` and handles selection, reveal, navigation, optional timer, and restart
- `com.cogniapp.ui.theme` — Color tokens, typography, and Material 3 theme
- `com.cogniapp.ui.screens.GameScreen` — Main practice screen and completion state

## 3. UI State Model (`PracticeUiState`)
- `currentIndex: Int` (`0..9`)
- `cards: List<FruitCard>`
- `selectedOption: String?`
- `isAnswerRevealed: Boolean`
- `timerEnabled: Boolean` (default `false`)
- `secondsRemaining: Int` (default `10` when timer is enabled)
- `isTimerPaused: Boolean`
- `isSessionComplete: Boolean`

## 4. Bundled 10-Card Deck ("Guess the Fruit" — Easy)
1. **Card 1:** *"Guess the fruit that is juicy, sweet, and tear-drop shaped."* → **Pear**
2. **Card 2:** *"Guess the fruit that is red, crunchy, and often associated with keeping the doctor away."* → **Apple**
3. **Card 3:** *"Guess the fruit that is yellow, long, and easy to peel."* → **Banana**
4. **Card 4:** *"Guess the fruit that is orange, round, and full of juicy segments."* → **Orange**
5. **Card 5:** *"Guess the fruit that is small, red, and has tiny seeds on its outside."* → **Strawberry**
6. **Card 6:** *"Guess the fruit that is green outside, pink inside, and has many tiny black seeds."* → **Guava**
7. **Card 7:** *"Guess the fruit that is yellow, sour, and commonly used in lemonade."* → **Lemon**
8. **Card 8:** *"Guess the fruit that is purple or green, grows in bunches, and can be dried into raisins."* → **Grape**
9. **Card 9:** *"Guess the fruit that is fuzzy outside, brown inside, and has a bright green center."* → **Kiwi**
10. **Card 10:** *"Guess the fruit that is large, green outside, red inside, and very refreshing on a hot day."* → **Watermelon**
