package com.example.routes

import com.example.models.Note
import com.example.models.User
import com.example.repository.NoteRepository
import com.example.utils.Constants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.noteRoutes(noteRepository: NoteRepository = NoteRepository()) {

    authenticate("jwt") {

        post(Constants.CREATE_NOTE) {
            val noteRequest = try {
                call.receive<Note>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
                return@post
            }

            val email = try {
                call.principal<User>()?.email!!
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
                return@post
            }

            try {
                noteRepository.createNote(note = noteRequest, email = email)
                call.respond(HttpStatusCode.OK, noteRequest)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, e.localizedMessage)
            }
        }

        get(Constants.GET_ALL_NOTES) {
            val email = try {
                call.principal<User>()?.email!!
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
                return@get
            }

            try {
                val notes = noteRepository.getAllNotes(email = email)

                call.respond(HttpStatusCode.OK, notes)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, e.localizedMessage)
            }
        }

        get(Constants.GET_NOTE) {
            val noteIdQueryParam = try {
                call.request.queryParameters["id"]?.toInt()!!
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
                return@get
            }

            try {
                val note = noteRepository.getNote(noteIdQueryParam)

                if (note == null) {
                    call.respond(HttpStatusCode.OK, "Note not found")
                } else {
                    call.respond(HttpStatusCode.OK, note)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, e.localizedMessage)
            }
        }

        post(Constants.UPDATE_NOTE) {
            val noteIdQueryParam = try {
                call.request.queryParameters["id"]?.toInt()!!
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
                return@post
            }

            val noteRequest = try {
                call.receive<Note>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
                return@post
            }

            val email = try {
                call.principal<User>()?.email!!
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
                return@post
            }

            try {
                noteRepository.updateNote(note = noteRequest, email = email)

                val updatedNote = noteRepository.getNote(id = noteIdQueryParam)

                if (updatedNote == null) {
                    call.respond(HttpStatusCode.NotFound, "Note not found")
                } else {
                    call.respond(HttpStatusCode.OK, updatedNote)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, e.localizedMessage)
            }
        }

        delete(Constants.DELETE_NOTE) {
            val noteIdQueryParam = try {
                call.request.queryParameters["id"]?.toInt()!!
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
                return@delete
            }

            try {
                val note = noteRepository.getNote(id = noteIdQueryParam)
                if (note == null) {
                    call.respond(HttpStatusCode.NotFound, "Note not found")
                } else {
                    noteRepository.deleteNote(id = noteIdQueryParam)
                    call.respond(HttpStatusCode.OK, "Note deleted")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, e.localizedMessage)
            }
        }


    }

}