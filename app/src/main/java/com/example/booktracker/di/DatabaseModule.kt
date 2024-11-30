package com.example.booktracker.di

import android.content.Context
import androidx.room.Room
import com.example.booktracker.data.AppDatabase
import com.example.booktracker.data.dao.BookDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "database-book"
        ).build()
    }

    @Provides
    fun provideBookDao(database: AppDatabase): BookDao {
        return database.bookDao()
    }
}