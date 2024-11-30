package com.example.booktracker.presentation.dialogBook

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracker.domain.model.BookDomain
import com.example.booktracker.domain.usecase.formvalidation.ValidateAuthor
import com.example.booktracker.domain.usecase.formvalidation.ValidateIsbn
import com.example.booktracker.domain.usecase.formvalidation.ValidatePublicationYear
import com.example.booktracker.domain.usecase.formvalidation.ValidateTitle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialogBookViewModel @Inject constructor(
    private val validateTitle: ValidateTitle,
    private val validateAuthor: ValidateAuthor,
    private val validateIsbn: ValidateIsbn,
    private val validatePublicationYear: ValidatePublicationYear
) : ViewModel() {
    private val _state = MutableStateFlow(BookDialogFormState())
    var state: StateFlow<BookDialogFormState> = _state

    private val validationEventChanel = Channel<ValidationEvent>()
    val validationEvents = validationEventChanel.receiveAsFlow()

    fun loadBook(book: BookDomain) {
        _state.value = _state.value.copy(
            title = book.title,
            author = book.author,
            publicationYear = book.publicationYear?.toString() ?: "",
            isbn = book.isbn ?: ""
        )
    }

    fun onEvent(event: BookFormEvent) {
        when(event) {
            is BookFormEvent.TitleChanged -> {
                _state.value = _state.value.copy(title = event.title)
            }
            is BookFormEvent.AuthorChanged -> {
                _state.value = _state.value.copy(author = event.author)
            }
            is BookFormEvent.PublicationYearChanged -> {
                _state.value = _state.value.copy(publicationYear = event.publicationYear)
            }
            is BookFormEvent.IsbnChanged -> {
                _state.value = _state.value.copy(isbn = event.isbn)
            }

            is BookFormEvent.submit -> {
                submitForm()
            }
        }
    }

    private fun submitForm() {

        val titleResult = validateTitle.execute(_state.value.title)
        val authorResult = validateAuthor.execute(_state.value.author)
        val publicationYearResult = validatePublicationYear.execute(_state.value.publicationYear)
        val isbnResult = validateIsbn.execute(_state.value.isbn)

        val hasError = listOf(
            titleResult,
            authorResult,
            publicationYearResult,
            isbnResult
        ).any { !it.successful }

        if(hasError) {
            _state.value = _state.value.copy(
                titleError = titleResult.errorMessage,
                authorError = authorResult.errorMessage,
                publicationYearError = publicationYearResult.errorMessage,
                isbnError = isbnResult.errorMessage,

            )
            Log.d("DialogBookViewModel", "state value: ${state.value}")
            return
        }
        viewModelScope.launch {
            validationEventChanel.send(ValidationEvent.Success)
        }
    }

    sealed class ValidationEvent {
        object Success: ValidationEvent()
    }

}