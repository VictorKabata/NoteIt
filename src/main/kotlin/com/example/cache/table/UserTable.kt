package com.example.cache.table

import org.jetbrains.exposed.sql.Table

object UserTable : Table() {
    val email = varchar(name = "email", length = 512)
    val userName = varchar(name = "userName", length = 512)
    val hashPassword = varchar(name = "hashPassword", length = 512)

    override val primaryKey: PrimaryKey = PrimaryKey(email)
}
