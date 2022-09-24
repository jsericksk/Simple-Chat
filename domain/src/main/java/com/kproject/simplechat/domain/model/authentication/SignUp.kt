package com.kproject.simplechat.domain.model.authentication

data class SignUp(
    val profilePicture: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = ""
)