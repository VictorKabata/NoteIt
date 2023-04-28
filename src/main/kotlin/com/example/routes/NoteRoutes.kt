package com.example.routes

import com.example.models.Note
import com.example.models.User
import com.example.repository.NoteRepository
import com.example.utils.Constants
import com.example.utils.ResponseHandler.errorResponse
import com.example.utils.ResponseHandler.successResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

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
                    noteRepository.createNote(note = noteRequest, email = email)?.let {
                        call.successResponse(statusCode = HttpStatusCode.Created, message = it)
                    }
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

            val pageNumberQueryParam = call.request.queryParameters["page"]?.toInt() ?: 1
            val pageSizeQueryParam = call.request.queryParameters["size"]?.toInt() ?: 10

            try {
                val notes = noteRepository.getAllNotes(
                    email = email,
                    pageSize = pageSizeQueryParam,
                    pageNumber = pageNumberQueryParam
                )

                val pagedResponse = mapOf(
                    "page" to pageNumberQueryParam,
                    "size" to pageSizeQueryParam,
                    "data" to notes
                )

                call.successResponse(message = pagedResponse)
            } catch (e: Exception) {
                call.errorResponse(statusCode = HttpStatusCode.Conflict, message = e.localizedMessage)
            }
        }

        get(Constants.GET_NOTE) {
            val noteIdQueryParam = call.request.queryParameters["id"] ?: return@get call.errorResponse(
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
            val noteIdQueryParam = call.request.queryParameters["id"] ?: return@put call.errorResponse(
                statusCode = HttpStatusCode.BadRequest,
                message = "Missing ID parameter"
            )

            val noteRequest: Note? = try {
                call.receive<Note>()
            } catch (e: Exception) {
                call.errorResponse(statusCode = HttpStatusCode.BadRequest, message = e.localizedMessage)
                return@put
            }

            val email = call.principal<User>()?.email ?: return@put call.errorResponse(
                statusCode = HttpStatusCode.BadRequest,
                message = "Invalid token"
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
            val noteIdQueryParam = call.request.queryParameters["id"] ?: return@delete call.errorResponse(
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
