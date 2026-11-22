package com.cogniapp

import com.cogniapp.data.FruitDeckRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FruitDeckRepositoryTest {

    private lateinit var repository: FruitDeckRepository

    @Before
    fun setUp() {
        repository = FruitDeckRepository()
    }

    @Test
    fun getCards_returnsExactlyTenCards() {
        val cards = repository.getCards()
        assertEquals(10, cards.size)
        assertEquals(10, repository.getCardCount())
    }

    @Test
    fun allCards_haveValidStructureAndOptions() {
        val cards = repository.getCards()
        cards.forEachIndexed { index, card ->
            assertEquals("Card id should match 1-based index", index + 1, card.id)
            assertEquals("Guess the Fruit", card.category)
            assertEquals("Easy", card.difficulty)
            assertTrue("Clue must not be blank", card.clue.isNotBlank())
            assertTrue("Emoji must not be blank", card.emoji.isNotBlank())
            assertTrue(
                "Options count should be between 3 and 4, got ${card.options.size}",
                card.options.size in 3..4
            )
            assertTrue(
                "Options must contain the correctAnswer '${card.correctAnswer}'",
                card.options.contains(card.correctAnswer)
            )
        }
    }

    @Test
    fun cards_matchArchitectureSpecification() {
        val cards = repository.getCards()
        assertEquals("Pear", cards[0].correctAnswer)
        assertEquals("🍐", cards[0].emoji)

        assertEquals("Apple", cards[1].correctAnswer)
        assertEquals("🍎", cards[1].emoji)

        assertEquals("Banana", cards[2].correctAnswer)
        assertEquals("🍌", cards[2].emoji)

        assertEquals("Orange", cards[3].correctAnswer)
        assertEquals("🍊", cards[3].emoji)

        assertEquals("Strawberry", cards[4].correctAnswer)
        assertEquals("🍓", cards[4].emoji)

        assertEquals("Guava", cards[5].correctAnswer)
        assertEquals("🍈", cards[5].emoji)

        assertEquals("Lemon", cards[6].correctAnswer)
        assertEquals("🍋", cards[6].emoji)

        assertEquals("Grape", cards[7].correctAnswer)
        assertEquals("🍇", cards[7].emoji)

        assertEquals("Kiwi", cards[8].correctAnswer)
        assertEquals("🥝", cards[8].emoji)

        assertEquals("Watermelon", cards[9].correctAnswer)
        assertEquals("🍉", cards[9].emoji)
    }

    @Test
    fun getCard_outOfBounds_returnsNull() {
        assertNotNull(repository.getCard(0))
        assertNotNull(repository.getCard(9))
        assertEquals(null, repository.getCard(-1))
        assertEquals(null, repository.getCard(10))
    }
}
