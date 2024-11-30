package com.example.booktracker.presentation.dialogConfirmation

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class DialogConfirmationFragment() : DialogFragment() {
    private var message: String? = null
    private var onConfirm: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val messageText = message ?: throw IllegalArgumentException("Dialog message is required.")

        return AlertDialog.Builder(requireContext())
            .setMessage(messageText)
            .setPositiveButton("Confirm") { _, _ ->
                onConfirm?.invoke()
            }.setNegativeButton("Cancel") { _, _ ->
                dismiss()
            }.create()
    }

    companion object {
        private fun newInstance(dialogMessage: String, onConfirm: () -> Unit): DialogConfirmationFragment {
            return DialogConfirmationFragment().apply {
                this.message = dialogMessage
                this.onConfirm = onConfirm
            }
        }

        fun showDialog(
            dialogMessage: String,
            fragmentManager: FragmentManager,
            onConfirm: () -> Unit
        ) {
            val dialog = newInstance(dialogMessage, onConfirm)
            dialog.show(fragmentManager, DialogConfirmationFragment::class.simpleName.toString())
        }
    }
}