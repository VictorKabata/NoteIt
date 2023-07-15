package com.example.routes_graphql

import com.apurebase.kgraphql.schema.dsl.SchemaBuilder

fun SchemaBuilder.homeGraphQLRoutes() {
    query("home") {
        description = "Home endpoint"
        resolver { -> "Hello, World!" }
    }

}
