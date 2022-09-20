package com.kproject.simplechat.domain.usecase.authentication.validation

import com.kproject.simplechat.commom.validation.ValidationState
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ValidateUsernameUseCaseTest {
    private lateinit var validateUsernameUseCase: ValidateUsernameUseCase

    @Before
    fun setUp() {
        validateUsernameUseCase = ValidateUsernameUseCaseImpl()
    }

    @Test
    fun `empty username returns EmptyUsername error`() {
        val username = ""

        val expected = ValidationState.EmptyUsername

        val result = validateUsernameUseCase(username)

        assertEquals(expected, result)
    }

    @Test
    fun `username starts with withespace or @ returns InvalidUsername error`() {
        val username1 = " Simple Chat"
        val username2 = "@Simple Chat"

        val expected = ValidationState.InvalidUsername

        val result1 = validateUsernameUseCase(username1)
        val result2 = validateUsernameUseCase(username2)

        assertEquals(expected, result1)
        assertEquals(expected, result2)
    }

    @Test
    fun `correct username returns Success`() {
        val username = "Simple Chat"

        val expected = ValidationState.Success

        val result = validateUsernameUseCase(username)

        assertEquals(expected, result)
    }
}