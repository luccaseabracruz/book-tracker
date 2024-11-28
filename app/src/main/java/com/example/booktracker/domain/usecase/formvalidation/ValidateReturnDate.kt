package com.example.booktracker.domain.usecase.formvalidation

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class ValidateReturnDate @Inject constructor() {
    fun execute(returnDate: String): ValidationResult {

        if (returnDate.isNotBlank()) {
            val dateFormatRegex = Regex("^\\d{2}/\\d{2}/\\d{4}\$")
            if (!dateFormatRegex.matches(returnDate)) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "Invalid date format. Use dd/MM/yyyy"
                )
            }
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val parsedReturnDate = try {
                dateFormat.parse(returnDate)
            } catch (e: Exception) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "Invalid date value"
                )
            }

            val currentDate = Calendar.getInstance().time
            if(parsedReturnDate.before(currentDate)) {
                return ValidationResult(
                    successful = false,
                    errorMessage = " Return date must not be in the past"
                )
            }

        }


        return ValidationResult(
            successful = true
        )
    }
}