package com.example.repository

import com.example.cache.DatabaseFactory.dbQuery
import com.example.cache.tables.UserTable
import com.example.mappers.toUserDomain
import com.example.models.User
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class UserRepository {

    /**Create a new user*/
    suspend fun createUser(user: User) = dbQuery {
        UserTable.insert { userTable ->
            userTable[email] = user.email
            userTable[userName] = user.userName
            userTable[hashPassword] = user.hashPassword
        }
    }.resultedValues?.mapNotNull {
        it.toUserDomain()
    }?.singleOrNull()

    /**Get user*/
    suspend fun getUser(email: String): User? = dbQuery {
        UserTable.select { UserTable.email.eq(email) }.map { it.toUserDomain() }.singleOrNull()
    }

    /**Update user name*/
    suspend fun updateUser(user: User, email: String) {
        dbQuery {
            UserTable.update(where = {
                UserTable.email.eq(email)
            }) { userTable ->
                userTable[userName] = user.userName
            }
        }
    }

    /**Delete user account*/
    suspend fun deleteUser(email: String) {
        dbQuery {
            UserTable.deleteWhere { UserTable.email.eq(email) }
        }
    }
}
