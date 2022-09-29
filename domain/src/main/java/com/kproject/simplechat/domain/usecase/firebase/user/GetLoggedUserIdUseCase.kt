package com.kproject.simplechat.domain.usecase.firebase.user

fun interface GetLoggedUserIdUseCase {
    operator fun invoke(): String?
}