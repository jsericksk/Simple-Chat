package com.kproject.simplechat.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.kproject.simplechat.R
import com.kproject.simplechat.data.repository.TAG
import com.kproject.simplechat.ui.screens.components.TopBar
import com.kproject.simplechat.ui.theme.TextFieldFocusedIndicatorColor
import com.kproject.simplechat.ui.theme.TextFieldUnfocusedIndicatorColor
import com.kproject.simplechat.ui.viewmodels.ChatViewModel
import com.kproject.simplechat.utils.Utils
import java.util.*

@Composable
fun ChatScreen(
    userId: String,
    userName: String,
    userProfileImage: String,
    navigateBack: () -> Unit,
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    val messageList = chatViewModel.messageList.observeAsState()

    var isRequestFinished by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!isRequestFinished) {
            Log.d(TAG, "is Request not Finished")
            chatViewModel.getMessages(fromUserId = userId)
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = userName,
                navigateBack = navigateBack
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                 messageList.value?.let {
                     isRequestFinished = true
                    itemsIndexed(it) { index, message ->
                        MessageText(
                            message = message.message,
                            date = message.timestamp,
                            messageReceived = message.senderId == userId
                        )
                    }
                }
            }

            val message = remember { mutableStateOf("") }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = message.value,
                    onValueChange = { value ->
                        message.value = value
                    },
                    textStyle = TextStyle(color = Color.DarkGray, fontSize = 18.sp),
                    label = {
                        Text(text = stringResource(id = R.string.message))
                    },
                    maxLines = 4,
                    shape = CircleShape.copy(CornerSize(8.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.DarkGray,
                        leadingIconColor = Color.White,
                        trailingIconColor = Color.White,
                        focusedIndicatorColor = MaterialTheme.colors.TextFieldFocusedIndicatorColor,
                        unfocusedIndicatorColor = MaterialTheme.colors.TextFieldUnfocusedIndicatorColor,
                        focusedLabelColor = MaterialTheme.colors.TextFieldUnfocusedIndicatorColor,
                        unfocusedLabelColor = MaterialTheme.colors.TextFieldFocusedIndicatorColor,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp)
                        .weight(1f)
                )

                Spacer(modifier = Modifier.padding(start = 4.dp, end = 4.dp))

                Image(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(color = MaterialTheme.colors.onSecondary)
                        .clickable {
                            if (message.value.isNotEmpty()) {
                                chatViewModel.sendMessage(
                                    message = message.value,
                                    senderId = FirebaseAuth.getInstance().currentUser?.uid!!,
                                    receiverId = userId
                                )

                                chatViewModel.saveLastMessage(
                                    lastMessage = message.value,
                                    senderId = Utils.getCurrentUserId(),
                                    receiverId = userId,
                                    userName = userName,
                                    userProfileImage = userProfileImage
                                )
                                message.value = ""
                            }
                        }
                        .fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun MessageText(
    message: String,
    date: Date?,
    messageReceived: Boolean
) {
    val backgroundColor = if (messageReceived) Color.DarkGray else MaterialTheme.colors.onSecondary
    val alignment = if (messageReceived) Alignment.End else Alignment.Start

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = message,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
                .background(color = backgroundColor, shape = CircleShape.copy(CornerSize(8.dp)))
                .padding(8.dp)
                .align(alignment)
        )

        Text(
            text = Utils.getFormattedDate(date),
            color = Color.DarkGray,
            maxLines = 1,
            fontSize = 13.sp,
            modifier = Modifier
                .padding(start = 6.dp, end = 6.dp)
                .align(alignment)
        )

        Spacer(modifier = Modifier.padding(top = 4.dp))
    }
}