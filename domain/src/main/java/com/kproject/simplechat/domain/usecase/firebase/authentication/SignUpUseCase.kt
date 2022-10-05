package com.kproject.simplechat.domain.usecase.firebase.authentication

import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.model.authentication.SignUp

fun interface SignUpUseCase {
    suspend operator fun invoke(signUp: SignUp): DataState<Unit>
}