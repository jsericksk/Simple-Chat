package com.kproject.simplechat.domain.usecase.firebase.user

import com.kproject.simplechat.commom.DataState

fun interface SubscribeToTopicUseCase {
    suspend operator fun invoke(userId: String): DataState<Unit>
}