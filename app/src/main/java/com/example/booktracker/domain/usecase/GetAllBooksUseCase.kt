package com.example.booktracker.domain.usecase

import com.example.booktracker.domain.model.BookDomain
import com.example.booktracker.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllBooksUseCase @Inject constructor(private val repository: BookRepository) {
    suspend operator fun invoke(): Flow<List<BookDomain>> =
        repository.getAllBooks().map { books -> books.sortedBy { it.title.lowercase() } }
}