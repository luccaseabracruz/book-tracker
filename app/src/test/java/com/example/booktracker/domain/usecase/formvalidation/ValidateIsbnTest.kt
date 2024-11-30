package com.example.booktracker.domain.usecase.formvalidation

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class ValidateIsbnTest {
    private lateinit var validateIsbn: ValidateIsbn

    @Before
    fun setUp() {
        validateIsbn = ValidateIsbn()
    }

    @Test
    fun whiteSpacesReturnsError() {
        val result = validateIsbn.execute("85732 87527")
        assertEquals(result.successful, false)
    }

    @Test
    fun invalidSizeReturnsError() {
        val smallerSizeResult = validateIsbn.execute("87527")
        assertEquals(smallerSizeResult.successful, false)

        val biggerSizeResult = validateIsbn.execute("8573224755187527")
        assertEquals(biggerSizeResult.successful, false)
    }

    @Test
    fun isbnHasLettersReturnsError() {
        val resultIsbn10 = validateIsbn.execute("857328752x")
        assertEquals(resultIsbn10.successful, false)

        val resultIsbn13 = validateIsbn.execute("978-857328752a")
        assertEquals(resultIsbn13.successful, false)
    }

    @Test
    fun acceptsXWhenIsbn10() {
        val resultIsbn10 = validateIsbn.execute("857328752-X")
        assertEquals(resultIsbn10.successful, true)

        val resultIsbn13 = validateIsbn.execute("978-857328752-X")
        assertEquals(resultIsbn13.successful, false)
    }

    @Test
    fun specialCharactersReturnsError() {
        val result = validateIsbn.execute("857328752-!")
        assertEquals(result.successful, false)
    }
}