package com.example.booktracker.domain.repository

import com.example.booktracker.domain.model.BookDomain
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun getAllBooks(): Flow<List<BookDomain>>
    suspend fun insertBook(book: BookDomain)
    suspend fun updateBook(book: BookDomain)
    suspend fun retrieveBookById(bookId: Int): BookDomain
    suspend fun deleteBook(book: BookDomain)
}