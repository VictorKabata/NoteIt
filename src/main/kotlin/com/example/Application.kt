package com.example

import com.example.cache.DatabaseFactory.initDatabase
import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    /**Initialize database*/
    initDatabase()

    configureSecurity()
    configureMonitoring()
    configureSerialization()
    // configureContentValidation()
    configureRouting()
    // configureDocumentation()
    configureGraphQL()
}
