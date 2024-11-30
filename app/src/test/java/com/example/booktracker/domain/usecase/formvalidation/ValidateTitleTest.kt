package com.example.booktracker.domain.usecase.formvalidation

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class ValidateTitleTest {
    private lateinit var validateTitle: ValidateTitle

    @Before
    fun setUp() {
        validateTitle = ValidateTitle()
    }
    @Test
    fun titleIsEmptyReturnsError() {
        val result = validateTitle.execute("")
        assertEquals(result.successful, false)
    }
    @Test
    fun titlePassedReturnsSuccess() {
        val result = validateTitle.execute("Name Passed")
        assertEquals(result.successful, true)
    }
}