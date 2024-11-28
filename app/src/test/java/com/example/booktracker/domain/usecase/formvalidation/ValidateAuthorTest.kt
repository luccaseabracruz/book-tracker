package com.example.booktracker.domain.usecase.formvalidation

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class ValidateAuthorTest {
    private lateinit var validateAuthor: ValidateAuthor

    @Before
    fun setUp() {
        validateAuthor = ValidateAuthor()
    }
    @Test
    fun authorIsEmptyReturnsError() {
        val result = validateAuthor.execute("")
        assertEquals(result.successful, false)
    }
    @Test
    fun authorPassedNoError() {
        val result = validateAuthor.execute("Name Passed")
        assertEquals(result.successful, true)
    }
}