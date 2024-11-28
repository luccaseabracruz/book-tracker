package com.example.booktracker.presentation.dialogLoan

data class LoanDialogFormState(
    val loanedTo: String = "",
    val loanedToError: String? = null,
    val returnDate: String = "",
    val returnDateError: String? = null,
)
