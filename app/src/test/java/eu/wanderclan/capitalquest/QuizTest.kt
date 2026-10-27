package eu.wanderclan.capitalquest

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class QuizTest {
    @Test fun everyPlaceIsReachableBeforeRepeat() {
        val deck = QuizDeck(Random(42))
        val questions = List(Europe.places.size) { deck.next(Direction.MIXED) }
        assertEquals(Europe.places.size, questions.map { it.place }.toSet().size)
        assertEquals(4, questions.first().options.size)
        questions.forEach { question ->
            assertEquals(4, question.options.toSet().size)
            assertTrue(question.answer in question.options)
        }
    }

    @Test fun bothDirectionsUseTheCorrectPromptAndAnswer() {
        val deck = QuizDeck(Random(5))
        val capitals = deck.next(Direction.COUNTRY_TO_CAPITAL)
        assertEquals(capitals.place.country, capitals.prompt)
        assertEquals(capitals.place.capital, capitals.answer)
        val countries = deck.next(Direction.CAPITAL_TO_COUNTRY)
        assertEquals(countries.place.capital, countries.prompt)
        assertEquals(countries.place.country, countries.answer)
    }

    @Test fun germanQuestionsAndOptionsAreLocalized() {
        val deck = QuizDeck(Random(7))
        repeat(Europe.places.size) {
            val question = deck.next(Direction.COUNTRY_TO_CAPITAL, german = true)
            assertEquals(question.place.countryDe, question.prompt)
            assertEquals(question.place.capitalDe, question.answer)
            assertEquals(4, question.options.toSet().size)
            assertTrue(question.answer in question.options)
        }
        val reverse = deck.next(Direction.CAPITAL_TO_COUNTRY, german = true)
        assertEquals(reverse.place.capitalDe, reverse.prompt)
        assertEquals(reverse.place.countryDe, reverse.answer)
    }

    @Test fun hardAnswersPracticeSpellingInBothLanguages() {
        val uk = Europe.places.first { it.country == "United Kingdom" }
        val country = Question(uk, Direction.CAPITAL_TO_COUNTRY, emptyList(), german = true)
        assertTrue(country.matchesTyped("  vereinigtes   königreich  "))
        assertTrue(!country.matchesTyped("Vereinigtes Konigreich"))
        val capital = Question(Europe.places.first { it.country == "Ukraine" }, Direction.COUNTRY_TO_CAPITAL, emptyList(), german = true)
        assertEquals("Kiew", capital.answer)
        assertTrue(capital.matchesTyped("kiew"))
        assertTrue(capital.matchesTyped("Kyjiw"))
        assertTrue(capital.matchesTyped("Kyiv"))
        assertTrue(capital.matchesTyped("Kyïv"))
        assertTrue(!capital.matchesTyped("Kievv"))
        val english = Question(uk, Direction.CAPITAL_TO_COUNTRY, emptyList())
        assertTrue(english.matchesTyped(" united kingdom "))
        assertTrue(!english.matchesTyped("united kindgom"))
    }

    @Test fun flagsAppearOnlyBesideCountriesInBothLanguages() {
        val microstates = listOf("Andorra", "San Marino", "Liechtenstein")
        assertTrue(Europe.places.map { it.country }.containsAll(microstates))
        assertEquals("🇦🇩", Europe.places.first { it.country == "Andorra" }.flag)
        assertEquals(Europe.places.size, Europe.places.map { it.iso }.toSet().size)
        for (german in listOf(false, true)) {
            val deck = QuizDeck(Random(11))
            repeat(Europe.places.size) {
                val question = deck.next(Direction.COUNTRY_TO_CAPITAL, german)
                assertTrue(question.displayPrompt.startsWith(question.place.flag))
                question.options.forEach { assertEquals(it, question.displayOption(it)) }
            }
            repeat(Europe.places.size) {
                val question = deck.next(Direction.CAPITAL_TO_COUNTRY, german)
                assertEquals(question.prompt, question.displayPrompt)
                question.options.forEach { assertTrue(question.displayOption(it).endsWith("  $it")) }
                assertTrue(question.displayOption(question.answer).startsWith(question.place.flag))
            }
        }
    }

    @Test fun datasetIncludesNonEuAndTranscontinentalStates() {
        val names = Europe.places.map { it.country }
        assertEquals(51, names.size)
        assertEquals(names.size, names.toSet().size)
        assertEquals(names.size, Europe.places.map { it.countryDe }.toSet().size)
        assertTrue(names.containsAll(listOf("United Kingdom", "Norway", "Switzerland", "Ukraine", "Russia", "Türkiye", "Kazakhstan", "Cyprus", "Kosovo", "Vatican City")))
    }
}
