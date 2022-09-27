package com.kproject.simplechat.domain.usecase.firebase.authentication.validation

import com.kproject.simplechat.commom.validation.ValidationState

interface ValidateProfilePictureUseCase {
    operator fun invoke(profilePicture: String): ValidationState
}