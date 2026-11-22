package com.cogniapp.data

import com.cogniapp.model.PracticeCard
import com.cogniapp.model.PracticeDeck

/**
 * Offline repository providing 10 thematic and mixed cognitive practice decks:
 * 1. Fruits
 * 2. Vegetables
 * 3. Animals
 * 4. Daily Life Objects
 * 5. Food & Drinks
 * 6. Colors & Shapes
 * 7. Clothes & Wearables
 * 8. Nature & Weather
 * 9. Daily Routines & Time
 * 10. Mixed All Decks (All Decks Mix)
 *
 * Each card features high-contrast readable clues, clear options, a normalized
 * correctAnswer for typing and voice STT, and an emoji visual cue.
 */
class PracticeDeckRepository {

    companion object {
        const val DECK_FRUITS = "fruits"
        const val DECK_VEGETABLES = "vegetables"
        const val DECK_ANIMALS = "animals"
        const val DECK_DAILY_OBJECTS = "daily_objects"
        const val DECK_FOOD_DRINKS = "food_drinks"
        const val DECK_COLORS_SHAPES = "colors_shapes"
        const val DECK_CLOTHES_WEARABLES = "clothes_wearables"
        const val DECK_NATURE_WEATHER = "nature_weather"
        const val DECK_DAILY_ROUTINES = "daily_routines"
        const val DECK_MIXED_ALL = "mixed_all"

        const val DEFAULT_DECK_ID = DECK_FRUITS
    }

    private val fruitsDeck = PracticeDeck(
        id = DECK_FRUITS,
        title = "Guess the Fruit",
        difficulty = "Easy",
        emoji = "🍎",
        cards = listOf(
            PracticeCard(
                id = 1,
                deckId = DECK_FRUITS,
                category = "Guess the Fruit",
                difficulty = "Easy",
                clue = "Guess the fruit that is juicy, sweet, and tear-drop shaped.",
                options = listOf("Pear", "Apple", "Peach", "Plum"),
                correctAnswer = "Pear",
                emoji = "🍐"
            ),
            PracticeCard(
                id = 2,
                deckId = DECK_FRUITS,
                category = "Guess the Fruit",
                difficulty = "Easy",
                clue = "Guess the fruit that is red, crunchy, and often associated with keeping the doctor away.",
                options = listOf("Orange", "Apple", "Banana", "Grape"),
                correctAnswer = "Apple",
                emoji = "🍎"
            ),
            PracticeCard(
                id = 3,
                deckId = DECK_FRUITS,
                category = "Guess the Fruit",
                difficulty = "Easy",
                clue = "Guess the fruit that is yellow, long, and easy to peel.",
                options = listOf("Lemon", "Banana", "Mango", "Pineapple"),
                correctAnswer = "Banana",
                emoji = "🍌"
            ),
            PracticeCard(
                id = 4,
                deckId = DECK_FRUITS,
                category = "Guess the Fruit",
                difficulty = "Easy",
                clue = "Guess the fruit that is orange, round, and full of juicy segments.",
                options = listOf("Orange", "Peach", "Apricot", "Papaya"),
                correctAnswer = "Orange",
                emoji = "🍊"
            ),
            PracticeCard(
                id = 5,
                deckId = DECK_FRUITS,
                category = "Guess the Fruit",
                difficulty = "Easy",
                clue = "Guess the fruit that is small, red, and has tiny seeds on its outside.",
                options = listOf("Cherry", "Strawberry", "Grape", "Raspberry"),
                correctAnswer = "Strawberry",
                emoji = "🍓"
            ),
            PracticeCard(
                id = 6,
                deckId = DECK_FRUITS,
                category = "Guess the Fruit",
                difficulty = "Easy",
                clue = "Guess the fruit that is green outside, pink inside, and has many tiny black seeds.",
                options = listOf("Kiwi", "Guava", "Watermelon", "Fig"),
                correctAnswer = "Guava",
                emoji = "🍈"
            ),
            PracticeCard(
                id = 7,
                deckId = DECK_FRUITS,
                category = "Guess the Fruit",
                difficulty = "Easy",
                clue = "Guess the fruit that is yellow, sour, and commonly used in lemonade.",
                options = listOf("Lime", "Lemon", "Grapefruit", "Orange"),
                correctAnswer = "Lemon",
                emoji = "🍋"
            ),
            PracticeCard(
                id = 8,
                deckId = DECK_FRUITS,
                category = "Guess the Fruit",
                difficulty = "Easy",
                clue = "Guess the fruit that is purple or green, grows in bunches, and can be dried into raisins.",
                options = listOf("Grape", "Blueberry", "Plum", "Olive"),
                correctAnswer = "Grape",
                emoji = "🍇"
            ),
            PracticeCard(
                id = 9,
                deckId = DECK_FRUITS,
                category = "Guess the Fruit",
                difficulty = "Easy",
                clue = "Guess the fruit that is fuzzy outside, brown inside, and has a bright green center.",
                options = listOf("Kiwi", "Avocado", "Coconut", "Passionfruit"),
                correctAnswer = "Kiwi",
                emoji = "🥝"
            ),
            PracticeCard(
                id = 10,
                deckId = DECK_FRUITS,
                category = "Guess the Fruit",
                difficulty = "Easy",
                clue = "Guess the fruit that is large, green outside, red inside, and very refreshing on a hot day.",
                options = listOf("Cantaloupe", "Watermelon", "Honeydew", "Papaya"),
                correctAnswer = "Watermelon",
                emoji = "🍉"
            ),
            PracticeCard(
                id = 11,
                deckId = DECK_FRUITS,
                category = "Guess the Fruit",
                difficulty = "Easy",
                clue = "Guess the sweet tropical fruit with golden flesh and a large flat stone inside.",
                options = listOf("Mango", "Pineapple", "Papaya", "Peach"),
                correctAnswer = "Mango",
                emoji = "🥭"
            ),
            PracticeCard(
                id = 12,
                deckId = DECK_FRUITS,
                category = "Guess the Fruit",
                difficulty = "Easy",
                clue = "Guess the soft velvety fruit with pink-orange skin and sweet fragrant flesh.",
                options = listOf("Peach", "Plum", "Apricot", "Cherry"),
                correctAnswer = "Peach",
                emoji = "🍑"
            )
        )
    )

