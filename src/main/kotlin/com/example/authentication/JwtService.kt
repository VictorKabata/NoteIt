package com.example.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.models.User
import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object JwtService {

    private const val issuer = "noteServer"
    private val jwtSecret = System.getenv("JWT_SECRET") ?: "mementomori"
    private val algorithm = Algorithm.HMAC512(jwtSecret)

    val verifier = JWT.require(algorithm).withIssuer(issuer).build()

    /**Generate user token*/
    fun User.generateToken(): String {
        return JWT
            .create()
            .withSubject("NoteIt Authentication")
            .withIssuer(issuer)
            .withClaim("email", this.email)
            .sign(algorithm)
    }

    private val hashKey = System.getenv("HASH_KEY")?.toByteArray() ?: "mementomori".toByteArray()
    private val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")

    /**Hash password string*/
    fun String.hash(): String {
        val hmac = Mac.getInstance("HmacSHA1")
        hmac.init(hmacKey)

        return hex(hmac.doFinal(this.toByteArray(Charsets.UTF_8)))
    }
}