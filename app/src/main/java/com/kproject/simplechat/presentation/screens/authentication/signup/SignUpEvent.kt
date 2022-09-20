package com.kproject.simplechat.presentation.screens.authentication.signup

sealed class SignUpEvent {
    data class ProfilePictureChanged(val profilePicture: String) : SignUpEvent()
    data class EmailChanged(val email: String) : SignUpEvent()
    data class UsernameChanged(val username: String) : SignUpEvent()
    data class PasswordChanged(val password: String) : SignUpEvent()
    data class RepeatedPasswordChanged(val repeatedPassword: String) : SignUpEvent()
    object OnDismissErrorDialog : SignUpEvent()
}