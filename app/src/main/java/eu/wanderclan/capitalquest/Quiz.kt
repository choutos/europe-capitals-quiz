package eu.wanderclan.capitalquest

import kotlin.random.Random

data class Place(val country: String, val capital: String)

enum class Direction { MIXED, COUNTRY_TO_CAPITAL, CAPITAL_TO_COUNTRY }

data class Question(val place: Place, val direction: Direction, val options: List<String>) {
    val answer: String get() = if (direction == Direction.COUNTRY_TO_CAPITAL) place.capital else place.country
    val prompt: String get() = if (direction == Direction.COUNTRY_TO_CAPITAL) place.country else place.capital
}

/** Sovereign states wholly or partly in Europe, plus Cyprus and Kosovo. */
object Europe {
    val places = listOf(
        Place("Albania", "Tirana"),
        Place("Andorra", "Andorra la Vella"),
        Place("Armenia", "Yerevan"),
        Place("Austria", "Vienna"),
        Place("Azerbaijan", "Baku"),
        Place("Belarus", "Minsk"),
        Place("Belgium", "Brussels"),
        Place("Bosnia and Herzegovina", "Sarajevo"),
        Place("Bulgaria", "Sofia"),
        Place("Croatia", "Zagreb"),
        Place("Cyprus", "Nicosia"),
        Place("Czechia", "Prague"),
        Place("Denmark", "Copenhagen"),
        Place("Estonia", "Tallinn"),
        Place("Finland", "Helsinki"),
        Place("France", "Paris"),
        Place("Georgia", "Tbilisi"),
        Place("Germany", "Berlin"),
        Place("Greece", "Athens"),
        Place("Hungary", "Budapest"),
        Place("Iceland", "Reykjavík"),
        Place("Ireland", "Dublin"),
        Place("Italy", "Rome"),
        Place("Kazakhstan", "Astana"),
        Place("Kosovo", "Pristina"),
        Place("Latvia", "Riga"),
        Place("Liechtenstein", "Vaduz"),
        Place("Lithuania", "Vilnius"),
        Place("Luxembourg", "Luxembourg"),
        Place("Malta", "Valletta"),
        Place("Moldova", "Chișinău"),
        Place("Monaco", "Monaco"),
        Place("Montenegro", "Podgorica"),
        Place("Netherlands", "Amsterdam"),
        Place("North Macedonia", "Skopje"),
        Place("Norway", "Oslo"),
        Place("Poland", "Warsaw"),
        Place("Portugal", "Lisbon"),
        Place("Romania", "Bucharest"),
        Place("Russia", "Moscow"),
        Place("San Marino", "San Marino"),
        Place("Serbia", "Belgrade"),
        Place("Slovakia", "Bratislava"),
        Place("Slovenia", "Ljubljana"),
        Place("Spain", "Madrid"),
        Place("Sweden", "Stockholm"),
        Place("Switzerland", "Bern"),
        Place("Türkiye", "Ankara"),
        Place("Ukraine", "Kyiv"),
        Place("United Kingdom", "London"),
        Place("Vatican City", "Vatican City"),
    )
}

class QuizDeck(private val random: Random = Random.Default) {
    private var remaining = emptyList<Place>()

    fun next(direction: Direction): Question {
        if (remaining.isEmpty()) remaining = Europe.places.shuffled(random)
        val place = remaining.first()
        remaining = remaining.drop(1)
        val actualDirection = if (direction == Direction.MIXED) {
            if (random.nextBoolean()) Direction.COUNTRY_TO_CAPITAL else Direction.CAPITAL_TO_COUNTRY
        } else direction
        val others = Europe.places.asSequence().filter { it != place }
            .map { if (actualDirection == Direction.COUNTRY_TO_CAPITAL) it.capital else it.country }
            .distinct().shuffled(random).take(3).toList()
        val answer = if (actualDirection == Direction.COUNTRY_TO_CAPITAL) place.capital else place.country
        return Question(place, actualDirection, (others + answer).shuffled(random))
    }
}
