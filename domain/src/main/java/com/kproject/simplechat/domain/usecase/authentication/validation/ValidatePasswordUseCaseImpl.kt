package com.kproject.simplechat.domain.usecase.authentication.validation

import com.kproject.simplechat.domain.provider.StringResourceProvider

class ValidatePasswordUseCaseImpl(
    private val stringResourceProvider: StringResourceProvider
) : ValidatePasswordUseCase {

    override fun invoke(password: String): Boolean {

        return false
    }
}