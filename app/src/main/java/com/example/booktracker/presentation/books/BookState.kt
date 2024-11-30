package com.example.booktracker.presentation.books

import com.example.booktracker.domain.model.BookDomain

sealed interface BookState {
    object Loading : BookState
    object Empty : BookState
    data class Success(val books: List<BookDomain>) : BookState
    data class Error(val message: String) : BookState
}