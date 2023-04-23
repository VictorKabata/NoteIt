package com.example.routes

import com.example.models.Note
import com.example.models.User
import com.example.repository.NoteRepository
import com.example.utils.Constants
import com.example.utils.ResponseHandler.errorResponse
import com.example.utils.ResponseHandler.successResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.noteRoutes(noteRepository: NoteRepository = NoteRepository()) = route(Constants.NOTES) {

    authenticate("jwt") {

        post(Constants.CREATE_NOTE) {
            val noteRequest: Note? = try {
                call.receive<Note>()
            } catch (e: Exception) {
                call.errorResponse(statusCode = HttpStatusCode.BadRequest, message = e.localizedMessage)
                return@post
            }

            val email = call.principal<User>()?.email ?: return@post call.errorResponse(
                statusCode = HttpStatusCode.BadRequest,
                message = "Invalid token"
            )

            try {
                if (noteRequest == null) {
                    call.errorResponse(statusCode = HttpStatusCode.BadRequest, message = "Missing note request body")
                } else {
                    noteRepository.createNote(note = noteRequest, email = email)
                    call.successResponse(statusCode = HttpStatusCode.Created, message = noteRequest)
                }
            } catch (e: Exception) {
                call.errorResponse(statusCode = HttpStatusCode.Conflict, message = e.localizedMessage)
            }
        }

        get(Constants.GET_ALL_NOTES) {
            val email = call.principal<User>()?.email ?: return@get call.errorResponse(
                statusCode = HttpStatusCode.BadRequest,
                message = "Invalid token"
            )

            try {
                val notes = noteRepository.getAllNotes(email = email)
                call.successResponse(message = notes)

            } catch (e: Exception) {
                call.errorResponse(statusCode = HttpStatusCode.Conflict, message = e.localizedMessage)
            }
        }

        get(Constants.GET_NOTE) {
            val noteIdQueryParam = call.request.queryParameters["id"]?.toInt() ?: return@get call.errorResponse(
                statusCode = HttpStatusCode.BadRequest,
                message = "Missing ID parameter"
            )

            try {
                val note = noteRepository.getNote(noteIdQueryParam)

                if (note == null) {
                    call.errorResponse(statusCode = HttpStatusCode.NotFound, message = "Note not found")
                } else {
                    call.successResponse(message = note)
                }
            } catch (e: Exception) {
                call.errorResponse(statusCode = HttpStatusCode.Conflict, message = e.localizedMessage)
            }
        }

        put(Constants.UPDATE_NOTE) {
            val noteIdQueryParam = call.request.queryParameters["id"]?.toInt() ?: return@put call.errorResponse(
                statusCode = HttpStatusCode.BadRequest,
                message = "Missing ID parameter"
            )

            val noteRequest: Note? = try {
                call.receive<Note>()
            } catch (e: Exception) {
                call.errorResponse(statusCode = HttpStatusCode.BadRequest, message = e.localizedMessage)
                return@put
            }

            val email = call.principal<User>()?.email ?: return@put call.respond(
                HttpStatusCode.BadRequest,
                "Invalid token"
            )

            try {
                if (noteRequest == null) {
                    call.errorResponse(statusCode = HttpStatusCode.NotFound, message = "Note not found")
                } else {
                    noteRepository.updateNote(note = noteRequest, email = email)

                    val updatedNote = noteRepository.getNote(id = noteIdQueryParam)

                    if (updatedNote == null) {
                        call.errorResponse(statusCode = HttpStatusCode.NotFound, message = "Note not found")
                    } else {
                        call.successResponse(message = updatedNote)
                    }
                }
            } catch (e: Exception) {
                call.errorResponse(statusCode = HttpStatusCode.Conflict, message = e.localizedMessage)
            }
        }

        delete(Constants.DELETE_NOTE) {
            val noteIdQueryParam = call.request.queryParameters["id"]?.toInt() ?: return@delete call.errorResponse(
                statusCode = HttpStatusCode.BadRequest,
                message = "Missing ID parameter"
            )

            try {
                val note = noteRepository.getNote(id = noteIdQueryParam)
                if (note == null) {
                    call.errorResponse(statusCode = HttpStatusCode.NotFound, message = "Note not found")
                } else {
                    noteRepository.deleteNote(id = noteIdQueryParam)
                    call.successResponse(message = mapOf("message" to "Note deleted"))
                }
            } catch (e: Exception) {
                call.errorResponse(statusCode = HttpStatusCode.Conflict, message = e.localizedMessage)
            }
        }


    }

}