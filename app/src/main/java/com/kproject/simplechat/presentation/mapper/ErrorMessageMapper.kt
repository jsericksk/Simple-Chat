package com.kproject.simplechat.presentation.mapper

import com.kproject.simplechat.R
import com.kproject.simplechat.commom.exception.AuthenticationException
import com.kproject.simplechat.commom.exception.BaseException
import com.kproject.simplechat.commom.validation.ValidationState
import com.kproject.simplechat.presentation.model.UiText

fun ValidationState.toErrorMessage(): UiText  {
    return when (this) {
        ValidationState.EmptyEmail -> UiText.StringResource(R.string.error_empty_email)
        ValidationState.InvalidEmail -> UiText.StringResource(R.string.error_email_badly_formatted)
        ValidationState.EmptyPassword -> UiText.StringResource(R.string.error_empty_password)
        ValidationState.PasswordTooShort -> UiText.StringResource(R.string.error_password_too_short)
        ValidationState.InvalidPassword -> UiText.StringResource(R.string.error_invalid_password)
        ValidationState.RepeatedPasswordDoesNotMatch -> UiText.StringResource(R.string.error_passwords_does_not_match)
        else -> UiText.HardcodedString("")
    }
}

fun BaseException.toErrorMessage(): UiText  {
    return when (this) {
        AuthenticationException.EmailInUseException -> UiText.StringResource(R.string.error_email_already_in_use)
        AuthenticationException.UnknownLoginException -> UiText.StringResource(R.string.error_login)
        AuthenticationException.UnknownSignUpException -> UiText.StringResource(R.string.error_sign_up)
        AuthenticationException.UserNotFoundException -> UiText.StringResource(R.string.error_user_not_found)
        AuthenticationException.WrongPasswordException -> UiText.StringResource(R.string.error_password_is_wrong)
        else -> UiText.StringResource(R.string.unknown_error)
    }
}