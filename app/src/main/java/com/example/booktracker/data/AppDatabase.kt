package com.example.booktracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.booktracker.data.dao.BookDao
import com.example.booktracker.data.entity.BookEntity

@Database(
    entities = [
        BookEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}