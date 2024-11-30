package com.example.booktracker.presentation

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, message, duration).show()
}