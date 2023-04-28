package com.example.utils

import java.util.UUID

/**Convert string to UUID
 * @return [UUID]*/
fun String.toUUID(): UUID {
    return UUID.fromString(this)
}
