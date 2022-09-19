package com.kproject.simplechat.domain.model.authentication

data class SignUp(
    val profileImage: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = ""
)