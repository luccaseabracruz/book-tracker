package com.example.booktracker.domain.usecase.formvalidation

import javax.inject.Inject

class ValidateLoanedTo @Inject constructor() {
    fun execute(loanedTo: String): ValidationResult {
        if(loanedTo.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Borrower name can't be blank"
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}