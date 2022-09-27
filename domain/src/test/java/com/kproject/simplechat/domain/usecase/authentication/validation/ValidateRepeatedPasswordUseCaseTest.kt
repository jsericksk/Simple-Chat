package com.kproject.simplechat.domain.usecase.authentication.validation

import com.kproject.simplechat.commom.validation.ValidationState
import com.kproject.simplechat.domain.usecase.firebase.authentication.validation.ValidateRepeatedPasswordUseCase
import com.kproject.simplechat.domain.usecase.firebase.authentication.validation.ValidateRepeatedPasswordUseCaseImpl
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ValidateRepeatedPasswordUseCaseTest {
    private lateinit var validateRepeatedPasswordUseCase: ValidateRepeatedPasswordUseCase

    @Before
    fun setUp() {
        validateRepeatedPasswordUseCase = ValidateRepeatedPasswordUseCaseImpl()
    }

    @Test
    fun `repeated password different from password returns RepeatedPasswordDoesNotMatch error`() {
        val password = "123456"
        val repeatedPassword = "1234567"

        val expected = ValidationState.RepeatedPasswordDoesNotMatch

        val result = validateRepeatedPasswordUseCase(password, repeatedPassword)

        assertEquals(expected, result)
    }

    @Test
    fun `repeated password equals password returns Success`() {
        val password = "123456"
        val repeatedPassword = "123456"

        val expected = ValidationState.Success

        val result = validateRepeatedPasswordUseCase(password, repeatedPassword)

        assertEquals(expected, result)
    }
}