    private val vegetablesDeck = PracticeDeck(
        id = DECK_VEGETABLES,
        title = "Guess the Vegetable",
        difficulty = "Easy",
        emoji = "🥕",
        cards = listOf(
            PracticeCard(
                id = 1,
                deckId = DECK_VEGETABLES,
                category = "Guess the Vegetable",
                difficulty = "Easy",
                clue = "Guess the vegetable that is bright orange, crunchy, and grows underground.",
                options = listOf("Carrot", "Sweet Potato", "Turnip", "Radish"),
                correctAnswer = "Carrot",
                emoji = "🥕"
            ),
            PracticeCard(
                id = 2,
                deckId = DECK_VEGETABLES,
                category = "Guess the Vegetable",
                difficulty = "Easy",
                clue = "Guess the vegetable that grows underground and can be mashed, baked, or made into fries.",
                options = listOf("Potato", "Onion", "Beet", "Garlic"),
                correctAnswer = "Potato",
                emoji = "🥔"
            ),
            PracticeCard(
                id = 3,
                deckId = DECK_VEGETABLES,
                category = "Guess the Vegetable",
                difficulty = "Easy",
                clue = "Guess the green vegetable that looks like a little tree.",
                options = listOf("Broccoli", "Cauliflower", "Celery", "Asparagus"),
                correctAnswer = "Broccoli",
                emoji = "🥦"
            ),
            PracticeCard(
                id = 4,
                deckId = DECK_VEGETABLES,
                category = "Guess the Vegetable",
                difficulty = "Easy",
                clue = "Guess the layered vegetable with a papery skin that can bring a tear to your eye when sliced.",
                options = listOf("Onion", "Garlic", "Shallot", "Leek"),
                correctAnswer = "Onion",
                emoji = "🧅"
            ),
            PracticeCard(
                id = 5,
                deckId = DECK_VEGETABLES,
                category = "Guess the Vegetable",
                difficulty = "Easy",
                clue = "Guess the juicy red vegetable often used in fresh salads and pasta sauce.",
                options = listOf("Tomato", "Red Pepper", "Radish", "Beetroot"),
                correctAnswer = "Tomato",
                emoji = "🍅"
            ),
            PracticeCard(
                id = 6,
                deckId = DECK_VEGETABLES,
                category = "Guess the Vegetable",
                difficulty = "Easy",
                clue = "Guess the leafy green vegetable known for making Popeye strong.",
                options = listOf("Spinach", "Kale", "Lettuce", "Cabbage"),
                correctAnswer = "Spinach",
                emoji = "🥬"
            ),
            PracticeCard(
                id = 7,
                deckId = DECK_VEGETABLES,
                category = "Guess the Vegetable",
                difficulty = "Easy",
                clue = "Guess the yellow vegetable with rows of sweet kernels on a cob.",
                options = listOf("Corn", "Yellow Pepper", "Squash", "Pumpkin"),
                correctAnswer = "Corn",
                emoji = "🌽"
            ),
            PracticeCard(
                id = 8,
                deckId = DECK_VEGETABLES,
                category = "Guess the Vegetable",
                difficulty = "Easy",
                clue = "Guess the long, cool green vegetable that is crisp and often made into pickles.",
                options = listOf("Cucumber", "Zucchini", "Celery", "Green Bean"),
                correctAnswer = "Cucumber",
                emoji = "🥒"
            ),
            PracticeCard(
                id = 9,
                deckId = DECK_VEGETABLES,
                category = "Guess the Vegetable",
                difficulty = "Easy",
                clue = "Guess the large round orange vegetable commonly carved into holiday lanterns and baked in pies.",
                options = listOf("Pumpkin", "Squash", "Sweet Potato", "Carrot"),
                correctAnswer = "Pumpkin",
                emoji = "🎃"
            ),
            PracticeCard(
                id = 10,
                deckId = DECK_VEGETABLES,
                category = "Guess the Vegetable",
                difficulty = "Easy",
                clue = "Guess the small round green seeds that grow in a pod and taste sweet when steamed.",
                options = listOf("Peas", "Beans", "Edamame", "Corn"),
                correctAnswer = "Peas",
                emoji = "🫛"
            )
        )
    )

