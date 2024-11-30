package com.example.booktracker.domain.usecase.formvalidation

import javax.inject.Inject

class ValidateTitle @Inject constructor() {
    fun execute(title: String): ValidationResult {
        if(title.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Title can't be blank"
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}