package com.kproject.simplechat.domain.usecase.firebase.authentication.validation

import com.kproject.simplechat.commom.validation.ValidationState

class ValidateProfilePictureUseCaseImpl : ValidateProfilePictureUseCase {

    override fun invoke(profilePicture: String): ValidationState {
       if (profilePicture.isBlank()) {
           return ValidationState.ProfilePictureNotSelected
       }

        return ValidationState.Success
    }
}