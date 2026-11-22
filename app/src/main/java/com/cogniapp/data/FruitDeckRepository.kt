package com.cogniapp.data

import com.cogniapp.model.PracticeCard

/**
 * Offline repository providing the bundled 10-card "Guess the Fruit" (Easy) deck.
 *
 * Delegates to [PracticeDeckRepository] for unified multi-deck data access
 * while maintaining 100% backward compatibility for all existing callers.
 */
class FruitDeckRepository(
    private val practiceDeckRepository: PracticeDeckRepository = PracticeDeckRepository()
) {

    /**
     * Returns the canonical 10 fruit cards in this deck for backward compatibility.
     */
    fun getCards(): List<PracticeCard> =
        (practiceDeckRepository.getDeck(PracticeDeckRepository.DECK_FRUITS)?.cards ?: emptyList()).take(10)

    /**
     * Returns the total number of cards in the deck.
     */
    fun getCardCount(): Int = getCards().size

    /**
     * Returns the card at [index], or null if index is out of bounds.
     */
    fun getCard(index: Int): PracticeCard? = getCards().getOrNull(index)
}
