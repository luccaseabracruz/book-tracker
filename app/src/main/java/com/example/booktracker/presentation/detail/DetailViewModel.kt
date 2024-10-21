package com.example.booktracker.presentation.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.booktracker.data.db
import com.example.booktracker.data.repository.BookRepositoryImpl
import com.example.booktracker.domain.model.BookDomain
import com.example.booktracker.domain.usecase.DeleteBookUseCase
import com.example.booktracker.domain.usecase.RetrieveBookByIdUseCase
import kotlinx.coroutines.launch

class DetailViewModel(
    private val retrieveBookByIdUseCase: RetrieveBookByIdUseCase,
    private val deleteBookUseCase: DeleteBookUseCase
) : ViewModel() {

    val book: MutableLiveData<BookDomain> by lazy {
        MutableLiveData<BookDomain>()
    }

    fun retrieveBookById(bookId: Int) = viewModelScope.launch {
        book.value = retrieveBookByIdUseCase(bookId)
    }

    fun deleteBook(book: BookDomain) = viewModelScope.launch {
        deleteBookUseCase(book)
    }

    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application =
                checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            val repository = BookRepositoryImpl(application.db.bookDao())
            return DetailViewModel(
                retrieveBookByIdUseCase = RetrieveBookByIdUseCase(repository),
                deleteBookUseCase = DeleteBookUseCase(repository)
            ) as T
        }
    }
}