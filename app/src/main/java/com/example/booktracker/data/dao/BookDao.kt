package com.example.booktracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.booktracker.data.entity.BookEntity
import com.example.booktracker.domain.model.BookDomain
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM book WHERE id = :bookId")
    fun retrieveBookById(bookId: Int): BookEntity

    @Insert
    fun insertBook(book: BookEntity)

    @Update
    fun updateBook(book: BookEntity)
}