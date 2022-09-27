package com.kproject.simplechat.domain.usecase.authentication.validation

import com.kproject.simplechat.commom.validation.ValidationState
import com.kproject.simplechat.domain.usecase.firebase.authentication.validation.ValidateProfilePictureUseCase
import com.kproject.simplechat.domain.usecase.firebase.authentication.validation.ValidateProfilePictureUseCaseImpl
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ValidateProfilePictureUseCaseTest {
    private lateinit var validateProfilePictureUseCase: ValidateProfilePictureUseCase

    @Before
    fun setUp() {
        validateProfilePictureUseCase = ValidateProfilePictureUseCaseImpl()
    }

    @Test
    fun `profile picture not selected returns ProfilePictureNotSelected error`() {
        val profilePicture = ""

        val expected = ValidationState.ProfilePictureNotSelected

        val result = validateProfilePictureUseCase(profilePicture)

        assertEquals(expected, result)
    }

    @Test
    fun `profile picture selected returns Success`() {
        val profilePicture = "imageUri"

        val expected = ValidationState.Success

        val result = validateProfilePictureUseCase(profilePicture)

        assertEquals(expected, result)
    }
}