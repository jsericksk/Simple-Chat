package com.kproject.simplechat.presentation.mapper

import com.kproject.simplechat.domain.model.firebase.UserModel
import com.kproject.simplechat.presentation.model.User

fun UserModel.toUser() = User(userId, username, profilePicture, registrationDate)