package com.kproject.simplechat.commom.exception

sealed class AuthenticationException : BaseException() {

    object UserNotFoundException : AuthenticationException()

    object InvalidEmailException : AuthenticationException()

    object EmptyEmaiException : AuthenticationException()

    object EmailInUseException : AuthenticationException()

    object EmptyPasswordException : AuthenticationException()

    object InvalidPasswordException : AuthenticationException()

    object WrongPasswordException : AuthenticationException()
}