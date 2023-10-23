package com.example.plugins

import com.apurebase.kgraphql.GraphQL
import io.ktor.server.application.*

fun Application.configureGraphQL() {
    install(GraphQL) {
        playground = true
        endpoint = "/graphql"
        schema {
            configureGraphQLSchema()
        }
    }
}
