package com.kproject.simplechat.domain.usecase.firebase.chat

import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.model.firebase.LatestMessageModel

fun interface SaveLatestMessageUseCase {
    suspend operator fun invoke(latestMessageModel: LatestMessageModel): DataState<Unit>
}