package com.example.booktracker.domain.usecase.formvalidation

import java.util.Calendar
import javax.inject.Inject

class ValidatePublicationYear @Inject constructor() {
    fun execute(publicationYear: String): ValidationResult {
        if (publicationYear.isNotBlank()) {
            val publicationYearInt = publicationYear.toInt()
            val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)
            if (publicationYearInt > currentYear) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "Invalid year value"
                )
            }
        }

        return ValidationResult(
            successful = true
        )
    }
}