package com.kproject.simplechat.di

import android.util.Patterns
import com.kproject.simplechat.domain.usecase.authentication.validation.EmailValidator

class AndroidEmailValidator : EmailValidator {

    override fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}