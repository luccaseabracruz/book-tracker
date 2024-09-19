package com.example.booktracker.domain.usecase

import com.example.booktracker.domain.repository.BookRepository

class getAllBooksUseCase constructor(private val repository: BookRepository){
    suspend operator fun invoke() = repository.getAllBooks()
}