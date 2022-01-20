package com.kproject.simplechat.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import coil.annotation.ExperimentalCoilApi
import com.google.firebase.auth.FirebaseAuth
import com.kproject.simplechat.R
import com.kproject.simplechat.data.DataStateResult
import com.kproject.simplechat.data.network.models.MessageNotificationData
import com.kproject.simplechat.data.network.models.PushNotification
import com.kproject.simplechat.model.Message
import com.kproject.simplechat.ui.screens.components.ChatTopBar
import com.kproject.simplechat.ui.screens.components.EmptyListInfo
import com.kproject.simplechat.ui.screens.components.LoadingProgressIndicator
import com.kproject.simplechat.ui.theme.ChatTextFieldBackgroundColor
import com.kproject.simplechat.ui.theme.TextDefaultColor
import com.kproject.simplechat.ui.theme.TextFieldFocusedIndicatorColor
import com.kproject.simplechat.ui.theme.TextFieldUnfocusedIndicatorColor
import com.kproject.simplechat.ui.viewmodels.ChatViewModel
import com.kproject.simplechat.utils.Utils
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalCoilApi
@Composable
fun ChatScreen(
    userId: String,
    userName: String,
    userProfileImage: String,
    navigateBack: () -> Unit,
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    val dataStateResult by chatViewModel.dataStateResult.observeAsState()
    val messageList = chatViewModel.messageList.observeAsState()

    val isRequestFinished = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(messageList) {
        if (!isRequestFinished.value) {
            chatViewModel.getMessages(fromUserId = userId)
        }
    }

    Scaffold(
        topBar = {
            ChatTopBar(
                userName = userName,
                navigateBack = navigateBack
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                messageList.value?.let { list ->
                    if (list.isNotEmpty()) {
                        MessageList(
                            userId = userId,
                            messageList = list,
                            isRequestFinished = isRequestFinished
                        )
                    } else {
                        EmptyListInfo(
                            iconResId = R.drawable.ic_chat,
                            messageResId = R.string.info_empty_message_list,
                            errorMessageResId = R.string.info_message_empty_message_list
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
                TextField(
                    value = message.value,
                    onValueChange = { value ->
                        message.value = value
                    },
                    textStyle = TextStyle(
                        color = MaterialTheme.colors.TextDefaultColor,
                        fontSize = 18.sp
                    ),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.message),
                            color = MaterialTheme.colors.TextFieldFocusedIndicatorColor
                        )
                    },
                    maxLines = 4,
                    shape = CircleShape,
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = MaterialTheme.colors.TextDefaultColor,
                        backgroundColor = MaterialTheme.colors.ChatTextFieldBackgroundColor,
                        leadingIconColor = Color.White,
                        trailingIconColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedLabelColor = MaterialTheme.colors.TextFieldUnfocusedIndicatorColor,
                        unfocusedLabelColor = MaterialTheme.colors.TextFieldFocusedIndicatorColor,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
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
                            if (message.value
                                    .trim()
                                    .isNotEmpty()
                            ) {
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

                                val topic = "/topics/$userId"
                                PushNotification(
                                    MessageNotificationData(
                                        title = userName,
                                        message = message.value,
                                        fromUserId = Utils.getCurrentUserId(),
                                        fromUserName = "User",
                                        userProfileImage = "test"
                                    ),
                                    topic
                                ).also { notification ->
                                    chatViewModel.postNotification(notification)
                                }
                                message.value = ""
                            }
                        }
                        .fillMaxSize()
                )
            }
        }

        if (!isRequestFinished.value) {
            when (dataStateResult) {
                is DataStateResult.Loading -> {
                    LoadingProgressIndicator()
                }
                else -> {}
            }
        }
    }
}

@Composable
fun MessageList(
    userId: String,
    messageList: List<Message>,
    isRequestFinished: MutableState<Boolean>
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    var currentMessageListSize by rememberSaveable { mutableStateOf(0) }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        isRequestFinished.value = true
        itemsIndexed(messageList) { index, message ->
            MessageTextItem(
                message = message.message,
                date = message.timestamp,
                messageReceived = message.senderId == userId
            )
        }

        /**
         * Always scroll to the last message when opening the screen or when
         * the list size changes (sends or receives a new message).
         */
        if (messageList.size > currentMessageListSize) {
            coroutineScope.launch {
                listState.scrollToItem(messageList.size)
            }
            currentMessageListSize = messageList.size
        }
    }
}

@Composable
fun MessageTextItem(
    message: String,
    date: Date?,
    messageReceived: Boolean
) {
    val backgroundColor = if (messageReceived) Color.DarkGray else MaterialTheme.colors.onSecondary
    val alignment = if (messageReceived) Alignment.Start else Alignment.End

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
            color = MaterialTheme.colors.TextDefaultColor,
            maxLines = 1,
            fontSize = 13.sp,
            modifier = Modifier
                .padding(start = 6.dp, end = 6.dp)
                .align(alignment)
        )

        Spacer(modifier = Modifier.padding(top = 4.dp))
    }
}