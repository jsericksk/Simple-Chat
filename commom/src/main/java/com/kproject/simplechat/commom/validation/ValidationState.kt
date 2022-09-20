package com.kproject.simplechat.commom.validation

sealed class ValidationState {
    object Success : ValidationState()
    object EmptyEmail : ValidationState()
    object InvalidEmail : ValidationState()
    object EmptyPassword : ValidationState()
    object PasswordTooShort : ValidationState()
    object InvalidPassword : ValidationState()
    object RepeatedPasswordDoesNotMatch : ValidationState()
    object EmptyUsername : ValidationState()
    object InvalidUsername : ValidationState()
    object ProfilePictureNotSelected : ValidationState()
}