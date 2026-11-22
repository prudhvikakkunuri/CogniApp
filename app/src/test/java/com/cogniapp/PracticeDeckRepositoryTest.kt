package com.cogniapp

import com.cogniapp.data.PracticeDeckRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PracticeDeckRepositoryTest {

    private lateinit var repository: PracticeDeckRepository

    @Before
    fun setUp() {
        repository = PracticeDeckRepository()
    }

    @Test
    fun getAllDecks_returnsTenDecksIncludingMixedAll() {
        val decks = repository.getAllDecks()
        assertEquals(10, decks.size)

        val expectedIds = listOf(
            PracticeDeckRepository.DECK_FRUITS,
            PracticeDeckRepository.DECK_VEGETABLES,
            PracticeDeckRepository.DECK_ANIMALS,
            PracticeDeckRepository.DECK_DAILY_OBJECTS,
            PracticeDeckRepository.DECK_FOOD_DRINKS,
            PracticeDeckRepository.DECK_COLORS_SHAPES,
            PracticeDeckRepository.DECK_CLOTHES_WEARABLES,
            PracticeDeckRepository.DECK_NATURE_WEATHER,
            PracticeDeckRepository.DECK_DAILY_ROUTINES,
            PracticeDeckRepository.DECK_MIXED_ALL
        )
        val actualIds = decks.map { it.id }
        assertEquals(expectedIds, actualIds)
    }

    @Test
    fun getDefaultDeck_returnsFruitsDeck() {
        val defaultDeck = repository.getDefaultDeck()
        assertEquals(PracticeDeckRepository.DECK_FRUITS, defaultDeck.id)
        assertEquals("Guess the Fruit", defaultDeck.title)
        assertTrue(defaultDeck.cardCount >= 10)
    }

    @Test
    fun thematicDecks_haveBetweenEightAndTwelveCards() {
        repository.getThematicDecks().forEach { deck ->
            assertTrue(
                "Thematic deck ${deck.id} must have 8 to 12 cards, but has ${deck.cardCount}",
                deck.cardCount in 8..12
            )
        }
    }

    @Test
    fun mixedAllDeck_aggregatesAllThematicCards() {
        val mixedDeck = repository.getDeck(PracticeDeckRepository.DECK_MIXED_ALL)
        assertNotNull(mixedDeck)

        val expectedTotal = repository.getThematicDecks().sumOf { it.cardCount }
        assertEquals(expectedTotal, mixedDeck!!.cardCount)
        assertEquals("Mixed All Decks", mixedDeck.title)
        assertEquals("All Decks Mix", mixedDeck.categoryLabel)
    }

    @Test
    fun allCardsInAllDecks_haveValidCluesOptionsAndAnswers() {
        repository.getAllDecks().forEach { deck ->
            deck.cards.forEachIndexed { index, card ->
                assertEquals("Card deckId must match parent deck", deck.id, card.deckId)
                assertEquals("Card id should match 1-based index", index + 1, card.id)
                assertTrue("Clue must not be blank in ${deck.id} card ${card.id}", card.clue.isNotBlank())
                assertTrue("Emoji must not be blank in ${deck.id} card ${card.id}", card.emoji.isNotBlank())
                assertTrue(
                    "Options count should be between 3 and 4 in ${deck.id} card ${card.id}",
                    card.options.size in 3..4
                )
                assertTrue(
                    "Options must contain correctAnswer '${card.correctAnswer}' in ${deck.id} card ${card.id}",
                    card.options.contains(card.correctAnswer)
                )
            }
        }
    }

    @Test
    fun vegetablesDeck_containsAllRequiredVegetables() {
        val vegDeck = repository.getDeck(PracticeDeckRepository.DECK_VEGETABLES)
        assertNotNull(vegDeck)

        val answers = vegDeck!!.cards.map { it.correctAnswer }
        val required = listOf(
            "Carrot", "Potato", "Broccoli", "Onion",
            "Tomato", "Spinach", "Corn", "Cucumber"
        )
        required.forEach { veg ->
            assertTrue("Vegetables deck must contain $veg", answers.contains(veg))
        }
    }

    @Test
    fun animalsDeck_containsAllRequiredAnimals() {
        val animalsDeck = repository.getDeck(PracticeDeckRepository.DECK_ANIMALS)
        assertNotNull(animalsDeck)

        val answers = animalsDeck!!.cards.map { it.correctAnswer }
        val required = listOf(
            "Dog", "Cat", "Elephant", "Giraffe",
            "Cow", "Lion", "Bird", "Fish"
        )
        required.forEach { animal ->
            assertTrue("Animals deck must contain $animal", answers.contains(animal))
        }
    }

    @Test
    fun dailyObjectsDeck_containsAllRequiredObjects() {
        val objectsDeck = repository.getDeck(PracticeDeckRepository.DECK_DAILY_OBJECTS)
        assertNotNull(objectsDeck)

        val answers = objectsDeck!!.cards.map { it.correctAnswer }
        val actualRequired = listOf(
            "Car", "Bike", "Bottle", "Key",
            "Spoon", "Chair", "Umbrella", "Clock"
        )
        actualRequired.forEach { obj ->
            assertTrue("Everyday Things deck must contain $obj", answers.contains(obj))
        }
    }

    @Test
    fun foodDrinksDeck_containsAllRequiredItems() {
        val foodDeck = repository.getDeck(PracticeDeckRepository.DECK_FOOD_DRINKS)
        assertNotNull(foodDeck)

        val answers = foodDeck!!.cards.map { it.correctAnswer }
        val required = listOf(
            "Noodles", "Tea", "Coffee", "Rice",
            "Bread", "Milk", "Soup", "Egg"
        )
        required.forEach { item ->
            assertTrue("Food & Drinks deck must contain $item", answers.contains(item))
        }
    }

    @Test
    fun colorsShapesDeck_containsColorsAndShapes() {
        val deck = repository.getDeck(PracticeDeckRepository.DECK_COLORS_SHAPES)
        assertNotNull(deck)
        val answers = deck!!.cards.map { it.correctAnswer }
        assertTrue(answers.contains("Red"))
        assertTrue(answers.contains("Blue"))
        assertTrue(answers.contains("Circle"))
        assertTrue(answers.contains("Square"))
    }

    @Test
    fun clothesWearablesDeck_containsClothes() {
        val deck = repository.getDeck(PracticeDeckRepository.DECK_CLOTHES_WEARABLES)
        assertNotNull(deck)
        val answers = deck!!.cards.map { it.correctAnswer }
        assertTrue(answers.contains("Hat"))
        assertTrue(answers.contains("Shirt"))
        assertTrue(answers.contains("Shoes"))
        assertTrue(answers.contains("Watch"))
    }

    @Test
    fun natureWeatherDeck_containsNatureElements() {
        val deck = repository.getDeck(PracticeDeckRepository.DECK_NATURE_WEATHER)
        assertNotNull(deck)
        val answers = deck!!.cards.map { it.correctAnswer }
        assertTrue(answers.contains("Sun"))
        assertTrue(answers.contains("Rain"))
        assertTrue(answers.contains("Tree"))
        assertTrue(answers.contains("Moon"))
    }

    @Test
    fun dailyRoutinesDeck_containsRoutines() {
        val deck = repository.getDeck(PracticeDeckRepository.DECK_DAILY_ROUTINES)
        assertNotNull(deck)
        val answers = deck!!.cards.map { it.correctAnswer }
        assertTrue(answers.contains("Morning"))
        assertTrue(answers.contains("Bedtime"))
        assertTrue(answers.contains("Breakfast"))
        assertTrue(answers.contains("Walking"))
    }

    @Test
    fun getDeck_withCaseInsensitiveOrUnknownId() {
        assertNotNull(repository.getDeck("FRUITS"))
        assertNotNull(repository.getDeck("Vegetables"))
        assertNotNull(repository.getDeck("mixed_all"))
        assertNull(repository.getDeck("unknown_deck_id"))
    }
}