    private val animalsDeck = PracticeDeck(
        id = DECK_ANIMALS,
        title = "Guess the Animal",
        difficulty = "Easy",
        emoji = "🐶",
        cards = listOf(
            PracticeCard(
                id = 1,
                deckId = DECK_ANIMALS,
                category = "Guess the Animal",
                difficulty = "Easy",
                clue = "Guess the loyal animal known as man's best friend that barks and wags its tail.",
                options = listOf("Dog", "Wolf", "Fox", "Cat"),
                correctAnswer = "Dog",
                emoji = "🐶"
            ),
            PracticeCard(
                id = 2,
                deckId = DECK_ANIMALS,
                category = "Guess the Animal",
                difficulty = "Easy",
                clue = "Guess the furry pet that purrs softly and loves to chase mice.",
                options = listOf("Cat", "Rabbit", "Hamster", "Squirrel"),
                correctAnswer = "Cat",
                emoji = "🐱"
            ),
            PracticeCard(
                id = 3,
                deckId = DECK_ANIMALS,
                category = "Guess the Animal",
                difficulty = "Easy",
                clue = "Guess the giant animal with large ears, a long trunk, and curved tusks.",
                options = listOf("Elephant", "Rhino", "Hippo", "Bear"),
                correctAnswer = "Elephant",
                emoji = "🐘"
            ),
            PracticeCard(
                id = 4,
                deckId = DECK_ANIMALS,
                category = "Guess the Animal",
                difficulty = "Easy",
                clue = "Guess the tall animal with a very long neck and brown spotted coat.",
                options = listOf("Giraffe", "Zebra", "Camel", "Deer"),
                correctAnswer = "Giraffe",
                emoji = "🦒"
            ),
            PracticeCard(
                id = 5,
                deckId = DECK_ANIMALS,
                category = "Guess the Animal",
                difficulty = "Easy",
                clue = "Guess the gentle farm animal that says 'moo' and gives us fresh milk.",
                options = listOf("Cow", "Goat", "Sheep", "Horse"),
                correctAnswer = "Cow",
                emoji = "🐮"
            ),
            PracticeCard(
                id = 6,
                deckId = DECK_ANIMALS,
                category = "Guess the Animal",
                difficulty = "Easy",
                clue = "Guess the majestic big cat known as the 'King of the Jungle' with a glorious mane.",
                options = listOf("Lion", "Tiger", "Leopard", "Cheetah"),
                correctAnswer = "Lion",
                emoji = "🦁"
            ),
            PracticeCard(
                id = 7,
                deckId = DECK_ANIMALS,
                category = "Guess the Animal",
                difficulty = "Easy",
                clue = "Guess the feathered animal that builds nests in trees and can fly across the sky.",
                options = listOf("Bird", "Bat", "Butterfly", "Bee"),
                correctAnswer = "Bird",
                emoji = "🐦"
            ),
            PracticeCard(
                id = 8,
                deckId = DECK_ANIMALS,
                category = "Guess the Animal",
                difficulty = "Easy",
                clue = "Guess the animal that has scales, breathes with gills, and swims in the water.",
                options = listOf("Fish", "Turtle", "Frog", "Duck"),
                correctAnswer = "Fish",
                emoji = "🐟"
            ),
            PracticeCard(
                id = 9,
                deckId = DECK_ANIMALS,
                category = "Guess the Animal",
                difficulty = "Easy",
                clue = "Guess the magnificent animal with a flowing mane that neighs and can be ridden.",
                options = listOf("Horse", "Donkey", "Camel", "Zebra"),
                correctAnswer = "Horse",
                emoji = "🐴"
            ),
            PracticeCard(
                id = 10,
                deckId = DECK_ANIMALS,
                category = "Guess the Animal",
                difficulty = "Easy",
                clue = "Guess the water bird that quacks, has webbed feet, and swims in the pond.",
                options = listOf("Duck", "Swan", "Goose", "Penguin"),
                correctAnswer = "Duck",
                emoji = "🦆"
            )
        )
    )

