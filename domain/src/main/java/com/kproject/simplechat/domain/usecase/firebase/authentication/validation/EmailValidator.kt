package com.kproject.simplechat.domain.usecase.firebase.authentication.validation

interface EmailValidator {
    fun isValidEmail(email: String): Boolean
}