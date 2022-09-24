package com.kproject.simplechat.data.mapper

import com.kproject.simplechat.data.model.UserEntity
import com.kproject.simplechat.domain.model.firebase.UserModel

fun UserEntity.toUserModel() = UserModel(userId, username, profilePicture, registrationDate)