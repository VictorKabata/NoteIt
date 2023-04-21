package com.example.repository

import com.example.cache.DatabaseFactory.dbQuery
import com.example.cache.table.UserTable
import com.example.mappers.toUserDomain
import com.example.models.User
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserRepository {

    suspend fun createUser(user: User) {
        dbQuery {
            UserTable.insert { userTable ->
                userTable[email] = user.email
                userTable[userName] = user.userName
                userTable[hashPassword] = user.hashPassword
            }
        }
    }

    suspend fun findUserByEmail(email: String): User? = dbQuery {
        UserTable.select { UserTable.email.eq(email) }.map { it.toUserDomain() }.singleOrNull()
    }

}