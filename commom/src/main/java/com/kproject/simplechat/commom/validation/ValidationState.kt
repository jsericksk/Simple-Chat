package com.kproject.simplechat.commom.validation

sealed class ValidationState {
    object Success : ValidationState()
    object InvalidEmail : ValidationState()
    object EmptyEmail : ValidationState()
    object EmptyPassword : ValidationState()
    object InvalidPassword : ValidationState()
    object RepeatedPasswordDoesNotMatch : ValidationState()
}