    private val dailyObjectsDeck = PracticeDeck(
        id = DECK_DAILY_OBJECTS,
        title = "Everyday Things",
        difficulty = "Easy",
        emoji = "🏠",
        cards = listOf(
            PracticeCard(
                id = 1,
                deckId = DECK_DAILY_OBJECTS,
                category = "Everyday Things",
                difficulty = "Easy",
                clue = "Guess the vehicle with four wheels and an engine that you drive on roads.",
                options = listOf("Car", "Bus", "Train", "Truck"),
                correctAnswer = "Car",
                emoji = "🚗"
            ),
            PracticeCard(
                id = 2,
                deckId = DECK_DAILY_OBJECTS,
                category = "Everyday Things",
                difficulty = "Easy",
                clue = "Guess the two-wheeled vehicle that you pedal with your feet.",
                options = listOf("Bike", "Scooter", "Skateboard", "Motorcycle"),
                correctAnswer = "Bike",
                emoji = "🚲"
            ),
            PracticeCard(
                id = 3,
                deckId = DECK_DAILY_OBJECTS,
                category = "Everyday Things",
                difficulty = "Easy",
                clue = "Guess the everyday container used for holding water and refreshing drinks.",
                options = listOf("Bottle", "Mug", "Bowl", "Kettle"),
                correctAnswer = "Bottle",
                emoji = "🧴"
            ),
            PracticeCard(
                id = 4,
                deckId = DECK_DAILY_OBJECTS,
                category = "Everyday Things",
                difficulty = "Easy",
                clue = "Guess the small metal object used to lock and unlock doors.",
                options = listOf("Key", "Coin", "Nail", "Ring"),
                correctAnswer = "Key",
                emoji = "🔑"
            ),
            PracticeCard(
                id = 5,
                deckId = DECK_DAILY_OBJECTS,
                category = "Everyday Things",
                difficulty = "Easy",
                clue = "Guess the utensil with a shallow rounded bowl used for eating soup and stirring tea.",
                options = listOf("Spoon", "Fork", "Knife", "Whisk"),
                correctAnswer = "Spoon",
                emoji = "🥄"
            ),
            PracticeCard(
                id = 6,
                deckId = DECK_DAILY_OBJECTS,
                category = "Everyday Things",
                difficulty = "Easy",
                clue = "Guess the piece of furniture with four legs and a back made for sitting comfortably.",
                options = listOf("Chair", "Table", "Bed", "Desk"),
                correctAnswer = "Chair",
                emoji = "🪑"
            ),
            PracticeCard(
                id = 7,
                deckId = DECK_DAILY_OBJECTS,
                category = "Everyday Things",
                difficulty = "Easy",
                clue = "Guess the handheld canopy that opens up to keep you dry in the rain.",
                options = listOf("Umbrella", "Hat", "Coat", "Tent"),
                correctAnswer = "Umbrella",
                emoji = "☂️"
            ),
            PracticeCard(
                id = 8,
                deckId = DECK_DAILY_OBJECTS,
                category = "Everyday Things",
                difficulty = "Easy",
                clue = "Guess the object on the wall or bedside table that ticks and tells the time.",
                options = listOf("Clock", "Mirror", "Radio", "Lamp"),
                correctAnswer = "Clock",
                emoji = "⏰"
            ),
            PracticeCard(
                id = 9,
                deckId = DECK_DAILY_OBJECTS,
                category = "Everyday Things",
                difficulty = "Easy",
                clue = "Guess the bound collection of printed pages that you read for stories and knowledge.",
                options = listOf("Book", "Newspaper", "Letter", "Notebook"),
                correctAnswer = "Book",
                emoji = "📖"
            ),
            PracticeCard(
                id = 10,
                deckId = DECK_DAILY_OBJECTS,
                category = "Everyday Things",
                difficulty = "Easy",
                clue = "Guess the drinking vessel with a handle used for holding hot coffee or tea.",
                options = listOf("Cup", "Bowl", "Plate", "Jug"),
                correctAnswer = "Cup",
                emoji = "☕"
            )
        )
    )

