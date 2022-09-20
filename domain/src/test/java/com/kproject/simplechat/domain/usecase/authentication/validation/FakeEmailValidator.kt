package com.kproject.simplechat.domain.usecase.authentication.validation

class FakeEmailValidator : EmailValidator {

    override fun isValidEmail(email: String): Boolean {
        if (email.contains("@") && email.contains(".")){
            return true
        }
        return false
    }
}