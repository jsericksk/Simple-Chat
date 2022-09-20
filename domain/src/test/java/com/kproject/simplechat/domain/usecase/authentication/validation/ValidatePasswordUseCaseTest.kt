package com.kproject.simplechat.domain.usecase.authentication.validation

import com.kproject.simplechat.commom.validation.ValidationState
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ValidatePasswordUseCaseTest {
    private lateinit var validatePasswordUseCase: ValidatePasswordUseCase

    @Before
    fun setUp() {
        validatePasswordUseCase = ValidatePasswordUseCaseImpl()
    }

    @Test
    fun `empty password returns EmptyPassword error`() {
        val password = ""

        val expected = ValidationState.EmptyPassword

        val result = validatePasswordUseCase(password)

        assertEquals(expected, result)
    }

    @Test
    fun `password without at least 6 characters returns PasswordTooShort error`() {
        val password = "123a"

        val expected = ValidationState.PasswordTooShort

        val result = validatePasswordUseCase(password)

        assertEquals(expected, result)
    }

    @Test
    fun `password without digits returns InvalidPassword error`() {
        val password = "simplechat"

        val expected = ValidationState.InvalidPassword

        val result = validatePasswordUseCase(password)

        assertEquals(expected, result)
    }

    @Test
    fun `password with at least 6 characters and one digit returns Success`() {
        val password = "simplechat123"

        val expected = ValidationState.Success

        val result = validatePasswordUseCase(password)

        assertEquals(expected, result)
    }
}