package com.example.mappers

import com.example.cache.tables.NoteTable
import com.example.cache.tables.UserTable
import com.example.models.Note
import com.example.models.User
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow?.toUserDomain(): User? {
    return if (this == null) null
    else User(
        email = this[UserTable.email],
        userName = this[UserTable.userName],
        hashPassword = this[UserTable.hashPassword],
    )
}

fun ResultRow?.toNoteDomain(): Note? {
    return if (this == null) null
    else Note(
        id = this[NoteTable.id].toString(),
        title = this[NoteTable.title],
        description = this[NoteTable.description],
        createdAt = this[NoteTable.createdAt],
        updatedAt = this[NoteTable.updatedAt]
    )
}