    private val foodDrinksDeck = PracticeDeck(
        id = DECK_FOOD_DRINKS,
        title = "Food & Drinks",
        difficulty = "Easy",
        emoji = "🍲",
        cards = listOf(
            PracticeCard(
                id = 1,
                deckId = DECK_FOOD_DRINKS,
                category = "Food & Drinks",
                difficulty = "Easy",
                clue = "Guess the long, stringy food that you twirl on a fork or eat with chopsticks.",
                options = listOf("Noodles", "Rice", "Bread", "Beans"),
                correctAnswer = "Noodles",
                emoji = "🍜"
            ),
            PracticeCard(
                id = 2,
                deckId = DECK_FOOD_DRINKS,
                category = "Food & Drinks",
                difficulty = "Easy",
                clue = "Guess the soothing warm drink brewed by steeping dried fragrant leaves in hot water.",
                options = listOf("Tea", "Coffee", "Juice", "Milk"),
                correctAnswer = "Tea",
                emoji = "🍵"
            ),
            PracticeCard(
                id = 3,
                deckId = DECK_FOOD_DRINKS,
                category = "Food & Drinks",
                difficulty = "Easy",
                clue = "Guess the aromatic morning beverage brewed from roasted dark beans.",
                options = listOf("Coffee", "Hot Chocolate", "Tea", "Soda"),
                correctAnswer = "Coffee",
                emoji = "☕"
            ),
            PracticeCard(
                id = 4,
                deckId = DECK_FOOD_DRINKS,
                category = "Food & Drinks",
                difficulty = "Easy",
                clue = "Guess the small white or brown grains steamed and eaten as a staple with many meals.",
                options = listOf("Rice", "Oats", "Barley", "Corn"),
                correctAnswer = "Rice",
                emoji = "🍚"
            ),
            PracticeCard(
                id = 5,
                deckId = DECK_FOOD_DRINKS,
                category = "Food & Drinks",
                difficulty = "Easy",
                clue = "Guess the baked food made from flour and yeast, often sliced for toast and sandwiches.",
                options = listOf("Bread", "Cake", "Pancake", "Biscuit"),
                correctAnswer = "Bread",
                emoji = "🍞"
            ),
            PracticeCard(
                id = 6,
                deckId = DECK_FOOD_DRINKS,
                category = "Food & Drinks",
                difficulty = "Easy",
                clue = "Guess the white nutritious drink that comes from cows and is poured over cereal.",
                options = listOf("Milk", "Yogurt", "Cream", "Water"),
                correctAnswer = "Milk",
                emoji = "🥛"
            ),
            PracticeCard(
                id = 7,
                deckId = DECK_FOOD_DRINKS,
                category = "Food & Drinks",
                difficulty = "Easy",
                clue = "Guess the warm liquid dish cooked in a pot with vegetables or broth, served in a bowl.",
                options = listOf("Soup", "Stew", "Sauce", "Curry"),
                correctAnswer = "Soup",
                emoji = "🍲"
            ),
            PracticeCard(
                id = 8,
                deckId = DECK_FOOD_DRINKS,
                category = "Food & Drinks",
                difficulty = "Easy",
                clue = "Guess the oval food laid by chickens that can be scrambled, boiled, or fried.",
                options = listOf("Egg", "Cheese", "Butter", "Tofu"),
                correctAnswer = "Egg",
                emoji = "🥚"
            ),
            PracticeCard(
                id = 9,
                deckId = DECK_FOOD_DRINKS,
                category = "Food & Drinks",
                difficulty = "Easy",
                clue = "Guess the sweet fruit drink squeezed fresh from oranges, apples, or berries.",
                options = listOf("Juice", "Water", "Tea", "Milk"),
                correctAnswer = "Juice",
                emoji = "🧃"
            ),
            PracticeCard(
                id = 10,
                deckId = DECK_FOOD_DRINKS,
                category = "Food & Drinks",
                difficulty = "Easy",
                clue = "Guess the popular baked dish of round flat dough topped with tomato sauce and melted cheese.",
                options = listOf("Pizza", "Pie", "Sandwich", "Pasta"),
                correctAnswer = "Pizza",
                emoji = "🍕"
            )
        )
    )

