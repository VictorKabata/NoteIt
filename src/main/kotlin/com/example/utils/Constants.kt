package com.example.utils

object Constants {
    private const val API_VERSION = "/v1"
    const val HOME = "$API_VERSION/"
    const val USERS = "$API_VERSION/users"
    const val REGISTER = "register"
    const val LOGIN = "login"
    const val UPDATE_USER = "update"
    const val DELETE_USER = "delete"

    const val NOTES = "$API_VERSION/notes"
    const val CREATE_NOTE = "create"
    const val GET_ALL_NOTES = "getAllNotes"
    const val GET_NOTE = "getNote"
    const val UPDATE_NOTE = "update"
    const val DELETE_NOTE = "delete"
}