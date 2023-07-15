package com.example.plugins

import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import com.example.models.Note
import com.example.models.User
import com.example.routes_graphql.homeGraphQLRoutes

fun SchemaBuilder.configureGraphQLSchema(){

    type<User> {
        description = "User object"
    }

    type<Note> {
        description = "Note object"
    }

    homeGraphQLRoutes()


}
