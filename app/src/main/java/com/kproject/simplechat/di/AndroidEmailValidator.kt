package com.kproject.simplechat.di

import androidx.core.util.PatternsCompat
import com.kproject.simplechat.domain.usecase.authentication.validation.EmailValidator

class AndroidEmailValidator : EmailValidator {

    override fun isValidEmail(email: String): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }
}