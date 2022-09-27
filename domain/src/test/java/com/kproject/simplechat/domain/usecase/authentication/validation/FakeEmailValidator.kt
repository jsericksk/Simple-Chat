package com.kproject.simplechat.domain.usecase.authentication.validation

import com.kproject.simplechat.domain.usecase.firebase.authentication.validation.EmailValidator

class FakeEmailValidator : EmailValidator {

    override fun isValidEmail(email: String): Boolean {
        if (email.contains("@") && email.contains(".")){
            return true
        }
        return false
    }
}