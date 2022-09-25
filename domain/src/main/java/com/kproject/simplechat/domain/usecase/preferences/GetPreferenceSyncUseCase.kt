package com.kproject.simplechat.domain.usecase.preferences

fun interface GetPreferenceSyncUseCase {
    operator fun invoke(key: String, defaultValue: Any): Any
}