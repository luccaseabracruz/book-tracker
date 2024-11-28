package com.example.booktracker.presentation.dialogLoan

import android.app.AlertDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.booktracker.databinding.FragmentDialogLoanBinding
import com.example.booktracker.domain.model.BookDomain
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class DialogLoanFragment : DialogFragment() {
    private val viewModel: DialogLoanViewModel by viewModels()
    lateinit var binding: FragmentDialogLoanBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        @Suppress("DEPRECATION")
        val book = arguments?.getParcelable<BookDomain>(BOOK_ARG)
        val titleText = arguments?.getString(DIALOG_TITLE_TEXT)
            ?: throw IllegalArgumentException("Ups... title is required")


        binding = FragmentDialogLoanBinding.inflate(
            requireActivity().layoutInflater
        ).apply {
            tvTitle.text = titleText

            book?.let {
                tilLoanedTo.editText?.setText(book.loanedTo)
                tilReturnDate.editText?.setText(book.returnDate)
            }

            tilLoanedTo.editText?.addTextChangedListener { text ->
                viewModel.onEvent(LoanFormEvent.LoanedToChanged(text.toString()))
            }
            tilReturnDate.editText?.addTextChangedListener { text ->
                viewModel.onEvent(LoanFormEvent.ReturnDateChanged(text.toString()))
            }
        }

        val dialog = AlertDialog.Builder(requireContext()).setView(binding.root)
            .setPositiveButton("Confirm", null)
            .setNegativeButton("Cancel") { _, _ ->
                dismiss()
            }.create()

        dialog.setOnShowListener {
            val confirmationButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            confirmationButton.setOnClickListener {
                viewModel.onEvent(LoanFormEvent.submit)
            }
        }



        return dialog

    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch {
            viewModel.state.collect() { state ->
                displayErrors(state)
            }
        }

        lifecycleScope.launch {
            viewModel.validationEvents.collect { event ->
                when (event) {
                    is DialogLoanViewModel.ValidationEvent.Success -> {
                        val calendar = Calendar.getInstance()
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val formattedDate = dateFormat.format(calendar.time)
                        
                        setFragmentResult(
                            LOAN_FRAGMENT_RESULT,
                            bundleOf(
                                TIL_LOANED_TO to binding.tilLoanedTo.editText?.text.toString(),
                                TIL_RETURN_DATE to binding.tilReturnDate.editText?.text.toString(),
                                LOAN_DATE to formattedDate
                            )
                        )
                        dismiss()
                    }
                }

            }
        }


    }

    private fun displayErrors(state: LoanDialogFormState) {
        binding.apply {
            tilLoanedTo.error = state.loanedToError
            tilReturnDate.error = state.returnDateError
        }
    }

    companion object {
        const val DIALOG_TITLE_TEXT = "DIALOG_TITLE_TEXT"
        const val BOOK_ARG = "BOOK_ARG"
        const val LOAN_DATE = "TIL_LOAN_DATE"
        const val LOAN_FRAGMENT_RESULT = "LOAN_FRAGMENT_RESULT"
        const val TIL_LOANED_TO = "TIL_LOANED_TO"
        const val TIL_RETURN_DATE = "TIL_RETURN_DATE"

        fun show(
            dialogTitle: String,
            fragmentManager: FragmentManager,
            tag: String = DialogLoanFragment::class.simpleName.toString(),
            book: BookDomain?
        ) {
            DialogLoanFragment().apply {
                arguments = bundleOf(
                    DIALOG_TITLE_TEXT to dialogTitle,
                    BOOK_ARG to book
                )
            }.show(fragmentManager, tag)
        }

    }
}