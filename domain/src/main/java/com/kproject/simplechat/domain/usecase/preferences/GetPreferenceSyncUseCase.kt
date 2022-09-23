package com.kproject.simplechat.domain.usecase.preferences

interface GetPreferenceSyncUseCase {
    operator fun invoke(key: String, defaultValue: Any): Any
}