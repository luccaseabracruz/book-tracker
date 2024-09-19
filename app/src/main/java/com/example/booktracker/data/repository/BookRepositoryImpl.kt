package com.example.booktracker.data.repository

import com.example.booktracker.data.dao.BookDao
import com.example.booktracker.data.entity.Book
import com.example.booktracker.data.entity.BookEntity
import com.example.booktracker.data.mapper.toDomain
import com.example.booktracker.domain.model.BookDomain
import com.example.booktracker.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookRepositoryImpl(private val dao: BookDao): BookRepository {
    override suspend fun getAllBooks(): List<BookDomain> = withContext(Dispatchers.IO) {
        dao.getAllBooks().map { book: BookEntity ->
            book.toDomain()
        }
    }

    override suspend fun insertBook(book: BookDomain) {
        TODO("Not yet implemented")
    }
}