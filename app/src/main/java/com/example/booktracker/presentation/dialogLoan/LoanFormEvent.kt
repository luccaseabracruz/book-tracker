package com.example.booktracker.presentation.dialogLoan

sealed class LoanFormEvent {
    data class LoanedToChanged(val loanedTo: String): LoanFormEvent()
    data class ReturnDateChanged(val returnDate: String): LoanFormEvent()

    object submit: LoanFormEvent()
}