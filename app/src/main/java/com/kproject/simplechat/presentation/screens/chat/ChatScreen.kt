package com.kproject.simplechat.presentation.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.presentation.model.ChatMessage
import com.kproject.simplechat.presentation.screens.components.EmptyListInfo
import com.kproject.simplechat.presentation.screens.components.LoadingIndicator
import com.kproject.simplechat.presentation.theme.PreviewTheme
import com.kproject.simplechat.presentation.theme.SimplePreview
import com.kproject.simplechat.presentation.theme.TextDefaultColor
import com.kproject.simplechat.presentation.utils.Utils
import kotlinx.coroutines.launch
import com.kproject.simplechat.R
import com.kproject.simplechat.presentation.model.User
import com.kproject.simplechat.presentation.model.fakeChatMessagesList
import com.kproject.simplechat.presentation.screens.components.CustomImage
import com.kproject.simplechat.presentation.screens.home.HomeUiState
import com.kproject.simplechat.presentation.theme.CompletePreview

@Composable
fun ChatScreen(
    onNavigateBack: () -> Unit
) {
    val chatViewModel: ChatViewModel = hiltViewModel()
    val uiState: ChatUiState = chatViewModel.uiState
    val dataState = chatViewModel.dataState


    MainContent(
        uiState = uiState,
        dataState = dataState,
        loggedUserId = "",
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    uiState: ChatUiState,
    dataState: DataState<Unit>,
    loggedUserId: String,
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
                ChatList(
                    chatMessagesList = uiState.chatMessageList,
                    loggedUserId = loggedUserId,
                    modifier = Modifier.padding(18.dp)
                )
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
            .padding(12.dp)
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
                .size(40.dp)
                .clip(CircleShape)
                .clickable {

                }
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
    val messageReceived = loggedUserId == chatMessage.senderId
    val backgroundColor = if (messageReceived) Color.DarkGray else MaterialTheme.colors.secondary
    val alignment = if (messageReceived) Alignment.Start else Alignment.End

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = chatMessage.message,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
                .align(alignment)
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
            loggedUserId = "123",
            onNavigateBack = {}
        )
    }
}