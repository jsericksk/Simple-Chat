package com.kproject.simplechat.domain.usecase.authentication.validation

import com.kproject.simplechat.commom.validation.ValidationState
import com.kproject.simplechat.domain.usecase.firebase.authentication.validation.ValidateEmailUseCase
import com.kproject.simplechat.domain.usecase.firebase.authentication.validation.ValidateEmailUseCaseImpl
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ValidateEmailUseCaseTest {
    private lateinit var validateEmailUseCase: ValidateEmailUseCase

    @Before
    fun setUp() {
        validateEmailUseCase = ValidateEmailUseCaseImpl(FakeEmailValidator())
    }

    @Test
    fun `empty email returns EmptyEmail error`() {
        val email = ""

        val expected = ValidationState.EmptyEmail

        val result = validateEmailUseCase(email)

        assertEquals(expected, result)
    }

    @Test
    fun `email without @ returns InvalidEmail error`() {
        val email = "simplechatgmail.com.br"

        val expected = ValidationState.InvalidEmail

        val result = validateEmailUseCase(email)

        assertEquals(expected, result)
    }

    @Test
    fun `email without dot returns InvalidEmail error`() {
        val email = "simplechat@gmailcombr"

        val expected = ValidationState.InvalidEmail

        val result = validateEmailUseCase(email)

        assertEquals(expected, result)
    }

    @Test
    fun `correct email returns Success`() {
        val email = "simplechat@gmail.com.br"

        val expected = ValidationState.Success

        val result = validateEmailUseCase(email)

        assertEquals(expected, result)
    }
}