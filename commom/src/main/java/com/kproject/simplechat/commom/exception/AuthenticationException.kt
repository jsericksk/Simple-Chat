package com.kproject.simplechat.commom.exception

sealed class AuthenticationException : BaseException() {
    object UserNotFoundException : AuthenticationException()
    object EmailInUseException : AuthenticationException()
    object WrongPasswordException : AuthenticationException()
    object UnknownLoginException : AuthenticationException()
    object UnknownSignUpException : AuthenticationException()
}