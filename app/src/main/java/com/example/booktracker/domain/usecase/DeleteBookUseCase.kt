package com.example.booktracker.domain.usecase

import com.example.booktracker.domain.model.BookDomain
import com.example.booktracker.domain.repository.BookRepository

class DeleteBookUseCase constructor(private val repository: BookRepository) {
    suspend operator fun invoke(book: BookDomain): Unit = repository.deleteBook(book)
}