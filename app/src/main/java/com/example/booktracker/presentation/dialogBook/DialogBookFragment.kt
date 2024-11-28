package com.example.booktracker.presentation.dialogBook

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.booktracker.databinding.FragmentDialogBookBinding
import com.example.booktracker.domain.model.BookDomain
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DialogBookFragment : DialogFragment() {

    private val viewModel: DialogBookViewModel by viewModels()
    lateinit var binding: FragmentDialogBookBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        @Suppress("DEPRECATION")
        val book = arguments?.getParcelable<BookDomain>(BOOK_ARG)

        val titleText = arguments?.getString(DIALOG_TITLE_TEXT)
            ?: throw IllegalArgumentException("Ups... title is required")

        binding = FragmentDialogBookBinding.inflate(
            requireActivity().layoutInflater
        ).apply {
            tvTitle.text = titleText

            book?.let { book ->
                tilTitle.editText?.setText(book.title)
                tilAuthor.editText?.setText(book.author)
                if (book.publicationYear != null) {
                    tilPublicationYear.editText?.setText(book.publicationYear.toString())
                }
                tilIsbn.editText?.setText(book.isbn)

                viewModel.loadBook(book)
            }

            tilTitle.editText?.addTextChangedListener { text ->
                viewModel.onEvent(BookFormEvent.TitleChanged(text.toString()))
            }
            tilAuthor.editText?.addTextChangedListener { text ->
                viewModel.onEvent(BookFormEvent.AuthorChanged(text.toString()))
            }
            tilPublicationYear.editText?.addTextChangedListener { text ->
                viewModel.onEvent(BookFormEvent.PublicationYearChanged(text.toString()))
            }
            tilIsbn.editText?.addTextChangedListener { text ->
                viewModel.onEvent(BookFormEvent.IsbnChanged(text.toString()))
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
                viewModel.onEvent(BookFormEvent.submit)
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
                    is DialogBookViewModel.ValidationEvent.Success -> {

                        setFragmentResult(
                            BOOK_FRAGMENT_RESULT,
                            bundleOf(
                                TIL_TITLE_VALUE to binding.tilTitle.editText?.text.toString(),
                                TIL_AUTHOR_VALUE to binding.tilAuthor.editText?.text.toString(),
                                TIL_PUBLICATION_YEAR_VALUE to binding.tilPublicationYear.editText?.text.toString(),
                                TIL_ISBN_VALUE to binding.tilIsbn.editText?.text.toString()
                            )
                        )
                        dismiss()
                    }
                }

            }
        }
    }

    private fun displayErrors(state: BookDialogFormState) {
        binding.apply {
            tilTitle.error = state.titleError
            tilAuthor.error = state.authorError
            tilPublicationYear.error = state.publicationYearError
            tilIsbn.error = state.isbnError
        }
    }

    companion object {
        const val DIALOG_TITLE_TEXT = "DIALOG_TITLE_TEXT"
        const val BOOK_ARG = "BOOK_ARG"
        const val BOOK_FRAGMENT_RESULT = "BOOK_FRAGMENT_RESULT"
        const val TIL_TITLE_VALUE = "TIL_TITLE_VALUE"
        const val TIL_AUTHOR_VALUE = "TIL_AUTHOR_VALUE"
        const val TIL_PUBLICATION_YEAR_VALUE = "TIL_PUBLICATION_YEAR_VALUE"
        const val TIL_ISBN_VALUE = "TIL_ISBN_VALUE"

        fun show(
            dialogTitle: String,
            fragmentManager: FragmentManager,
            tag: String = DialogBookFragment::class.simpleName.toString(),
            book: BookDomain? = null
        ) {
            DialogBookFragment().apply {
                arguments = bundleOf(
                    DIALOG_TITLE_TEXT to dialogTitle,
                    BOOK_ARG to book
                )
            }.show(fragmentManager, tag)

        }
    }
}