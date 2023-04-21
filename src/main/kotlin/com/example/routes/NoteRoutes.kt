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

            val email =
                call.principal<User>()?.email ?: return@post call.respond(HttpStatusCode.BadRequest, "Invalid token")

            try {
                noteRepository.createNote(note = noteRequest, email = email)
                call.respond(HttpStatusCode.OK, noteRequest)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, e.localizedMessage)
            }
        }

        get(Constants.GET_ALL_NOTES) {
            val email =
                call.principal<User>()?.email ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid token")

            try {
                val notes = noteRepository.getAllNotes(email = email)

                call.respond(HttpStatusCode.OK, notes)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, e.localizedMessage)
            }
        }

        get(Constants.GET_NOTE) {
            val noteIdQueryParam = call.request.queryParameters["id"]?.toInt() ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "Missing ID parameter"
            )

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

        put(Constants.UPDATE_NOTE) {
            val noteIdQueryParam = call.request.queryParameters["id"]?.toInt() ?: return@put call.respond(
                HttpStatusCode.BadRequest,
                "Missing ID parameter"
            )

            val noteRequest = try {
                call.receive<Note>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
                return@put
            }

            val email = call.principal<User>()?.email ?: return@put call.respond(
                HttpStatusCode.BadRequest,
                "Invalid token"
            )

            try {
                noteRepository.updateNote(note = noteRequest, email = email)

                val updatedNote = noteRepository.getNote(id = noteIdQueryParam)

                if (updatedNote == null) {
                    call.respond(HttpStatusCode.OK, "Note not found")
                } else {
                    call.respond(HttpStatusCode.OK, updatedNote)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, e.localizedMessage)
            }
        }

        delete(Constants.DELETE_NOTE) {
            val noteIdQueryParam = call.request.queryParameters["id"]?.toInt() ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                "Missing ID parameter"
            )

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