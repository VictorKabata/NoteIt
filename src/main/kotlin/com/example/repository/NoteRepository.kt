package com.example.repository

import com.example.cache.DatabaseFactory.dbQuery
import com.example.cache.tables.NoteTable
import com.example.mappers.toNoteDomain
import com.example.models.Note
import com.example.utils.toUUID
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class NoteRepository {

    /**Save a note to database*/
    suspend fun createNote(note: Note, email: String) = dbQuery {
        NoteTable.insert { noteTable ->
            noteTable[userEmail] = email
            noteTable[title] = note.title
            noteTable[description] = note.description
            noteTable[createdAt] = Clock.System.now().toLocalDateTime(TimeZone.UTC).toString()
        }
    }.resultedValues?.mapNotNull {
        it.toNoteDomain()
    }?.singleOrNull()

    /**Get all notes for a specific user*/
    suspend fun getAllNotes(email: String): List<Note> = dbQuery {
        NoteTable.select {
            NoteTable.userEmail.eq(email)
        }.mapNotNull {
            it.toNoteDomain()
        }
    }

    /**Get single note based on note id*/
    suspend fun getNote(id: String): Note? = dbQuery {
        NoteTable.select {
            NoteTable.id.eq(id.toUUID())
        }.map {
            it.toNoteDomain()
        }.singleOrNull()
    }

    /**Update specific note*/
    suspend fun updateNote(note: Note, email: String) {
        dbQuery {
            NoteTable.update(
                where = {
                    NoteTable.id.eq(note.id.toUUID()) and NoteTable.userEmail.eq(email)
                }
            ) { noteTable ->
                noteTable[title] = note.title
                noteTable[description] = note.description
                noteTable[updatedAt] = Clock.System.now().toLocalDateTime(TimeZone.UTC).toString()
            }
        }
    }

    /**Delete specific note based on id*/
    suspend fun deleteNote(id: String) {
        dbQuery { NoteTable.deleteWhere { NoteTable.id.eq(id.toUUID()) } }
    }

}