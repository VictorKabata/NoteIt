package com.example.plugins

import com.apurebase.kgraphql.GraphQL
import com.example.models.Note
import com.example.models.User
import io.ktor.server.application.*

fun Application.configureGraphQL() {
    install(GraphQL) {
        playground = true
        endpoint="/graphql"
        schema {
            configureGraphQLSchema()
        }
    }
}