    private val colorsShapesDeck = PracticeDeck(
        id = DECK_COLORS_SHAPES,
        title = "Colors & Shapes",
        difficulty = "Easy",
        emoji = "🎨",
        cards = listOf(
            PracticeCard(
                id = 1,
                deckId = DECK_COLORS_SHAPES,
                category = "Colors & Shapes",
                difficulty = "Easy",
                clue = "Guess the bright warm color of a ripe strawberry, a rose, or a fire engine.",
                options = listOf("Red", "Blue", "Green", "Yellow"),
                correctAnswer = "Red",
                emoji = "🔴"
            ),
            PracticeCard(
                id = 2,
                deckId = DECK_COLORS_SHAPES,
                category = "Colors & Shapes",
                difficulty = "Easy",
                clue = "Guess the cool calming color of a clear summer sky or a deep ocean.",
                options = listOf("Blue", "Purple", "Green", "Black"),
                correctAnswer = "Blue",
                emoji = "🔵"
            ),
            PracticeCard(
                id = 3,
                deckId = DECK_COLORS_SHAPES,
                category = "Colors & Shapes",
                difficulty = "Easy",
                clue = "Guess the fresh color of lawn grass, springtime leaves, and emeralds.",
                options = listOf("Green", "Yellow", "Brown", "Orange"),
                correctAnswer = "Green",
                emoji = "🟢"
            ),
            PracticeCard(
                id = 4,
                deckId = DECK_COLORS_SHAPES,
                category = "Colors & Shapes",
                difficulty = "Easy",
                clue = "Guess the bright cheerful color of cheerful sunshine and ripe lemons.",
                options = listOf("Yellow", "White", "Pink", "Red"),
                correctAnswer = "Yellow",
                emoji = "🟡"
            ),
            PracticeCard(
                id = 5,
                deckId = DECK_COLORS_SHAPES,
                category = "Colors & Shapes",
                difficulty = "Easy",
                clue = "Guess the perfectly round shape with no corners that looks like a coin or clock.",
                options = listOf("Circle", "Square", "Triangle", "Star"),
                correctAnswer = "Circle",
                emoji = "⭕"
            ),
            PracticeCard(
                id = 6,
                deckId = DECK_COLORS_SHAPES,
                category = "Colors & Shapes",
                difficulty = "Easy",
                clue = "Guess the shape that has four equal straight sides and four sharp corners.",
                options = listOf("Square", "Circle", "Oval", "Heart"),
                correctAnswer = "Square",
                emoji = "⬛"
            ),
            PracticeCard(
                id = 7,
                deckId = DECK_COLORS_SHAPES,
                category = "Colors & Shapes",
                difficulty = "Easy",
                clue = "Guess the pointed shape that twinkles in the clear night sky.",
                options = listOf("Star", "Moon", "Cloud", "Sun"),
                correctAnswer = "Star",
                emoji = "⭐"
            ),
            PracticeCard(
                id = 8,
                deckId = DECK_COLORS_SHAPES,
                category = "Colors & Shapes",
                difficulty = "Easy",
                clue = "Guess the geometric shape that has exactly three sides and three corners.",
                options = listOf("Triangle", "Rectangle", "Square", "Diamond"),
                correctAnswer = "Triangle",
                emoji = "🔺"
            )
        )
    )

    private val clothesWearablesDeck = PracticeDeck(
        id = DECK_CLOTHES_WEARABLES,
        title = "Clothes & Wearables",
        difficulty = "Easy",
        emoji = "👒",
        cards = listOf(
            PracticeCard(
                id = 1,
                deckId = DECK_CLOTHES_WEARABLES,
                category = "Clothes & Wearables",
                difficulty = "Easy",
                clue = "Guess the headwear worn outdoors to shield your eyes and head from the sun.",
                options = listOf("Hat", "Scarf", "Belt", "Socks"),
                correctAnswer = "Hat",
                emoji = "👒"
            ),
            PracticeCard(
                id = 2,
                deckId = DECK_CLOTHES_WEARABLES,
                category = "Clothes & Wearables",
                difficulty = "Easy",
                clue = "Guess the garment worn on the upper body with sleeves and buttons down the front.",
                options = listOf("Shirt", "Pants", "Socks", "Shoes"),
                correctAnswer = "Shirt",
                emoji = "👕"
            ),
            PracticeCard(
                id = 3,
                deckId = DECK_CLOTHES_WEARABLES,
                category = "Clothes & Wearables",
                difficulty = "Easy",
                clue = "Guess the sturdy footwear worn over socks to protect your feet when walking.",
                options = listOf("Shoes", "Gloves", "Hat", "Coat"),
                correctAnswer = "Shoes",
                emoji = "👟"
            ),
            PracticeCard(
                id = 4,
                deckId = DECK_CLOTHES_WEARABLES,
                category = "Clothes & Wearables",
                difficulty = "Easy",
                clue = "Guess the soft knitted coverings worn on your feet inside your shoes.",
                options = listOf("Socks", "Gloves", "Scarf", "Slippers"),
                correctAnswer = "Socks",
                emoji = "🧦"
            ),
            PracticeCard(
                id = 5,
                deckId = DECK_CLOTHES_WEARABLES,
                category = "Clothes & Wearables",
                difficulty = "Easy",
                clue = "Guess the warm outer coat worn over clothes on cool or windy days.",
                options = listOf("Jacket", "T-shirt", "Vest", "Belt"),
                correctAnswer = "Jacket",
                emoji = "🧥"
            ),
            PracticeCard(
                id = 6,
                deckId = DECK_CLOTHES_WEARABLES,
                category = "Clothes & Wearables",
                difficulty = "Easy",
                clue = "Guess the small timepiece strapped around the wrist to tell the hours and minutes.",
                options = listOf("Watch", "Ring", "Bracelet", "Necklace"),
                correctAnswer = "Watch",
                emoji = "⌚"
            ),
            PracticeCard(
                id = 7,
                deckId = DECK_CLOTHES_WEARABLES,
                category = "Clothes & Wearables",
                difficulty = "Easy",
                clue = "Guess the optical frames with clear lenses worn over the eyes to help reading.",
                options = listOf("Glasses", "Sunglasses", "Goggles", "Binoculars"),
                correctAnswer = "Glasses",
                emoji = "👓"
            ),
            PracticeCard(
                id = 8,
                deckId = DECK_CLOTHES_WEARABLES,
                category = "Clothes & Wearables",
                difficulty = "Easy",
                clue = "Guess the soft length of knitted fabric wrapped around the neck for winter warmth.",
                options = listOf("Scarf", "Tie", "Belt", "Collar"),
                correctAnswer = "Scarf",
                emoji = "🧣"
            )
        )
    )

