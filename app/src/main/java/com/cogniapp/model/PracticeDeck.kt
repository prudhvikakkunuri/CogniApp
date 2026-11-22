package com.cogniapp.model

/**
 * Represents a thematic deck of cognitive practice cards.
 *
 * @property id Unique string identifier for the deck (e.g. "fruits", "vegetables").
 * @property title Full title of the deck (e.g. "Guess the Fruit").
 * @property difficulty Overall difficulty rating for the deck (defaults to "Easy").
 * @property emoji Representative emoji icon for the deck (e.g. "🍎", "🥕").
 * @property cards List of cards contained in this deck.
 * @property categoryLabel Short, unclipped category label for compact cards ("Fruits", "Vegetables", etc.).
 */
data class PracticeDeck(
    val id: String,
    val title: String,
    val difficulty: String = "Easy",
    val emoji: String,
    val cards: List<PracticeCard>,
    val categoryLabel: String = when (id) {
        "fruits" -> "Fruits"
        "vegetables" -> "Vegetables"
        "animals" -> "Animals"
        "daily_objects" -> "Everyday Things"
        "food_drinks" -> "Food & Drinks"
        "colors_shapes" -> "Colors & Shapes"
        "clothes_wearables" -> "Clothes & Wear"
        "nature_weather" -> "Nature & Weather"
        "daily_routines" -> "Daily Routines"
        "mixed_all" -> "All Decks Mix"
        else -> title.replace("Guess the ", "")
    }
) {
    /**
     * Total number of cards in this deck.
     */
    val cardCount: Int
        get() = cards.size
}
