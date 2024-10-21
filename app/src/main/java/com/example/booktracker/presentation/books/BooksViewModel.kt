package com.example.booktracker.presentation.books

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.booktracker.data.db
import com.example.booktracker.data.repository.BookRepositoryImpl
import com.example.booktracker.domain.model.BookDomain
import com.example.booktracker.domain.usecase.GetAllBooksUseCase
import com.example.booktracker.domain.usecase.InsertBookUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class BooksViewModel(
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val insertBookUseCase: InsertBookUseCase
) : ViewModel() {

    private val _state = MutableSharedFlow<BookState>()
    val state: SharedFlow<BookState> = _state

    init {
        getAllBooks()
    }

    private fun getAllBooks() = viewModelScope.launch {
        getAllBooksUseCase()
            .flowOn(Dispatchers.IO)
            .onStart {
                _state.emit(BookState.Loading)
            }.catch {
                _state.emit(BookState.Error("Fail on loanding Books"))
            }.collect { books ->
                if(books.isEmpty()) {
                    _state.emit(BookState.Empty)
                } else {
                    _state.emit(BookState.Success(books))
                }
            }
    }

    fun insertBook(
        title: String,
        author: String,
        publicationYear: Int?,
        isbn: String?,
        loanedTo: String?,
        loanDate: String?,
        returnDate: String?
    ) = viewModelScope.launch {
        insertBookUseCase(
            BookDomain(
                id = 0,
                title = title,
                author = author,
                publicationYear = publicationYear,
                isbn = isbn,
                loanedTo = loanedTo,
                loanDate = loanDate,
                returnDate = returnDate
            )
        )
    }

    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            val repository = BookRepositoryImpl(application.db.bookDao())
            return BooksViewModel(
                getAllBooksUseCase = GetAllBooksUseCase(repository),
                insertBookUseCase = InsertBookUseCase(repository),
            ) as T
        }
    }

}