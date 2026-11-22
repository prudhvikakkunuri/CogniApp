package com.cogniapp.model

/**
 * Immutable data model representing a card in a cognitive practice deck.
 *
 * @property id Unique 1-based card identifier within its deck.
 * @property deckId String identifier of the parent deck (e.g. "fruits", "vegetables").
 * @property category Category label (e.g. "Guess the Fruit", "Guess the Vegetable").
 * @property difficulty Difficulty rating (defaults to "Easy").
 * @property clue Descriptive clue text presented on the card surface.
 * @property options List of 3 to 4 distinct answer choices, guaranteed to include [correctAnswer].
 * @property correctAnswer Correct answer matching one item in [options].
 * @property emoji Visual cue / illustration emoji revealed alongside the answer.
 */
data class PracticeCard(
    val id: Int,
    val deckId: String = "fruits",
    val category: String = "Guess the Fruit",
    val difficulty: String = "Easy",
    val clue: String,
    val options: List<String>,
    val correctAnswer: String,
    val emoji: String
)

/**
 * Backward compatibility typealias for FruitCard.
 */
typealias FruitCard = PracticeCard
