package com.example.routes_graphql

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

