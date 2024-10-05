package com.example.booktracker.presentation.dialogBook

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import com.example.booktracker.databinding.FragmentDialogBookBinding

class DialogBookFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        lateinit var binding: FragmentDialogBookBinding

        val titleText = arguments?.getString(DIALOG_TITLE_TEXT)
            ?: throw IllegalArgumentException("Ups... title is required")

        return activity.let {
            binding = FragmentDialogBookBinding.inflate(
                requireActivity().layoutInflater
            ).apply {
                tvTitle.text = titleText
            }

            AlertDialog.Builder(it).setView(binding.root).setPositiveButton("Confirm") { _, _ ->
                setFragmentResult(
                    FRAGMENT_RESULT,
                    bundleOf(
                        TIL_TITLE_VALUE to binding.tilTitle.editText?.text.toString(),
                        TIL_AUTHOR_VALUE to binding.tilAuthor.editText?.text.toString(),
                        TIL_PUBLICATION_YEAR_VALUE to binding.tilPublicationYear.editText?.text.toString(),
                        TIL_ISBN_VALUE to binding.tilIsbn.editText?.text.toString()
                    )
                )
            }.setNegativeButton("Cancel") { _, _ ->
                dismiss()
            }.create()
        } ?: throw IllegalStateException("The activity can not be null.")
    }

    companion object {
        const val DIALOG_TITLE_TEXT = "DIALOG_TITLE_TEXT"

        const val FRAGMENT_RESULT = "FRAGMENT_RESULT"
        const val TIL_TITLE_VALUE = "ETL_TITLE_VALUE"
        const val TIL_AUTHOR_VALUE = "ETL_AUTHOR_VALUE"
        const val TIL_PUBLICATION_YEAR_VALUE = "ETL_PUBLICATION_YEAR_VALUE"
        const val TIL_ISBN_VALUE = "ETL_ISBN_VALUE"

        fun show(
            dialogTitle: String,
            fragmentManager: FragmentManager,
            tag: String = DialogBookFragment::class.simpleName.toString()
        ) {
            DialogBookFragment().apply {
                arguments = bundleOf(
                    DIALOG_TITLE_TEXT to dialogTitle,
                )
            }.show(fragmentManager, tag)

        }
    }
}