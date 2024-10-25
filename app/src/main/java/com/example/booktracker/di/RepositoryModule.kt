package com.example.booktracker.di

import com.example.booktracker.data.repository.BookRepositoryImpl
import com.example.booktracker.domain.repository.BookRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun provideBookRepository(repository: BookRepositoryImpl): BookRepository
}