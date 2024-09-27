package com.example.booktracker.presentation.books

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.booktracker.data.db
import com.example.booktracker.data.repository.BookRepositoryImpl
import com.example.booktracker.domain.model.BookDomain
import com.example.booktracker.domain.usecase.GetAllBooksUseCase
import com.example.booktracker.domain.usecase.InsertBookUseCase
import kotlinx.coroutines.launch

class BooksViewModel(
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val insertBookUseCase: InsertBookUseCase
) : ViewModel() {

    val state: LiveData<BookState> = liveData {
        emit(BookState.Loading)

        val state = try {

            val books = getAllBooksUseCase()

            if (books.isEmpty()) {
                BookState.Empty
            } else {
                BookState.Success(books)
            }

        } catch (exception: Exception) {
            val errorMessage: String = exception.message.toString()

            Log.e("Error", errorMessage)
            BookState.Error(errorMessage)
        }

        emit(state)
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
                insertBookUseCase = InsertBookUseCase(repository)
            ) as T
        }
    }

}