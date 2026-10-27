package eu.wanderclan.capitalquest

import kotlin.random.Random
import java.text.Normalizer
import java.util.Locale

data class Place(val country: String, val capital: String, val countryDe: String = country, val capitalDe: String = capital, val iso: String) {
    val flag: String get() = iso.map { String(Character.toChars(0x1F1E6 + (it - 'A'))) }.joinToString("")
}

private fun normalized(value: String) = Normalizer.normalize(value.trim(), Normalizer.Form.NFC)
    .replace(Regex("\\s+"), " ").lowercase(Locale.ROOT)

enum class Direction { MIXED, COUNTRY_TO_CAPITAL, CAPITAL_TO_COUNTRY }

data class Question(val place: Place, val direction: Direction, val options: List<String>, val german: Boolean = false) {
    val answer: String get() = if (direction == Direction.COUNTRY_TO_CAPITAL) { if (german) place.capitalDe else place.capital } else { if (german) place.countryDe else place.country }
    val prompt: String get() = if (direction == Direction.COUNTRY_TO_CAPITAL) { if (german) place.countryDe else place.country } else { if (german) place.capitalDe else place.capital }
    fun matchesTyped(input: String): Boolean {
        val value = normalized(input)
        if (german && direction == Direction.COUNTRY_TO_CAPITAL && place.country == "Ukraine") {
            return value in setOf("kiew", "kyjiw", "kyiv", "kyïv")
        }
        return value == normalized(answer)
    }
    val displayPrompt: String get() = if (direction == Direction.COUNTRY_TO_CAPITAL) "${place.flag}  $prompt" else prompt
    fun displayOption(option: String): String = if (direction == Direction.CAPITAL_TO_COUNTRY) {
        val flag = Europe.places.first { (if (german) it.countryDe else it.country) == option }.flag
        "$flag  $option"
    } else option
}

/** Sovereign states wholly or partly in Europe, plus Cyprus and Kosovo. */
object Europe {
    val places = listOf(
        Place("Albania", "Tirana", "Albanien", "Tirana", iso = "AL"),
        Place("Andorra", "Andorra la Vella", iso = "AD"),
        Place("Armenia", "Yerevan", "Armenien", "Jerewan", iso = "AM"),
        Place("Austria", "Vienna", "Österreich", "Wien", iso = "AT"),
        Place("Azerbaijan", "Baku", "Aserbaidschan", "Baku", iso = "AZ"),
        Place("Belarus", "Minsk", iso = "BY"),
        Place("Belgium", "Brussels", "Belgien", "Brüssel", iso = "BE"),
        Place("Bosnia and Herzegovina", "Sarajevo", "Bosnien und Herzegowina", "Sarajevo", iso = "BA"),
        Place("Bulgaria", "Sofia", "Bulgarien", "Sofia", iso = "BG"),
        Place("Croatia", "Zagreb", "Kroatien", "Zagreb", iso = "HR"),
        Place("Cyprus", "Nicosia", "Zypern", "Nikosia", iso = "CY"),
        Place("Czechia", "Prague", "Tschechien", "Prag", iso = "CZ"),
        Place("Denmark", "Copenhagen", "Dänemark", "Kopenhagen", iso = "DK"),
        Place("Estonia", "Tallinn", "Estland", "Tallinn", iso = "EE"),
        Place("Finland", "Helsinki", "Finnland", "Helsinki", iso = "FI"),
        Place("France", "Paris", "Frankreich", "Paris", iso = "FR"),
        Place("Georgia", "Tbilisi", "Georgien", "Tiflis", iso = "GE"),
        Place("Germany", "Berlin", "Deutschland", "Berlin", iso = "DE"),
        Place("Greece", "Athens", "Griechenland", "Athen", iso = "GR"),
        Place("Hungary", "Budapest", "Ungarn", "Budapest", iso = "HU"),
        Place("Iceland", "Reykjavík", "Island", "Reykjavík", iso = "IS"),
        Place("Ireland", "Dublin", "Irland", "Dublin", iso = "IE"),
        Place("Italy", "Rome", "Italien", "Rom", iso = "IT"),
        Place("Kazakhstan", "Astana", "Kasachstan", "Astana", iso = "KZ"),
        Place("Kosovo", "Pristina", iso = "XK"),
        Place("Latvia", "Riga", "Lettland", "Riga", iso = "LV"),
        Place("Liechtenstein", "Vaduz", iso = "LI"),
        Place("Lithuania", "Vilnius", "Litauen", "Vilnius", iso = "LT"),
        Place("Luxembourg", "Luxembourg", "Luxemburg", "Luxemburg", iso = "LU"),
        Place("Malta", "Valletta", iso = "MT"),
        Place("Moldova", "Chișinău", "Moldau", "Chișinău", iso = "MD"),
        Place("Monaco", "Monaco", iso = "MC"),
        Place("Montenegro", "Podgorica", iso = "ME"),
        Place("Netherlands", "Amsterdam", "Niederlande", "Amsterdam", iso = "NL"),
        Place("North Macedonia", "Skopje", "Nordmazedonien", "Skopje", iso = "MK"),
        Place("Norway", "Oslo", "Norwegen", "Oslo", iso = "NO"),
        Place("Poland", "Warsaw", "Polen", "Warschau", iso = "PL"),
        Place("Portugal", "Lisbon", "Portugal", "Lissabon", iso = "PT"),
        Place("Romania", "Bucharest", "Rumänien", "Bukarest", iso = "RO"),
        Place("Russia", "Moscow", "Russland", "Moskau", iso = "RU"),
        Place("San Marino", "San Marino", iso = "SM"),
        Place("Serbia", "Belgrade", "Serbien", "Belgrad", iso = "RS"),
        Place("Slovakia", "Bratislava", "Slowakei", "Bratislava", iso = "SK"),
        Place("Slovenia", "Ljubljana", "Slowenien", "Ljubljana", iso = "SI"),
        Place("Spain", "Madrid", "Spanien", "Madrid", iso = "ES"),
        Place("Sweden", "Stockholm", "Schweden", "Stockholm", iso = "SE"),
        Place("Switzerland", "Bern", "Schweiz", "Bern", iso = "CH"),
        Place("Türkiye", "Ankara", "Türkei", "Ankara", iso = "TR"),
        Place("Ukraine", "Kyiv", "Ukraine", "Kiew", iso = "UA"),
        Place("United Kingdom", "London", "Vereinigtes Königreich", "London", iso = "GB"),
        Place("Vatican City", "Vatican City", "Vatikanstadt", "Vatikanstadt", iso = "VA"),
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
