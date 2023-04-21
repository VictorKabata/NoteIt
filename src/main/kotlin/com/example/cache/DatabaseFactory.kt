package com.example.cache

import com.example.cache.table.NoteTable
import com.example.cache.table.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        Database.connect(datasource = hikari())

        transaction {
            SchemaUtils.create(UserTable, NoteTable)
        }
    }

    private fun hikari(): HikariDataSource {
        // ToDo: Remove env variable values
        val config = HikariConfig().apply {
            driverClassName = System.getenv("JDBC_DRIVER") ?: "org.postgresql.Driver"
            jdbcUrl = System.getenv("JDBC_DATABASE_URL") ?: "jdbc:postgresql:notes_db?user=postgres&password=password"
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        return HikariDataSource(config)

    }

    suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO) {
        transaction { block() }
    }


}