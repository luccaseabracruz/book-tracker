package com.example.booktracker.domain.usecase.formvalidation

import javax.inject.Inject

class ValidateAuthor @Inject constructor() {
    fun execute(author: String): ValidationResult {
        if(author.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Author can't be blank"
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}