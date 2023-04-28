package com.example.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.models.User
import com.typesafe.config.ConfigFactory
import io.ktor.util.hex
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object JwtService {

    private val envConfig = ConfigFactory.load()

    private val issuer = envConfig.getString("jwt.issuer")
    private val jwtSecret = envConfig.getString("jwt.secret")
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

    private val hashKey = envConfig.getString("security.hashKey").toByteArray()

    private val hashAlgorithm = envConfig.getString("security.hashAlgorithm")
    private val hmacKey = SecretKeySpec(hashKey, hashAlgorithm)

    /**Hash password string*/
    fun String.hash(): String {
        val hmac = Mac.getInstance(hashAlgorithm)
        hmac.init(hmacKey)

        return hex(hmac.doFinal(this.toByteArray(Charsets.UTF_8)))
    }
}
