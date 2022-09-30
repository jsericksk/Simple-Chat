package com.kproject.simplechat.data.mapper

import com.kproject.simplechat.data.model.ChatMessageEntity
import com.kproject.simplechat.data.model.ChatMessageNotificationEntity
import com.kproject.simplechat.data.model.LatestMessageEntity
import com.kproject.simplechat.data.model.UserEntity
import com.kproject.simplechat.domain.model.firebase.ChatMessageModel
import com.kproject.simplechat.domain.model.firebase.ChatMessageNotificationModel
import com.kproject.simplechat.domain.model.firebase.LatestMessageModel
import com.kproject.simplechat.domain.model.firebase.UserModel

fun UserEntity.toUserModel() = UserModel(userId, username, profilePicture, registrationDate)

fun LatestMessageEntity.toLatestMessageModel() = LatestMessageModel(
    chatId, latestMessage, senderId, receiverId, username, userProfilePicture, timestamp
)

fun ChatMessageEntity.toChatMessageModel() = ChatMessageModel(message, senderId, receiverId, sendDate)

fun ChatMessageModel.toChatMessageEntity() = ChatMessageEntity(message, senderId, receiverId, sendDate)

fun ChatMessageNotificationModel.toChatMessageNotificationEntity() = ChatMessageNotificationEntity(
    title, message, fromUserId, fromUsername, userProfilePicture
)

