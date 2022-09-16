package com.kproject.simplechat.commom.exception

sealed class AuthenticationException(
    override val message: String = ""
) : Exception(message) {

    data class EmailException(
        override val message: String
    ) : AuthenticationException(message)

    data class PasswordException(
        override val message: String
    ) : AuthenticationException(message)
}