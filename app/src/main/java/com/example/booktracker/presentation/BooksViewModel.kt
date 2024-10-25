package com.example.booktracker.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracker.domain.model.BookDomain
import com.example.booktracker.domain.usecase.DeleteBookUseCase
import com.example.booktracker.domain.usecase.GetAllBooksUseCase
import com.example.booktracker.domain.usecase.InsertBookUseCase
import com.example.booktracker.domain.usecase.RetrieveBookByIdUseCase
import com.example.booktracker.presentation.books.BookState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val insertBookUseCase: InsertBookUseCase,
    private val retrieveBookByIdUseCase: RetrieveBookByIdUseCase,
    private val deleteBookUseCase: DeleteBookUseCase
) : ViewModel() {

    private val _state = MutableSharedFlow<BookState>()
    val state: SharedFlow<BookState> = _state

    val selectedBook: MutableLiveData<BookDomain> by lazy {
        MutableLiveData<BookDomain>()
    }

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
                _state.emit(
                    if (books.isEmpty()) {
                        BookState.Empty
                    } else {
                        BookState.Success(books)
                    }
                )
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

        getAllBooks()
    }

    fun retrieveBookById(bookId: Int) = viewModelScope.launch {
        selectedBook.value = retrieveBookByIdUseCase(bookId)
    }

    fun deleteBook(book: BookDomain) = viewModelScope.launch {
        deleteBookUseCase(book)
        getAllBooks()
    }

}