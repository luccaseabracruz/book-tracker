package com.example.booktracker.domain.model

typealias BookDomain = Book

data class Book (
    val id: Int,
    val title: String,
    val author: String,
    val publicationYear: Int?,
    val isbn: String?,
    val loanedTo: String?,
    val loanDate: String?,
    val returnDate: String?,
)