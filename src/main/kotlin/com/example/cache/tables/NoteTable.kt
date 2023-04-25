package com.example.cache.tables

import org.jetbrains.exposed.sql.Table

object NoteTable : Table() {
    val id = integer(name = "id").autoIncrement()
    val userEmail = varchar(name = "user_email", length = 512).references(UserTable.email)
    val title = text(name = "title")
    val description = text(name = "description")
    val createdAt = varchar(name = "created_at", length = 512)
    val updatedAt = varchar(name = "updated_at", length = 512).nullable()

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}
