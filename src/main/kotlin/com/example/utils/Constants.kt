package com.example.utils

object Constants {
    private const val API_VERSION = "/v1"
    const val HOME = "$API_VERSION/"
    private const val USERS = "$API_VERSION/users"
    const val REGISTER = "$USERS/register"
    const val LOGIN = "$USERS/login"

    private const val NOTES = "$API_VERSION/notes"
    const val CREATE_NOTE = "$NOTES/create"
    const val GET_ALL_NOTES = "$NOTES/getAllNotes"
    const val GET_NOTE = "$NOTES/getNote"
    const val UPDATE_NOTE = "$NOTES/update"
    const val DELETE_NOTE = "$NOTES/delete"
}