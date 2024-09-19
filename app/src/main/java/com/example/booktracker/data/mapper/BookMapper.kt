package com.example.booktracker.data.mapper

import com.example.booktracker.data.entity.BookEntity
import com.example.booktracker.domain.model.BookDomain

fun BookDomain.toEntity() = BookEntity(
    id = id,
    title = title,
    author = author,
    publicationYear = publicationYear,
    isbn = isbn,
    loanedTo = loanedTo,
    loanDate = loanDate,
    returnDate = returnDate
)

fun BookEntity.toDomain() = BookDomain(
    id = id,
    title = title,
    author = author,
    publicationYear = publicationYear,
    isbn = isbn,
    loanedTo = loanedTo,
    loanDate = loanDate,
    returnDate = returnDate
)