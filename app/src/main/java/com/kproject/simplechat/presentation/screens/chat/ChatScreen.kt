package com.kproject.simplechat.presentation.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kproject.simplechat.R
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.presentation.model.ChatMessage
import com.kproject.simplechat.presentation.model.User
import com.kproject.simplechat.presentation.model.fakeChatMessagesList
import com.kproject.simplechat.presentation.screens.components.CustomImage
import com.kproject.simplechat.presentation.screens.components.EmptyListInfo
import com.kproject.simplechat.presentation.screens.components.LoadingIndicator
import com.kproject.simplechat.presentation.theme.ChatTextFieldBackgroundColor
import com.kproject.simplechat.presentation.theme.CompletePreview
import com.kproject.simplechat.presentation.theme.PreviewTheme
import com.kproject.simplechat.presentation.theme.TextDefaultColor

@Composable
fun ChatScreen(
    user: User,
    loggedUserId: String,
    onNavigateBack: () -> Unit
) {
    val chatViewModel: ChatViewModel = hiltViewModel()
    val uiState: ChatUiState = chatViewModel.uiState
    val dataState = chatViewModel.dataState

    LaunchedEffect(user) {
        chatViewModel.initializeUser(user)
        chatViewModel.getMessages(fromUserId = user.userId)
    }

    MainContent(
        uiState = uiState,
        dataState = dataState,
        loggedUserId = loggedUserId,
        onMessageValueChange = { message ->
            chatViewModel.onMessageChange(message)
        },
        onSendMessage = { message ->
            val chatMessage = ChatMessage(
                message = message,
                senderId = loggedUserId,
                receiverId = user.userId
            )
            chatViewModel.sendMessage(user = uiState.user, chatMessage = chatMessage)
        },
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    uiState: ChatUiState,
    dataState: DataState<Unit>,
    loggedUserId: String,
    onMessageValueChange: (String) -> Unit,
    onSendMessage: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Column {
        TopBar(
            uiState = uiState,
            onNavigateBack = onNavigateBack
        )

        when (dataState) {
            is DataState.Loading -> {
                LoadingIndicator(
                    modifier = modifier
                )
            }
            is DataState.Success -> {
                Column {
                    ChatList(
                        chatMessagesList = uiState.chatMessageList,
                        loggedUserId = loggedUserId,
                        modifier = Modifier
                            .weight(1f)
                    )
                    Spacer(Modifier.height(8.dp))
                    ChatTextField(
                        message = uiState.message,
                        onMessageValueChange = { message ->
                            onMessageValueChange.invoke(message)
                        },
                        onSendMessage = { message ->
                            onSendMessage.invoke(message)
                        },
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
            is DataState.Error -> {
                EmptyListInfo(
                    iconResId = R.drawable.ic_mood_bad,
                    title = stringResource(id = R.string.error),
                    description = stringResource(id = R.string.error_get_chat_messages_list),
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
private fun TopBar(
    uiState: ChatUiState,
    onNavigateBack: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .padding(10.dp)
    ) {
        IconButton(onClick = onNavigateBack) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null,
                tint = Color.White
            )
        }
        Spacer(Modifier.width(8.dp))
        CustomImage(
            imageModel = uiState.user.profilePicture.ifEmpty { R.drawable.ic_person },
            colorFilter = if (uiState.user.profilePicture.isEmpty()) ColorFilter.tint(Color.White) else null,
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = uiState.user.username,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun ChatList(
    modifier: Modifier = Modifier,
    chatMessagesList: List<ChatMessage>,
    loggedUserId: String
) {
    if (chatMessagesList.isNotEmpty()) {
        val lazyListState = rememberLazyListState()

        /**
         * Always scroll to the last message when opening the screen or when
         * the list size changes (sends or receives a new message).
         */
        LaunchedEffect(key1 = chatMessagesList.size) {
            lazyListState.scrollToItem(chatMessagesList.size)
        }

        LazyColumn(
            state = lazyListState,
            modifier = modifier.fillMaxSize()
        ) {
            itemsIndexed(chatMessagesList) { index, message ->
                ChatListItem(
                    chatMessage = message,
                    loggedUserId = loggedUserId
                )
            }
        }
    } else {
        EmptyListInfo(
            iconResId = R.drawable.ic_message,
            title = stringResource(id = R.string.info_title_empty_message_list),
            description = stringResource(id = R.string.info_description_empty_message_list),
            modifier = modifier
        )
    }
}

@Composable
private fun ChatListItem(
    modifier: Modifier = Modifier,
    chatMessage: ChatMessage,
    loggedUserId: String
) {
    val messageReceived = loggedUserId != chatMessage.senderId
    val backgroundColor = if (messageReceived) Color.DarkGray else MaterialTheme.colors.secondary
    val alignment = if (messageReceived) Alignment.Start else Alignment.End
    val shape = if (messageReceived) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 8.dp, bottomStart = 8.dp, bottomEnd = 8.dp)
    } else {
        RoundedCornerShape(topStart = 8.dp, topEnd = 16.dp, bottomStart = 8.dp, bottomEnd = 8.dp)
    }

    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Text(
            text = chatMessage.message,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
                .background(
                    color = backgroundColor,
                    shape = shape
                )
                .align(alignment)
                .padding(8.dp)
        )

        Text(
            text = chatMessage.formattedDate,
            color = MaterialTheme.colors.TextDefaultColor,
            maxLines = 1,
            fontSize = 13.sp,
            modifier = Modifier
                .padding(start = 6.dp, end = 6.dp)
                .align(alignment)
        )
    }
}

@Composable
private fun ChatTextField(
    modifier: Modifier = Modifier,
    message: String,
    onMessageValueChange: (String) -> Unit,
    onSendMessage: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
    ) {
        TextField(
            value = message,
            onValueChange = { value ->
                onMessageValueChange.invoke(value)
            },
            textStyle = TextStyle(
                color = MaterialTheme.colors.TextDefaultColor,
                fontSize = 18.sp
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.message),
                    color = MaterialTheme.colors.secondary
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
                focusedLabelColor = MaterialTheme.colors.secondary,
                unfocusedLabelColor = MaterialTheme.colors.onSecondary,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(Modifier.width(8.dp))

        IconButton(
            onClick = { onSendMessage.invoke(message) },
            enabled = message.isNotBlank(),
            modifier = Modifier.background(
                color = MaterialTheme.colors.secondary,
                shape = CircleShape
            )
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_send),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@CompletePreview
@Composable
private fun Preview() {
    PreviewTheme {
        val uiState = ChatUiState(
            chatMessageList = fakeChatMessagesList,
            user = User(username = "Ericks")
        )
        MainContent(
            uiState = uiState,
            dataState = DataState.Success(),
            loggedUserId = "123456",
            onMessageValueChange = {},
            onSendMessage = {},
            onNavigateBack = {}
        )
    }
}