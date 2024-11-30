package com.example.booktracker.presentation.dialogBook

data class BookDialogFormState(
    val title: String = "",
    val titleError: String? = null,
    val author: String = "",
    val authorError: String? = null,
    val publicationYear: String = "",
    val publicationYearError: String? = null,
    val isbn: String = "",
    val isbnError: String? = null,
)
