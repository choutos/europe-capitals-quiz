package eu.wanderclan.capitalquest

import kotlin.random.Random

data class Place(val country: String, val capital: String, val countryDe: String = country, val capitalDe: String = capital)

enum class Direction { MIXED, COUNTRY_TO_CAPITAL, CAPITAL_TO_COUNTRY }

data class Question(val place: Place, val direction: Direction, val options: List<String>, val german: Boolean = false) {
    val answer: String get() = if (direction == Direction.COUNTRY_TO_CAPITAL) { if (german) place.capitalDe else place.capital } else { if (german) place.countryDe else place.country }
    val prompt: String get() = if (direction == Direction.COUNTRY_TO_CAPITAL) { if (german) place.countryDe else place.country } else { if (german) place.capitalDe else place.capital }
}

/** Sovereign states wholly or partly in Europe, plus Cyprus and Kosovo. */
object Europe {
    val places = listOf(
        Place("Albania", "Tirana", "Albanien", "Tirana"),
        Place("Andorra", "Andorra la Vella"),
        Place("Armenia", "Yerevan", "Armenien", "Yerevan"),
        Place("Austria", "Vienna", "Österreich", "Wien"),
        Place("Azerbaijan", "Baku", "Aserbaidschan", "Baku"),
        Place("Belarus", "Minsk"),
        Place("Belgium", "Brussels", "Belgien", "Brüssel"),
        Place("Bosnia and Herzegovina", "Sarajevo", "Bosnien und Herzegowina", "Sarajevo"),
        Place("Bulgaria", "Sofia", "Bulgarien", "Sofia"),
        Place("Croatia", "Zagreb", "Kroatien", "Zagreb"),
        Place("Cyprus", "Nicosia", "Zypern", "Nicosia"),
        Place("Czechia", "Prague", "Tschechien", "Prag"),
        Place("Denmark", "Copenhagen", "Dänemark", "Kopenhagen"),
        Place("Estonia", "Tallinn", "Estland", "Tallinn"),
        Place("Finland", "Helsinki", "Finnland", "Helsinki"),
        Place("France", "Paris", "Frankreich", "Paris"),
        Place("Georgia", "Tbilisi", "Georgien", "Tbilisi"),
        Place("Germany", "Berlin", "Deutschland", "Berlin"),
        Place("Greece", "Athens", "Griechenland", "Athen"),
        Place("Hungary", "Budapest", "Ungarn", "Budapest"),
        Place("Iceland", "Reykjavík", "Island", "Reykjavík"),
        Place("Ireland", "Dublin", "Irland", "Dublin"),
        Place("Italy", "Rome", "Italien", "Rom"),
        Place("Kazakhstan", "Astana", "Kasachstan", "Astana"),
        Place("Kosovo", "Pristina"),
        Place("Latvia", "Riga", "Lettland", "Riga"),
        Place("Liechtenstein", "Vaduz"),
        Place("Lithuania", "Vilnius", "Litauen", "Vilnius"),
        Place("Luxembourg", "Luxembourg", "Luxemburg", "Luxemburg"),
        Place("Malta", "Valletta"),
        Place("Moldova", "Chișinău", "Moldau", "Chișinău"),
        Place("Monaco", "Monaco"),
        Place("Montenegro", "Podgorica"),
        Place("Netherlands", "Amsterdam", "Niederlande", "Amsterdam"),
        Place("North Macedonia", "Skopje", "Nordmazedonien", "Skopje"),
        Place("Norway", "Oslo", "Norwegen", "Oslo"),
        Place("Poland", "Warsaw", "Polen", "Warschau"),
        Place("Portugal", "Lisbon", "Portugal", "Lissabon"),
        Place("Romania", "Bucharest", "Rumänien", "Bukarest"),
        Place("Russia", "Moscow", "Russland", "Moskau"),
        Place("San Marino", "San Marino"),
        Place("Serbia", "Belgrade", "Serbien", "Belgrad"),
        Place("Slovakia", "Bratislava", "Slowakei", "Bratislava"),
        Place("Slovenia", "Ljubljana", "Slowenien", "Ljubljana"),
        Place("Spain", "Madrid", "Spanien", "Madrid"),
        Place("Sweden", "Stockholm", "Schweden", "Stockholm"),
        Place("Switzerland", "Bern", "Schweiz", "Bern"),
        Place("Türkiye", "Ankara", "Türkei", "Ankara"),
        Place("Ukraine", "Kyiv", "Ukraine", "Kyjiw"),
        Place("United Kingdom", "London", "Vereinigtes Königreich", "London"),
        Place("Vatican City", "Vatican City", "Vatikanstadt", "Vatikanstadt"),
    )
}

class QuizDeck(private val random: Random = Random.Default) {
    private var remaining = emptyList<Place>()

    fun next(direction: Direction, german: Boolean = false): Question {
        if (remaining.isEmpty()) remaining = Europe.places.shuffled(random)
        val place = remaining.first()
        remaining = remaining.drop(1)
        val actualDirection = if (direction == Direction.MIXED) {
            if (random.nextBoolean()) Direction.COUNTRY_TO_CAPITAL else Direction.CAPITAL_TO_COUNTRY
        } else direction
        val others = Europe.places.asSequence().filter { it != place }
            .map { if (actualDirection == Direction.COUNTRY_TO_CAPITAL) if (german) it.capitalDe else it.capital else if (german) it.countryDe else it.country }
            .distinct().shuffled(random).take(3).toList()
        val answer = if (actualDirection == Direction.COUNTRY_TO_CAPITAL) if (german) place.capitalDe else place.capital else if (german) place.countryDe else place.country
        return Question(place, actualDirection, (others + answer).shuffled(random), german)
    }
}
