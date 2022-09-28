package com.kproject.simplechat.presentation.mapper

import com.kproject.simplechat.domain.model.firebase.ChatMessageModel
import com.kproject.simplechat.domain.model.firebase.LatestMessageModel
import com.kproject.simplechat.domain.model.firebase.UserModel
import com.kproject.simplechat.presentation.model.ChatMessage
import com.kproject.simplechat.presentation.model.LatestMessage
import com.kproject.simplechat.presentation.model.User

fun UserModel.toUser() = User(userId, username, profilePicture, registrationDate)

fun LatestMessageModel.toLatestMessage() = LatestMessage(
    chatId, latestMessage, senderId, receiverId, username, userProfilePicture, timestamp
)

fun ChatMessageModel.toChatMessage() = ChatMessage(message, senderId, receiverId, sendDate)

fun ChatMessage.toChatMessageModel() = ChatMessageModel(message, senderId, receiverId, sendDate)