    private val natureWeatherDeck = PracticeDeck(
        id = DECK_NATURE_WEATHER,
        title = "Nature & Weather",
        difficulty = "Easy",
        emoji = "🌳",
        cards = listOf(
            PracticeCard(
                id = 1,
                deckId = DECK_NATURE_WEATHER,
                category = "Nature & Weather",
                difficulty = "Easy",
                clue = "Guess the bright golden star that warms the earth and lights up daytime.",
                options = listOf("Sun", "Moon", "Cloud", "Star"),
                correctAnswer = "Sun",
                emoji = "☀️"
            ),
            PracticeCard(
                id = 2,
                deckId = DECK_NATURE_WEATHER,
                category = "Nature & Weather",
                difficulty = "Easy",
                clue = "Guess the refreshing water droplets falling from grey clouds onto the garden.",
                options = listOf("Rain", "Snow", "Fog", "Hail"),
                correctAnswer = "Rain",
                emoji = "🌧️"
            ),
            PracticeCard(
                id = 3,
                deckId = DECK_NATURE_WEATHER,
                category = "Nature & Weather",
                difficulty = "Easy",
                clue = "Guess the soft white frozen flakes that gently flutter down on a winter day.",
                options = listOf("Snow", "Frost", "Ice", "Hail"),
                correctAnswer = "Snow",
                emoji = "❄️"
            ),
            PracticeCard(
                id = 4,
                deckId = DECK_NATURE_WEATHER,
                category = "Nature & Weather",
                difficulty = "Easy",
                clue = "Guess the colorful arch of red, green, and blue that appears in the sky after a shower.",
                options = listOf("Rainbow", "Sunset", "Aurora", "Comet"),
                correctAnswer = "Rainbow",
                emoji = "🌈"
            ),
            PracticeCard(
                id = 5,
                deckId = DECK_NATURE_WEATHER,
                category = "Nature & Weather",
                difficulty = "Easy",
                clue = "Guess the tall woodland plant with a strong wooden trunk, branches, and green leaves.",
                options = listOf("Tree", "Bush", "Grass", "Flower"),
                correctAnswer = "Tree",
                emoji = "🌳"
            ),
            PracticeCard(
                id = 6,
                deckId = DECK_NATURE_WEATHER,
                category = "Nature & Weather",
                difficulty = "Easy",
                clue = "Guess the winding stream of freshwater flowing across the land into the sea.",
                options = listOf("River", "Pond", "Lake", "Ocean"),
                correctAnswer = "River",
                emoji = "🌊"
            ),
            PracticeCard(
                id = 7,
                deckId = DECK_NATURE_WEATHER,
                category = "Nature & Weather",
                difficulty = "Easy",
                clue = "Guess the fluffy white or grey mass floating high up in the blue sky.",
                options = listOf("Cloud", "Fog", "Smoke", "Mist"),
                correctAnswer = "Cloud",
                emoji = "☁️"
            ),
            PracticeCard(
                id = 8,
                deckId = DECK_NATURE_WEATHER,
                category = "Nature & Weather",
                difficulty = "Easy",
                clue = "Guess the glowing celestial sphere that brightens the night sky in changing phases.",
                options = listOf("Moon", "Sun", "Star", "Planet"),
                correctAnswer = "Moon",
                emoji = "🌙"
            )
        )
    )

