package com.example.plugins

import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import com.example.models.Note
import com.example.models.User
import com.example.routes_graphql.homeGraphQLRoutes
import com.example.routes_graphql.notesGraphQLRoutes
import com.example.routes_graphql.userGraphQLRoutes

fun SchemaBuilder.configureGraphQLSchema() {

    type<User> {
        description = "User object"
    }

    type<Note> {
        description = "Note object"
    }

    inputType<User> {
        description = "User object input"
    }

    inputType<Note> {
        description = "Note object input"
    }

    homeGraphQLRoutes()

    userGraphQLRoutes()

    notesGraphQLRoutes()

}
