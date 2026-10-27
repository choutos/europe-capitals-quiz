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

    @Test fun datasetIncludesNonEuAndTranscontinentalStates() {
        val names = Europe.places.map { it.country }
        assertEquals(names.size, names.toSet().size)
        assertTrue(names.containsAll(listOf("United Kingdom", "Norway", "Switzerland", "Ukraine", "Russia", "Türkiye", "Kazakhstan", "Cyprus", "Kosovo", "Vatican City")))
    }
}
