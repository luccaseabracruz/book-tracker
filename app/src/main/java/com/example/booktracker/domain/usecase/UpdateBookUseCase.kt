package com.example.booktracker.domain.usecase

import com.example.booktracker.domain.model.BookDomain
import com.example.booktracker.domain.repository.BookRepository

class UpdateBookUseCase constructor(private val repository: BookRepository) {
    suspend operator fun invoke(book: BookDomain) = repository.updateBook(book)
}