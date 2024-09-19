package com.example.booktracker.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

typealias BookEntity = Book

@Entity
data class Book (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "publication_year") val publicationYear: Int?,
    @ColumnInfo(name = "isbn") val isbn: String?,
    @ColumnInfo(name = "loaned_to") val loanedTo: String?,
    @ColumnInfo(name = "loan_date") val loanDate: String?,
    @ColumnInfo(name = "return_date") val returnDate: String?
)