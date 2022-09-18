package com.kproject.simplechat.domain.usecase.authentication.validation

interface EmailValidator {
    fun isValidEmail(email: String): Boolean
}