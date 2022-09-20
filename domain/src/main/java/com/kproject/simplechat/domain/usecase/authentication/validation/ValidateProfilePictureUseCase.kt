package com.kproject.simplechat.domain.usecase.authentication.validation

import com.kproject.simplechat.commom.validation.ValidationState

interface ValidateProfilePictureUseCase {
    operator fun invoke(profilePicture: String): ValidationState
}