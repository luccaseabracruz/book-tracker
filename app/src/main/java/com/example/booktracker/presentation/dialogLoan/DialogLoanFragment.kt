package com.example.booktracker.presentation.dialogLoan

import android.app.AlertDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import com.example.booktracker.databinding.FragmentDialogLoanBinding
import com.example.booktracker.domain.model.BookDomain
import java.text.SimpleDateFormat
import java.util.Locale

class DialogLoanFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        lateinit var binding: FragmentDialogLoanBinding

        val book = arguments?.getParcelable<BookDomain>(BOOK_ARG)
        val titleText = arguments?.getString(DIALOG_TITLE_TEXT)
            ?: throw IllegalArgumentException("Ups... title is required")

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)

        return activity.let {
            binding = FragmentDialogLoanBinding.inflate(
                requireActivity().layoutInflater
            ).apply {
                tvTitle.text = titleText

                book?.let {
                    tilLoanedTo.editText?.setText(book.loanedTo)
                    tilReturnDate.editText?.setText(book.returnDate)
                }
            }

            AlertDialog.Builder(it).setView(binding.root).setPositiveButton("Confirm") { _, _ ->
                setFragmentResult(
                    LOAN_FRAGMENT_RESULT,
                    bundleOf(
                        TIL_LOANED_TO to binding.tilLoanedTo.editText?.text.toString(),
                        TIL_RETURN_DATE to binding.tilReturnDate.editText?.text.toString(),
                        LOAN_DATE to formattedDate
                    )
                )
            }.setNegativeButton("Cancel") { _, _ ->
                dismiss()
            }.create()
        } ?: throw IllegalStateException("The activity can not be null.")

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