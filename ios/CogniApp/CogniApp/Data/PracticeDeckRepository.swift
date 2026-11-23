import Foundation

/// Offline repository providing 10 thematic and mixed cognitive practice decks.
/// Fully ported from Android PracticeDeckRepository with 100% card, clue, and answer parity.
public final class PracticeDeckRepository {

    public static let deckFruits = "fruits"
    public static let deckVegetables = "vegetables"
    public static let deckAnimals = "animals"
    public static let deckDailyObjects = "daily_objects"
    public static let deckFoodDrinks = "food_drinks"
    public static let deckColorsShapes = "colors_shapes"
    public static let deckClothesWearables = "clothes_wearables"
    public static let deckNatureWeather = "nature_weather"
    public static let deckDailyRoutines = "daily_routines"
    public static let deckMixedAll = "mixed_all"

    public static let defaultDeckId = deckFruits

    public static let fruitsDeck = PracticeDeck(
        id: "fruits",
        title: "Guess the Fruit",
        categoryLabel: "Fruits",
        difficulty: "Easy",
        emoji: "🍎",
        cards: [
            PracticeCard(
                id: 1,
                deckId: "fruits",
                category: "Guess the Fruit",
                difficulty: "Easy",
                clue: "Guess the fruit that is juicy, sweet, and tear-drop shaped.",
                options: ["Pear", "Apple", "Peach", "Plum"],
                correctAnswer: "Pear",
                emoji: "🍐"
            ),
            PracticeCard(
                id: 2,
                deckId: "fruits",
                category: "Guess the Fruit",
                difficulty: "Easy",
                clue: "Guess the fruit that is red, crunchy, and often associated with keeping the doctor away.",
                options: ["Orange", "Apple", "Banana", "Grape"],
                correctAnswer: "Apple",
                emoji: "🍎"
            ),
            PracticeCard(
                id: 3,
                deckId: "fruits",
                category: "Guess the Fruit",
                difficulty: "Easy",
                clue: "Guess the fruit that is yellow, long, and easy to peel.",
                options: ["Lemon", "Banana", "Mango", "Pineapple"],
                correctAnswer: "Banana",
                emoji: "🍌"
            ),
            PracticeCard(
                id: 4,
                deckId: "fruits",
                category: "Guess the Fruit",
                difficulty: "Easy",
                clue: "Guess the fruit that is orange, round, and full of juicy segments.",
                options: ["Orange", "Peach", "Apricot", "Papaya"],
                correctAnswer: "Orange",
                emoji: "🍊"
            ),
            PracticeCard(
                id: 5,
                deckId: "fruits",
                category: "Guess the Fruit",
                difficulty: "Easy",
                clue: "Guess the fruit that is small, red, and has tiny seeds on its outside.",
                options: ["Cherry", "Strawberry", "Grape", "Raspberry"],
                correctAnswer: "Strawberry",
                emoji: "🍓"
            ),
            PracticeCard(
                id: 6,
                deckId: "fruits",
                category: "Guess the Fruit",
                difficulty: "Easy",
                clue: "Guess the fruit that is green outside, pink inside, and has many tiny black seeds.",
                options: ["Kiwi", "Guava", "Watermelon", "Fig"],
                correctAnswer: "Guava",
                emoji: "🍈"
            ),
            PracticeCard(
                id: 7,
                deckId: "fruits",
                category: "Guess the Fruit",
                difficulty: "Easy",
                clue: "Guess the fruit that is yellow, sour, and commonly used in lemonade.",
                options: ["Lime", "Lemon", "Grapefruit", "Orange"],
                correctAnswer: "Lemon",
                emoji: "🍋"
            ),
            PracticeCard(
                id: 8,
                deckId: "fruits",
                category: "Guess the Fruit",
                difficulty: "Easy",
                clue: "Guess the fruit that is purple or green, grows in bunches, and can be dried into raisins.",
                options: ["Grape", "Blueberry", "Plum", "Olive"],
                correctAnswer: "Grape",
                emoji: "🍇"
            ),
            PracticeCard(
                id: 9,
                deckId: "fruits",
                category: "Guess the Fruit",
                difficulty: "Easy",
                clue: "Guess the fruit that is fuzzy outside, brown inside, and has a bright green center.",
                options: ["Kiwi", "Avocado", "Coconut", "Passionfruit"],
                correctAnswer: "Kiwi",
                emoji: "🥝"
            ),
            PracticeCard(
                id: 10,
                deckId: "fruits",
                category: "Guess the Fruit",
                difficulty: "Easy",
                clue: "Guess the fruit that is large, green outside, red inside, and very refreshing on a hot day.",
                options: ["Cantaloupe", "Watermelon", "Honeydew", "Papaya"],
                correctAnswer: "Watermelon",
                emoji: "🍉"
            ),
            PracticeCard(
                id: 11,
                deckId: "fruits",
                category: "Guess the Fruit",
                difficulty: "Easy",
                clue: "Guess the sweet tropical fruit with golden flesh and a large flat stone inside.",
                options: ["Mango", "Pineapple", "Papaya", "Peach"],
                correctAnswer: "Mango",
                emoji: "🥭"
            ),
            PracticeCard(
                id: 12,
                deckId: "fruits",
                category: "Guess the Fruit",
                difficulty: "Easy",
                clue: "Guess the soft velvety fruit with pink-orange skin and sweet fragrant flesh.",
                options: ["Peach", "Plum", "Apricot", "Cherry"],
                correctAnswer: "Peach",
                emoji: "🍑"
            )
        ]
    )
    public static let vegetablesDeck = PracticeDeck(
        id: "vegetables",
        title: "Guess the Vegetable",
        categoryLabel: "Vegetables",
        difficulty: "Easy",
        emoji: "🥕",
        cards: [
            PracticeCard(
                id: 1,
                deckId: "vegetables",
                category: "Guess the Vegetable",
                difficulty: "Easy",
                clue: "Guess the vegetable that is bright orange, crunchy, and grows underground.",
                options: ["Carrot", "Sweet Potato", "Turnip", "Radish"],
                correctAnswer: "Carrot",
                emoji: "🥕"
            ),
            PracticeCard(
                id: 2,
                deckId: "vegetables",
                category: "Guess the Vegetable",
                difficulty: "Easy",
                clue: "Guess the vegetable that grows underground and can be mashed, baked, or made into fries.",
                options: ["Potato", "Onion", "Beet", "Garlic"],
                correctAnswer: "Potato",
                emoji: "🥔"
            ),
            PracticeCard(
                id: 3,
                deckId: "vegetables",
                category: "Guess the Vegetable",
                difficulty: "Easy",
                clue: "Guess the green vegetable that looks like a little tree.",
                options: ["Broccoli", "Cauliflower", "Celery", "Asparagus"],
                correctAnswer: "Broccoli",
                emoji: "🥦"
            ),
            PracticeCard(
                id: 4,
                deckId: "vegetables",
                category: "Guess the Vegetable",
                difficulty: "Easy",
                clue: "Guess the layered vegetable with a papery skin that can bring a tear to your eye when sliced.",
                options: ["Onion", "Garlic", "Shallot", "Leek"],
                correctAnswer: "Onion",
                emoji: "🧅"
            ),
            PracticeCard(
                id: 5,
                deckId: "vegetables",
                category: "Guess the Vegetable",
                difficulty: "Easy",
                clue: "Guess the juicy red vegetable often used in fresh salads and pasta sauce.",
                options: ["Tomato", "Red Pepper", "Radish", "Beetroot"],
                correctAnswer: "Tomato",
                emoji: "🍅"
            ),
            PracticeCard(
                id: 6,
                deckId: "vegetables",
                category: "Guess the Vegetable",
                difficulty: "Easy",
                clue: "Guess the leafy green vegetable known for making Popeye strong.",
                options: ["Spinach", "Kale", "Lettuce", "Cabbage"],
                correctAnswer: "Spinach",
                emoji: "🥬"
            ),
            PracticeCard(
                id: 7,
                deckId: "vegetables",
                category: "Guess the Vegetable",
                difficulty: "Easy",
                clue: "Guess the yellow vegetable with rows of sweet kernels on a cob.",
                options: ["Corn", "Yellow Pepper", "Squash", "Pumpkin"],
                correctAnswer: "Corn",
                emoji: "🌽"
            ),
            PracticeCard(
                id: 8,
                deckId: "vegetables",
                category: "Guess the Vegetable",
                difficulty: "Easy",
                clue: "Guess the long, cool green vegetable that is crisp and often made into pickles.",
                options: ["Cucumber", "Zucchini", "Celery", "Green Bean"],
                correctAnswer: "Cucumber",
                emoji: "🥒"
            ),
            PracticeCard(
                id: 9,
                deckId: "vegetables",
                category: "Guess the Vegetable",
                difficulty: "Easy",
                clue: "Guess the large round orange vegetable commonly carved into holiday lanterns and baked in pies.",
                options: ["Pumpkin", "Squash", "Sweet Potato", "Carrot"],
                correctAnswer: "Pumpkin",
                emoji: "🎃"
            ),
            PracticeCard(
                id: 10,
                deckId: "vegetables",
                category: "Guess the Vegetable",
                difficulty: "Easy",
                clue: "Guess the small round green seeds that grow in a pod and taste sweet when steamed.",
                options: ["Peas", "Beans", "Edamame", "Corn"],
                correctAnswer: "Peas",
                emoji: "🫛"
            )
        ]
    )
    public static let animalsDeck = PracticeDeck(
        id: "animals",
        title: "Guess the Animal",
        categoryLabel: "Animals",
        difficulty: "Easy",
        emoji: "🐶",
        cards: [
            PracticeCard(
                id: 1,
                deckId: "animals",
                category: "Guess the Animal",
                difficulty: "Easy",
                clue: "Guess the loyal animal known as man's best friend that barks and wags its tail.",
                options: ["Dog", "Wolf", "Fox", "Cat"],
                correctAnswer: "Dog",
                emoji: "🐶"
            ),
            PracticeCard(
                id: 2,
                deckId: "animals",
                category: "Guess the Animal",
                difficulty: "Easy",
                clue: "Guess the furry pet that purrs softly and loves to chase mice.",
                options: ["Cat", "Rabbit", "Hamster", "Squirrel"],
                correctAnswer: "Cat",
                emoji: "🐱"
            ),
            PracticeCard(
                id: 3,
                deckId: "animals",
                category: "Guess the Animal",
                difficulty: "Easy",
                clue: "Guess the giant animal with large ears, a long trunk, and curved tusks.",
                options: ["Elephant", "Rhino", "Hippo", "Bear"],
                correctAnswer: "Elephant",
                emoji: "🐘"
            ),
            PracticeCard(
                id: 4,
                deckId: "animals",
                category: "Guess the Animal",
                difficulty: "Easy",
                clue: "Guess the tall animal with a very long neck and brown spotted coat.",
                options: ["Giraffe", "Zebra", "Camel", "Deer"],
                correctAnswer: "Giraffe",
                emoji: "🦒"
            ),
            PracticeCard(
                id: 5,
                deckId: "animals",
                category: "Guess the Animal",
                difficulty: "Easy",
                clue: "Guess the gentle farm animal that says 'moo' and gives us fresh milk.",
                options: ["Cow", "Goat", "Sheep", "Horse"],
                correctAnswer: "Cow",
                emoji: "🐮"
            ),
            PracticeCard(
                id: 6,
                deckId: "animals",
                category: "Guess the Animal",
                difficulty: "Easy",
                clue: "Guess the majestic big cat known as the 'King of the Jungle' with a glorious mane.",
                options: ["Lion", "Tiger", "Leopard", "Cheetah"],
                correctAnswer: "Lion",
                emoji: "🦁"
            ),
            PracticeCard(
                id: 7,
                deckId: "animals",
                category: "Guess the Animal",
                difficulty: "Easy",
                clue: "Guess the feathered animal that builds nests in trees and can fly across the sky.",
                options: ["Bird", "Bat", "Butterfly", "Bee"],
                correctAnswer: "Bird",
                emoji: "🐦"
            ),
            PracticeCard(
                id: 8,
                deckId: "animals",
                category: "Guess the Animal",
                difficulty: "Easy",
                clue: "Guess the animal that has scales, breathes with gills, and swims in the water.",
                options: ["Fish", "Turtle", "Frog", "Duck"],
                correctAnswer: "Fish",
                emoji: "🐟"
            ),
            PracticeCard(
                id: 9,
                deckId: "animals",
                category: "Guess the Animal",
                difficulty: "Easy",
                clue: "Guess the magnificent animal with a flowing mane that neighs and can be ridden.",
                options: ["Horse", "Donkey", "Camel", "Zebra"],
                correctAnswer: "Horse",
                emoji: "🐴"
            ),
            PracticeCard(
                id: 10,
                deckId: "animals",
                category: "Guess the Animal",
                difficulty: "Easy",
                clue: "Guess the water bird that quacks, has webbed feet, and swims in the pond.",
                options: ["Duck", "Swan", "Goose", "Penguin"],
                correctAnswer: "Duck",
                emoji: "🦆"
            )
        ]
    )
    public static let dailyObjectsDeck = PracticeDeck(
        id: "daily_objects",
        title: "Everyday Things",
        categoryLabel: "Everyday Things",
        difficulty: "Easy",
        emoji: "🏠",
        cards: [
            PracticeCard(
                id: 1,
                deckId: "daily_objects",
                category: "Everyday Things",
                difficulty: "Easy",
                clue: "Guess the vehicle with four wheels and an engine that you drive on roads.",
                options: ["Car", "Bus", "Train", "Truck"],
                correctAnswer: "Car",
                emoji: "🚗"
            ),
            PracticeCard(
                id: 2,
                deckId: "daily_objects",
                category: "Everyday Things",
                difficulty: "Easy",
                clue: "Guess the two-wheeled vehicle that you pedal with your feet.",
                options: ["Bike", "Scooter", "Skateboard", "Motorcycle"],
                correctAnswer: "Bike",
                emoji: "🚲"
            ),
            PracticeCard(
                id: 3,
                deckId: "daily_objects",
                category: "Everyday Things",
                difficulty: "Easy",
                clue: "Guess the everyday container used for holding water and refreshing drinks.",
                options: ["Bottle", "Mug", "Bowl", "Kettle"],
                correctAnswer: "Bottle",
                emoji: "🧴"
            ),
            PracticeCard(
                id: 4,
                deckId: "daily_objects",
                category: "Everyday Things",
                difficulty: "Easy",
                clue: "Guess the small metal object used to lock and unlock doors.",
                options: ["Key", "Coin", "Nail", "Ring"],
                correctAnswer: "Key",
                emoji: "🔑"
            ),
            PracticeCard(
                id: 5,
                deckId: "daily_objects",
                category: "Everyday Things",
                difficulty: "Easy",
                clue: "Guess the utensil with a shallow rounded bowl used for eating soup and stirring tea.",
                options: ["Spoon", "Fork", "Knife", "Whisk"],
                correctAnswer: "Spoon",
                emoji: "🥄"
            ),
            PracticeCard(
                id: 6,
                deckId: "daily_objects",
                category: "Everyday Things",
                difficulty: "Easy",
                clue: "Guess the piece of furniture with four legs and a back made for sitting comfortably.",
                options: ["Chair", "Table", "Bed", "Desk"],
                correctAnswer: "Chair",
                emoji: "🪑"
            ),
            PracticeCard(
                id: 7,
                deckId: "daily_objects",
                category: "Everyday Things",
                difficulty: "Easy",
                clue: "Guess the handheld canopy that opens up to keep you dry in the rain.",
                options: ["Umbrella", "Hat", "Coat", "Tent"],
                correctAnswer: "Umbrella",
                emoji: "☂️"
            ),
            PracticeCard(
                id: 8,
                deckId: "daily_objects",
                category: "Everyday Things",
                difficulty: "Easy",
                clue: "Guess the object on the wall or bedside table that ticks and tells the time.",
                options: ["Clock", "Mirror", "Radio", "Lamp"],
                correctAnswer: "Clock",
                emoji: "⏰"
            ),
            PracticeCard(
                id: 9,
                deckId: "daily_objects",
                category: "Everyday Things",
                difficulty: "Easy",
                clue: "Guess the bound collection of printed pages that you read for stories and knowledge.",
                options: ["Book", "Newspaper", "Letter", "Notebook"],
                correctAnswer: "Book",
                emoji: "📖"
            ),
            PracticeCard(
                id: 10,
                deckId: "daily_objects",
                category: "Everyday Things",
                difficulty: "Easy",
                clue: "Guess the drinking vessel with a handle used for holding hot coffee or tea.",
                options: ["Cup", "Bowl", "Plate", "Jug"],
                correctAnswer: "Cup",
                emoji: "☕"
            )
        ]
    )
    public static let foodDrinksDeck = PracticeDeck(
        id: "food_drinks",
        title: "Food & Drinks",
        categoryLabel: "Food & Drinks",
        difficulty: "Easy",
        emoji: "🍲",
        cards: [
            PracticeCard(
                id: 1,
                deckId: "food_drinks",
                category: "Food & Drinks",
                difficulty: "Easy",
                clue: "Guess the long, stringy food that you twirl on a fork or eat with chopsticks.",
                options: ["Noodles", "Rice", "Bread", "Beans"],
                correctAnswer: "Noodles",
                emoji: "🍜"
            ),
            PracticeCard(
                id: 2,
                deckId: "food_drinks",
                category: "Food & Drinks",
                difficulty: "Easy",
                clue: "Guess the soothing warm drink brewed by steeping dried fragrant leaves in hot water.",
                options: ["Tea", "Coffee", "Juice", "Milk"],
                correctAnswer: "Tea",
                emoji: "🍵"
            ),
            PracticeCard(
                id: 3,
                deckId: "food_drinks",
                category: "Food & Drinks",
                difficulty: "Easy",
                clue: "Guess the aromatic morning beverage brewed from roasted dark beans.",
                options: ["Coffee", "Hot Chocolate", "Tea", "Soda"],
                correctAnswer: "Coffee",
                emoji: "☕"
            ),
            PracticeCard(
                id: 4,
                deckId: "food_drinks",
                category: "Food & Drinks",
                difficulty: "Easy",
                clue: "Guess the small white or brown grains steamed and eaten as a staple with many meals.",
                options: ["Rice", "Oats", "Barley", "Corn"],
                correctAnswer: "Rice",
                emoji: "🍚"
            ),
            PracticeCard(
                id: 5,
                deckId: "food_drinks",
                category: "Food & Drinks",
                difficulty: "Easy",
                clue: "Guess the baked food made from flour and yeast, often sliced for toast and sandwiches.",
                options: ["Bread", "Cake", "Pancake", "Biscuit"],
                correctAnswer: "Bread",
                emoji: "🍞"
            ),
            PracticeCard(
                id: 6,
                deckId: "food_drinks",
                category: "Food & Drinks",
                difficulty: "Easy",
                clue: "Guess the white nutritious drink that comes from cows and is poured over cereal.",
                options: ["Milk", "Yogurt", "Cream", "Water"],
                correctAnswer: "Milk",
                emoji: "🥛"
            ),
            PracticeCard(
                id: 7,
                deckId: "food_drinks",
                category: "Food & Drinks",
                difficulty: "Easy",
                clue: "Guess the warm liquid dish cooked in a pot with vegetables or broth, served in a bowl.",
                options: ["Soup", "Stew", "Sauce", "Curry"],
                correctAnswer: "Soup",
                emoji: "🍲"
            ),
            PracticeCard(
                id: 8,
                deckId: "food_drinks",
                category: "Food & Drinks",
                difficulty: "Easy",
                clue: "Guess the oval food laid by chickens that can be scrambled, boiled, or fried.",
                options: ["Egg", "Cheese", "Butter", "Tofu"],
                correctAnswer: "Egg",
                emoji: "🥚"
            ),
            PracticeCard(
                id: 9,
                deckId: "food_drinks",
                category: "Food & Drinks",
                difficulty: "Easy",
                clue: "Guess the sweet fruit drink squeezed fresh from oranges, apples, or berries.",
                options: ["Juice", "Water", "Tea", "Milk"],
                correctAnswer: "Juice",
                emoji: "🧃"
            ),
            PracticeCard(
                id: 10,
                deckId: "food_drinks",
                category: "Food & Drinks",
                difficulty: "Easy",
                clue: "Guess the popular baked dish of round flat dough topped with tomato sauce and melted cheese.",
                options: ["Pizza", "Pie", "Sandwich", "Pasta"],
                correctAnswer: "Pizza",
                emoji: "🍕"
            )
        ]
    )
    public static let colorsShapesDeck = PracticeDeck(
        id: "colors_shapes",
        title: "Colors & Shapes",
        categoryLabel: "Colors & Shapes",
        difficulty: "Easy",
        emoji: "🎨",
        cards: [
            PracticeCard(
                id: 1,
                deckId: "colors_shapes",
                category: "Colors & Shapes",
                difficulty: "Easy",
                clue: "Guess the bright warm color of a ripe strawberry, a rose, or a fire engine.",
                options: ["Red", "Blue", "Green", "Yellow"],
                correctAnswer: "Red",
                emoji: "🔴"
            ),
            PracticeCard(
                id: 2,
                deckId: "colors_shapes",
                category: "Colors & Shapes",
                difficulty: "Easy",
                clue: "Guess the cool calming color of a clear summer sky or a deep ocean.",
                options: ["Blue", "Purple", "Green", "Black"],
                correctAnswer: "Blue",
                emoji: "🔵"
            ),
            PracticeCard(
                id: 3,
                deckId: "colors_shapes",
                category: "Colors & Shapes",
                difficulty: "Easy",
                clue: "Guess the fresh color of lawn grass, springtime leaves, and emeralds.",
                options: ["Green", "Yellow", "Brown", "Orange"],
                correctAnswer: "Green",
                emoji: "🟢"
            ),
            PracticeCard(
                id: 4,
                deckId: "colors_shapes",
                category: "Colors & Shapes",
                difficulty: "Easy",
                clue: "Guess the bright cheerful color of cheerful sunshine and ripe lemons.",
                options: ["Yellow", "White", "Pink", "Red"],
                correctAnswer: "Yellow",
                emoji: "🟡"
            ),
            PracticeCard(
                id: 5,
                deckId: "colors_shapes",
                category: "Colors & Shapes",
                difficulty: "Easy",
                clue: "Guess the perfectly round shape with no corners that looks like a coin or clock.",
                options: ["Circle", "Square", "Triangle", "Star"],
                correctAnswer: "Circle",
                emoji: "⭕"
            ),
            PracticeCard(
                id: 6,
                deckId: "colors_shapes",
                category: "Colors & Shapes",
                difficulty: "Easy",
                clue: "Guess the shape that has four equal straight sides and four sharp corners.",
                options: ["Square", "Circle", "Oval", "Heart"],
                correctAnswer: "Square",
                emoji: "⬛"
            ),
            PracticeCard(
                id: 7,
                deckId: "colors_shapes",
                category: "Colors & Shapes",
                difficulty: "Easy",
                clue: "Guess the pointed shape that twinkles in the clear night sky.",
                options: ["Star", "Moon", "Cloud", "Sun"],
                correctAnswer: "Star",
                emoji: "⭐"
            ),
            PracticeCard(
                id: 8,
                deckId: "colors_shapes",
                category: "Colors & Shapes",
                difficulty: "Easy",
                clue: "Guess the geometric shape that has exactly three sides and three corners.",
                options: ["Triangle", "Rectangle", "Square", "Diamond"],
                correctAnswer: "Triangle",
                emoji: "🔺"
            )
        ]
    )
    public static let clothesWearablesDeck = PracticeDeck(
        id: "clothes_wearables",
        title: "Clothes & Wearables",
        categoryLabel: "Clothes & Wearables",
        difficulty: "Easy",
        emoji: "👒",
        cards: [
            PracticeCard(
                id: 1,
                deckId: "clothes_wearables",
                category: "Clothes & Wearables",
                difficulty: "Easy",
                clue: "Guess the headwear worn outdoors to shield your eyes and head from the sun.",
                options: ["Hat", "Scarf", "Belt", "Socks"],
                correctAnswer: "Hat",
                emoji: "👒"
            ),
            PracticeCard(
                id: 2,
                deckId: "clothes_wearables",
                category: "Clothes & Wearables",
                difficulty: "Easy",
                clue: "Guess the garment worn on the upper body with sleeves and buttons down the front.",
                options: ["Shirt", "Pants", "Socks", "Shoes"],
                correctAnswer: "Shirt",
                emoji: "👕"
            ),
            PracticeCard(
                id: 3,
                deckId: "clothes_wearables",
                category: "Clothes & Wearables",
                difficulty: "Easy",
                clue: "Guess the sturdy footwear worn over socks to protect your feet when walking.",
                options: ["Shoes", "Gloves", "Hat", "Coat"],
                correctAnswer: "Shoes",
                emoji: "👟"
            ),
            PracticeCard(
                id: 4,
                deckId: "clothes_wearables",
                category: "Clothes & Wearables",
                difficulty: "Easy",
                clue: "Guess the soft knitted coverings worn on your feet inside your shoes.",
                options: ["Socks", "Gloves", "Scarf", "Slippers"],
                correctAnswer: "Socks",
                emoji: "🧦"
            ),
            PracticeCard(
                id: 5,
                deckId: "clothes_wearables",
                category: "Clothes & Wearables",
                difficulty: "Easy",
                clue: "Guess the warm outer coat worn over clothes on cool or windy days.",
                options: ["Jacket", "T-shirt", "Vest", "Belt"],
                correctAnswer: "Jacket",
                emoji: "🧥"
            ),
            PracticeCard(
                id: 6,
                deckId: "clothes_wearables",
                category: "Clothes & Wearables",
                difficulty: "Easy",
                clue: "Guess the small timepiece strapped around the wrist to tell the hours and minutes.",
                options: ["Watch", "Ring", "Bracelet", "Necklace"],
                correctAnswer: "Watch",
                emoji: "⌚"
            ),
            PracticeCard(
                id: 7,
                deckId: "clothes_wearables",
                category: "Clothes & Wearables",
                difficulty: "Easy",
                clue: "Guess the optical frames with clear lenses worn over the eyes to help reading.",
                options: ["Glasses", "Sunglasses", "Goggles", "Binoculars"],
                correctAnswer: "Glasses",
                emoji: "👓"
            ),
            PracticeCard(
                id: 8,
                deckId: "clothes_wearables",
                category: "Clothes & Wearables",
                difficulty: "Easy",
                clue: "Guess the soft length of knitted fabric wrapped around the neck for winter warmth.",
                options: ["Scarf", "Tie", "Belt", "Collar"],
                correctAnswer: "Scarf",
                emoji: "🧣"
            )
        ]
    )
    public static let natureWeatherDeck = PracticeDeck(
        id: "nature_weather",
        title: "Nature & Weather",
        categoryLabel: "Nature & Weather",
        difficulty: "Easy",
        emoji: "🌳",
        cards: [
            PracticeCard(
                id: 1,
                deckId: "nature_weather",
                category: "Nature & Weather",
                difficulty: "Easy",
                clue: "Guess the bright golden star that warms the earth and lights up daytime.",
                options: ["Sun", "Moon", "Cloud", "Star"],
                correctAnswer: "Sun",
                emoji: "☀️"
            ),
            PracticeCard(
                id: 2,
                deckId: "nature_weather",
                category: "Nature & Weather",
                difficulty: "Easy",
                clue: "Guess the refreshing water droplets falling from grey clouds onto the garden.",
                options: ["Rain", "Snow", "Fog", "Hail"],
                correctAnswer: "Rain",
                emoji: "🌧️"
            ),
            PracticeCard(
                id: 3,
                deckId: "nature_weather",
                category: "Nature & Weather",
                difficulty: "Easy",
                clue: "Guess the soft white frozen flakes that gently flutter down on a winter day.",
                options: ["Snow", "Frost", "Ice", "Hail"],
                correctAnswer: "Snow",
                emoji: "❄️"
            ),
            PracticeCard(
                id: 4,
                deckId: "nature_weather",
                category: "Nature & Weather",
                difficulty: "Easy",
                clue: "Guess the colorful arch of red, green, and blue that appears in the sky after a shower.",
                options: ["Rainbow", "Sunset", "Aurora", "Comet"],
                correctAnswer: "Rainbow",
                emoji: "🌈"
            ),
            PracticeCard(
                id: 5,
                deckId: "nature_weather",
                category: "Nature & Weather",
                difficulty: "Easy",
                clue: "Guess the tall woodland plant with a strong wooden trunk, branches, and green leaves.",
                options: ["Tree", "Bush", "Grass", "Flower"],
                correctAnswer: "Tree",
                emoji: "🌳"
            ),
            PracticeCard(
                id: 6,
                deckId: "nature_weather",
                category: "Nature & Weather",
                difficulty: "Easy",
                clue: "Guess the winding stream of freshwater flowing across the land into the sea.",
                options: ["River", "Pond", "Lake", "Ocean"],
                correctAnswer: "River",
                emoji: "🌊"
            ),
            PracticeCard(
                id: 7,
                deckId: "nature_weather",
                category: "Nature & Weather",
                difficulty: "Easy",
                clue: "Guess the fluffy white or grey mass floating high up in the blue sky.",
                options: ["Cloud", "Fog", "Smoke", "Mist"],
                correctAnswer: "Cloud",
                emoji: "☁️"
            ),
            PracticeCard(
                id: 8,
                deckId: "nature_weather",
                category: "Nature & Weather",
                difficulty: "Easy",
                clue: "Guess the glowing celestial sphere that brightens the night sky in changing phases.",
                options: ["Moon", "Sun", "Star", "Planet"],
                correctAnswer: "Moon",
                emoji: "🌙"
            )
        ]
    )
    public static let dailyRoutinesDeck = PracticeDeck(
        id: "daily_routines",
        title: "Daily Routines",
        categoryLabel: "Daily Routines",
        difficulty: "Easy",
        emoji: "⏰",
        cards: [
            PracticeCard(
                id: 1,
                deckId: "daily_routines",
                category: "Daily Routines",
                difficulty: "Easy",
                clue: "Guess the early time of day when the sun rises and a brand-new day begins.",
                options: ["Morning", "Afternoon", "Night", "Evening"],
                correctAnswer: "Morning",
                emoji: "🌅"
            ),
            PracticeCard(
                id: 2,
                deckId: "daily_routines",
                category: "Daily Routines",
                difficulty: "Easy",
                clue: "Guess the restful evening hour when you tuck into bed to sleep.",
                options: ["Bedtime", "Noon", "Morning", "Breakfast"],
                correctAnswer: "Bedtime",
                emoji: "🛏️"
            ),
            PracticeCard(
                id: 3,
                deckId: "daily_routines",
                category: "Daily Routines",
                difficulty: "Easy",
                clue: "Guess the first meal of the day eaten soon after waking up in the morning.",
                options: ["Breakfast", "Lunch", "Dinner", "Snack"],
                correctAnswer: "Breakfast",
                emoji: "🥞"
            ),
            PracticeCard(
                id: 4,
                deckId: "daily_routines",
                category: "Daily Routines",
                difficulty: "Easy",
                clue: "Guess the relaxing wash in warm soapy water to feel refreshed and clean.",
                options: ["Bath", "Nap", "Swim", "Wash"],
                correctAnswer: "Bath",
                emoji: "🛁"
            ),
            PracticeCard(
                id: 5,
                deckId: "daily_routines",
                category: "Daily Routines",
                difficulty: "Easy",
                clue: "Guess the gentle outdoor exercise of stepping along a quiet park or garden path.",
                options: ["Walking", "Running", "Driving", "Resting"],
                correctAnswer: "Walking",
                emoji: "🚶"
            ),
            PracticeCard(
                id: 6,
                deckId: "daily_routines",
                category: "Daily Routines",
                difficulty: "Easy",
                clue: "Guess the quiet enjoyable pastime of looking at words in a book or magazine.",
                options: ["Reading", "Writing", "Drawing", "Talking"],
                correctAnswer: "Reading",
                emoji: "📖"
            ),
            PracticeCard(
                id: 7,
                deckId: "daily_routines",
                category: "Daily Routines",
                difficulty: "Easy",
                clue: "Guess the comforting activity of preparing a warm homemade meal in the kitchen.",
                options: ["Cooking", "Cleaning", "Baking", "Shopping"],
                correctAnswer: "Cooking",
                emoji: "🍳"
            ),
            PracticeCard(
                id: 8,
                deckId: "daily_routines",
                category: "Daily Routines",
                difficulty: "Easy",
                clue: "Guess the calming hobby of tending to flowers, potted herbs, and garden plants.",
                options: ["Gardening", "Sewing", "Painting", "Knitting"],
                correctAnswer: "Gardening",
                emoji: "🪴"
            )
        ]
    )

