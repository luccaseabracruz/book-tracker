package com.example.booktracker.domain.repository

import com.example.booktracker.domain.model.BookDomain

interface BookRepository {
    suspend fun getAllBooks(): List<BookDomain>
    suspend fun insertBook(book: BookDomain)
}