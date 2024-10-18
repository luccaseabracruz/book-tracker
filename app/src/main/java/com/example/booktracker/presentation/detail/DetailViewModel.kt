package com.example.booktracker.presentation.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.booktracker.data.db
import com.example.booktracker.data.repository.BookRepositoryImpl
import com.example.booktracker.domain.model.BookDomain
import com.example.booktracker.domain.usecase.GetAllBooksUseCase
import com.example.booktracker.domain.usecase.InsertBookUseCase
import com.example.booktracker.domain.usecase.RetrieveBookByIdUseCase
import com.example.booktracker.domain.usecase.UpdateBookUseCase
import com.example.booktracker.presentation.books.BooksViewModel
import kotlinx.coroutines.launch

class DetailViewModel(
    private val retrieveBookByIdUseCase: RetrieveBookByIdUseCase
) : ViewModel() {

    val book: MutableLiveData<BookDomain> by lazy {
        MutableLiveData<BookDomain>()
    }

    fun retrieveBookById(bookId: Int) = viewModelScope.launch {
        book.value = retrieveBookByIdUseCase(bookId)
    }

    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application =
                checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            val repository = BookRepositoryImpl(application.db.bookDao())
            return DetailViewModel(
                retrieveBookByIdUseCase = RetrieveBookByIdUseCase(repository)
            ) as T
        }
    }
}