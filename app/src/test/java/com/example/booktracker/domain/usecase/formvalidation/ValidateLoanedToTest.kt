package com.example.booktracker.domain.usecase.formvalidation

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class ValidateLoanedToTest {
    private lateinit var validateLoanedTo: ValidateLoanedTo

    @Before
    fun setUp() {
        validateLoanedTo = ValidateLoanedTo()
    }

    @Test
    fun borrowerIsEmptyReturnsError() {
        val result = validateLoanedTo.execute("")
        assertEquals(result.successful, false)
    }
}