    private val dailyRoutinesDeck = PracticeDeck(
        id = DECK_DAILY_ROUTINES,
        title = "Daily Routines",
        difficulty = "Easy",
        emoji = "⏰",
        cards = listOf(
            PracticeCard(
                id = 1,
                deckId = DECK_DAILY_ROUTINES,
                category = "Daily Routines",
                difficulty = "Easy",
                clue = "Guess the early time of day when the sun rises and a brand-new day begins.",
                options = listOf("Morning", "Afternoon", "Night", "Evening"),
                correctAnswer = "Morning",
                emoji = "🌅"
            ),
            PracticeCard(
                id = 2,
                deckId = DECK_DAILY_ROUTINES,
                category = "Daily Routines",
                difficulty = "Easy",
                clue = "Guess the restful evening hour when you tuck into bed to sleep.",
                options = listOf("Bedtime", "Noon", "Morning", "Breakfast"),
                correctAnswer = "Bedtime",
                emoji = "🛏️"
            ),
            PracticeCard(
                id = 3,
                deckId = DECK_DAILY_ROUTINES,
                category = "Daily Routines",
                difficulty = "Easy",
                clue = "Guess the first meal of the day eaten soon after waking up in the morning.",
                options = listOf("Breakfast", "Lunch", "Dinner", "Snack"),
                correctAnswer = "Breakfast",
                emoji = "🥞"
            ),
            PracticeCard(
                id = 4,
                deckId = DECK_DAILY_ROUTINES,
                category = "Daily Routines",
                difficulty = "Easy",
                clue = "Guess the relaxing wash in warm soapy water to feel refreshed and clean.",
                options = listOf("Bath", "Nap", "Swim", "Wash"),
                correctAnswer = "Bath",
                emoji = "🛁"
            ),
            PracticeCard(
                id = 5,
                deckId = DECK_DAILY_ROUTINES,
                category = "Daily Routines",
                difficulty = "Easy",
                clue = "Guess the gentle outdoor exercise of stepping along a quiet park or garden path.",
                options = listOf("Walking", "Running", "Driving", "Resting"),
                correctAnswer = "Walking",
                emoji = "🚶"
            ),
            PracticeCard(
                id = 6,
                deckId = DECK_DAILY_ROUTINES,
                category = "Daily Routines",
                difficulty = "Easy",
                clue = "Guess the quiet enjoyable pastime of looking at words in a book or magazine.",
                options = listOf("Reading", "Writing", "Drawing", "Talking"),
                correctAnswer = "Reading",
                emoji = "📖"
            ),
            PracticeCard(
                id = 7,
                deckId = DECK_DAILY_ROUTINES,
                category = "Daily Routines",
                difficulty = "Easy",
                clue = "Guess the comforting activity of preparing a warm homemade meal in the kitchen.",
                options = listOf("Cooking", "Cleaning", "Baking", "Shopping"),
                correctAnswer = "Cooking",
                emoji = "🍳"
            ),
            PracticeCard(
                id = 8,
                deckId = DECK_DAILY_ROUTINES,
                category = "Daily Routines",
                difficulty = "Easy",
                clue = "Guess the calming hobby of tending to flowers, potted herbs, and garden plants.",
                options = listOf("Gardening", "Sewing", "Painting", "Knitting"),
                correctAnswer = "Gardening",
                emoji = "🪴"
            )
        )
    )

    private val thematicDecks = listOf(
        fruitsDeck,
        vegetablesDeck,
        animalsDeck,
        dailyObjectsDeck,
        foodDrinksDeck,
        colorsShapesDeck,
        clothesWearablesDeck,
        natureWeatherDeck,
        dailyRoutinesDeck
    )

    // Aggregate mixed deck containing cards across all thematic decks
    private val mixedAllDeck: PracticeDeck = PracticeDeck(
        id = DECK_MIXED_ALL,
        title = "Mixed All Decks",
        difficulty = "Easy",
        emoji = "🎲",
        cards = thematicDecks.flatMap { it.cards }.mapIndexed { index, card ->
            card.copy(id = index + 1, deckId = DECK_MIXED_ALL)
        }
    )

    private val allDecks = thematicDecks + mixedAllDeck

    /**
     * Returns all offline decks available for practice (thematic decks + mixed all decks).
     */
    fun getAllDecks(): List<PracticeDeck> = allDecks

    /**
     * Returns only the individual thematic decks.
     */
    fun getThematicDecks(): List<PracticeDeck> = thematicDecks

    /**
     * Finds a deck by its string identifier, or null if not found.
     */
    fun getDeck(deckId: String): PracticeDeck? =
        allDecks.firstOrNull { it.id.equals(deckId, ignoreCase = true) }

    /**
     * Returns the default deck (Guess the Fruit).
     */
    fun getDefaultDeck(): PracticeDeck =
        getDeck(DEFAULT_DECK_ID) ?: fruitsDeck

    /**
     * Returns cards for the default deck (backward compatibility helper).
     */
    fun getCards(): List<PracticeCard> = getDefaultDeck().cards
}
