package com.example.booktracker.domain.usecase.formvalidation

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class ValidateReturnDateTest {
    private lateinit var validateReturnDate: ValidateReturnDate

    @Before
    fun setUp() {
        validateReturnDate = ValidateReturnDate()
    }
    @Test
    fun invalidFormatReturnsError() {
        val result1 = validateReturnDate.execute("16.05.2035")
        assertEquals(result1.successful, false)
        val result2 = validateReturnDate.execute("16.13.2035")
        assertEquals(result2.successful, false)
    }
    @Test
    fun pastDateReturnsError() {
        val result = validateReturnDate.execute("16/05/2000")
        assertEquals(result.successful, false)
    }
}