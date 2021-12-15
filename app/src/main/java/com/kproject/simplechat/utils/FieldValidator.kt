package com.kproject.simplechat.utils

import android.net.Uri
import android.util.Patterns
import com.kproject.simplechat.R

object FieldValidator {

    fun validate(
        email: String,
        password: String,
        confirmedPassword: String = "",
        userName: String = "",
        profileImage: Uri? = null,
        errorMessageResId: (Int) -> Unit = {}
    ): Boolean {
        var isValid = true
        if (email.isEmpty() || password.isEmpty() || confirmedPassword.isEmpty() || userName.isEmpty()) {
            errorMessageResId.invoke(R.string.error_fill_all_fields)
            isValid = false
        } else if (!isValidEmail(email)) {
            errorMessageResId.invoke(R.string.error_email_badly_formatted)
            isValid = false
        } else if (password.length < 5) {
            errorMessageResId.invoke(R.string.error_password_too_small)
            isValid = false
        } else if (confirmedPassword != password) {
            errorMessageResId.invoke(R.string.error_passwords_do_not_match)
            isValid = false
        } else if (profileImage == null) {
            errorMessageResId.invoke(R.string.error_profile_image_not_selected)
            isValid = false
        }
        return isValid
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}