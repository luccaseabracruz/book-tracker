package com.example.booktracker.domain.usecase

import com.example.booktracker.domain.model.BookDomain
import com.example.booktracker.domain.repository.BookRepository
import javax.inject.Inject

class InsertBookUseCase @Inject constructor(private val repository: BookRepository) {
    suspend operator fun invoke(book: BookDomain) = repository.insertBook(book)
}