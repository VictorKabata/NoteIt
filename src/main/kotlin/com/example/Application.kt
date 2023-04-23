package com.example

import com.example.cache.DatabaseFactory.initDatabase
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {

    /**Initialize database*/
    initDatabase()

    configureSecurity()
    configureSerialization()
    // configureMonitoring()
    configureRouting()
}