    public static let thematicDecks: [PracticeDeck] = [
        fruitsDeck,
        vegetablesDeck,
        animalsDeck,
        dailyObjectsDeck,
        foodDrinksDeck,
        colorsShapesDeck,
        clothesWearablesDeck,
        natureWeatherDeck,
        dailyRoutinesDeck
    ]

    /// Aggregate mixed deck containing cards across all thematic decks (86 cards total)
    public static let mixedAllDeck: PracticeDeck = {
        let allThematicCards = thematicDecks.flatMap { $0.cards }
        let reindexedCards = allThematicCards.enumerated().map { index, card in
            PracticeCard(
                id: index + 1,
                deckId: deckMixedAll,
                category: card.category,
                difficulty: card.difficulty,
                clue: card.clue,
                options: card.options,
                correctAnswer: card.correctAnswer,
                emoji: card.emoji
            )
        }
        return PracticeDeck(
            id: deckMixedAll,
            title: "Mixed All Decks",
            categoryLabel: "All Decks Mix",
            difficulty: "Easy",
            emoji: "🎲",
            cards: reindexedCards
        )
    }()

    public static let allDecks: [PracticeDeck] = thematicDecks + [mixedAllDeck]

    public init() {}

    public func getAllDecks() -> [PracticeDeck] {
        Self.allDecks
    }

    public func getThematicDecks() -> [PracticeDeck] {
        Self.thematicDecks
    }

    public func getDeck(id: String) -> PracticeDeck? {
        Self.allDecks.first { $0.id.caseInsensitiveCompare(id) == .orderedSame }
    }

    public func getDefaultDeck() -> PracticeDeck {
        getDeck(id: Self.defaultDeckId) ?? Self.fruitsDeck
    }
}
