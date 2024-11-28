package com.example.booktracker.domain.usecase.formvalidation

data class ValidationResult (
    val successful: Boolean,
    val errorMessage: String? = null
)