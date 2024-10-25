package com.example.booktracker.domain.usecase

import com.example.booktracker.domain.model.BookDomain
import com.example.booktracker.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllBooksUseCase @Inject constructor(private val repository: BookRepository){
    suspend operator fun invoke(): Flow<List<BookDomain>> = repository.getAllBooks()
}