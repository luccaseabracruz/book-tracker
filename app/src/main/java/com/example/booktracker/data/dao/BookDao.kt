package com.example.booktracker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.booktracker.data.entity.Book
import com.example.booktracker.data.entity.BookEntity

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAllBooks(): List<Book>

    @Insert
    fun insertBook(book: BookEntity)
}