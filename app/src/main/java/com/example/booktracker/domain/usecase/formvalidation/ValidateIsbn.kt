package com.example.booktracker.domain.usecase.formvalidation

import javax.inject.Inject

class ValidateIsbn @Inject constructor() {
    fun execute(isbn: String): ValidationResult {

        if (isbn.isNotBlank()) {
            val containsWhiteSpaces = isbn.contains(" ")
            if (containsWhiteSpaces) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "It should not contain whitespaces"
                )
            }
            val cleanedIsbn = isbn.replace("-", "")
            if (cleanedIsbn.length != 10 && cleanedIsbn.length != 13) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "Invalid ISBN length"
                )
            }
            val containsLetters = cleanedIsbn.any { it.isLetter() }
            if (containsLetters) {
                if (cleanedIsbn.length == 10 && cleanedIsbn.last() == 'X') {
                    return ValidationResult(successful = true)
                }
                return ValidationResult(
                    successful = false,
                    errorMessage = "It should not contain letters, except 'X'"
                )
            }

            val containsSpecialCharacters = isbn.contains(Regex("[^a-zA-Z0-9-]"))
            if (containsSpecialCharacters) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "No special characters, except '-'"
                )
            }

        }



        return ValidationResult(
            successful = true
        )
    }
}