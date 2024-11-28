package com.example.booktracker.presentation.dialogBook

sealed class BookFormEvent {
    data class TitleChanged(val title: String): BookFormEvent()
    data class AuthorChanged(val author: String): BookFormEvent()
    data class PublicationYearChanged(val publicationYear: String): BookFormEvent()
    data class IsbnChanged(val isbn: String): BookFormEvent()

    object submit: BookFormEvent()
}