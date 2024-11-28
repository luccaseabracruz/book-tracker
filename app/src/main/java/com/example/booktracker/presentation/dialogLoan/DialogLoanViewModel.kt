package com.example.booktracker.presentation.dialogLoan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracker.domain.usecase.formvalidation.ValidateLoanedTo
import com.example.booktracker.domain.usecase.formvalidation.ValidateReturnDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialogLoanViewModel @Inject constructor(
    private val validateLoanedTo: ValidateLoanedTo,
    private val validateReturnDate: ValidateReturnDate
) : ViewModel() {
    private val _state = MutableStateFlow(LoanDialogFormState())
    var state: StateFlow<LoanDialogFormState> = _state

    private val validationEventChanel = Channel<ValidationEvent>()
    val validationEvents = validationEventChanel.receiveAsFlow()

    fun onEvent(event: LoanFormEvent) {
        when(event) {
            is LoanFormEvent.LoanedToChanged -> {
                _state.value = _state.value.copy(loanedTo = event.loanedTo)
            }
            is LoanFormEvent.ReturnDateChanged -> {
                _state.value = _state.value.copy(returnDate = event.returnDate)
            }

            is LoanFormEvent.submit -> {
                submitForm()
            }
        }
    }

    private fun submitForm() {

        val loanedToResult = validateLoanedTo.execute(_state.value.loanedTo)
        val returnDateResult = validateReturnDate.execute(_state.value.returnDate)

        val hasError = listOf(
            loanedToResult,
            returnDateResult
        ).any { !it.successful }

        if(hasError) {
            _state.value = _state.value.copy(
                loanedToError = loanedToResult.errorMessage,
                returnDateError = returnDateResult.errorMessage,
            )
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