package com.example.booktracker.domain.usecase.formvalidation

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class ValidatePublicationYearTest {
    private lateinit var validatePublicationYear: ValidatePublicationYear

    @Before
    fun setUp() {
        validatePublicationYear = ValidatePublicationYear()
    }
    @Test
    fun invalidYearReturnsError() {
        val result = validatePublicationYear.execute("5214")
        assertEquals(result.successful, false)
    }
}