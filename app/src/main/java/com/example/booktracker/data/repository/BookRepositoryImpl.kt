package com.example.booktracker.data.repository

import com.example.booktracker.data.dao.BookDao
import com.example.booktracker.data.mapper.toDomain
import com.example.booktracker.data.mapper.toEntity
import com.example.booktracker.domain.model.BookDomain
import com.example.booktracker.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class BookRepositoryImpl(private val dao: BookDao) : BookRepository {
    override suspend fun getAllBooks(): Flow<List<BookDomain>> = withContext(Dispatchers.IO) {
        dao.getAllBooks().map { list ->
            list.map { book ->
                book.toDomain()
            }
        }
    }

    override suspend fun insertBook(book: BookDomain): Unit = withContext(Dispatchers.IO) {
        dao.insertBook(book.toEntity())
    }

    override suspend fun updateBook(book: BookDomain): Unit = withContext(Dispatchers.IO) {
        dao.updateBook(book.toEntity())
    }

    override suspend fun retrieveBookById(bookId: Int): BookDomain = withContext(Dispatchers.IO) {
        dao.retrieveBookById(bookId).toDomain()
    }

    override suspend fun deleteBook(book: BookDomain): Unit = withContext(Dispatchers.IO) {
        dao.deleteBook(book.toEntity())
    }


}