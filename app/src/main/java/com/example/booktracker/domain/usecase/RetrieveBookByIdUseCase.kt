package com.example.booktracker.domain.usecase

import com.example.booktracker.domain.model.BookDomain
import com.example.booktracker.domain.repository.BookRepository
import javax.inject.Inject

class RetrieveBookByIdUseCase @Inject constructor(private val repository: BookRepository) {
    suspend operator fun invoke(bookId: Int): BookDomain = repository.retrieveBookById(bookId)
}