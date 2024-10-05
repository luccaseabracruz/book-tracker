package com.example.booktracker.data.repository

import com.example.booktracker.data.dao.BookDao
import com.example.booktracker.data.entity.Book
import com.example.booktracker.data.entity.BookEntity
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
                book.toDomain() }
        }
    }

    override suspend fun insertBook(book: BookDomain): Unit = withContext(Dispatchers.IO) {
        dao.insertBook(book.toEntity())
    }

    override suspend fun updateBook(book: BookDomain): Unit = withContext(Dispatchers.IO) {
        dao.updateBook(book.toEntity())
    }


}