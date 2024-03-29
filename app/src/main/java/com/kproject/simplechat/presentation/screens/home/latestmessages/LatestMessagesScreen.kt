package com.kproject.simplechat.presentation.screens.home.latestmessages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kproject.simplechat.R
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.presentation.model.LatestMessage
import com.kproject.simplechat.presentation.model.User
import com.kproject.simplechat.presentation.model.fakeLatestMessagesList
import com.kproject.simplechat.presentation.screens.components.CustomImage
import com.kproject.simplechat.presentation.screens.components.EmptyListInfo
import com.kproject.simplechat.presentation.screens.components.LoadingIndicator
import com.kproject.simplechat.presentation.theme.CompletePreview
import com.kproject.simplechat.presentation.theme.PreviewTheme
import com.kproject.simplechat.presentation.theme.TextDefaultColor

@Composable
fun LatestMessagesScreen(
    onNavigateToChatScreen: (user: User) -> Unit,
) {
    val latestMessagesViewModel: LatestMessagesViewModel = hiltViewModel()
    val uiState = latestMessagesViewModel.uiState
    val dataState = latestMessagesViewModel.dataState

    MainContent(
        uiState = uiState,
        dataState = dataState,
        onNavigateToChatScreen = { index ->
            val message = uiState.latestMessages[index]
            val user = User(
                userId = message.chatId,
                username = message.username,
                profilePicture = message.userProfilePicture,
            )
            onNavigateToChatScreen.invoke(user)
        }
    )
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    uiState: LatestMessagesUiState,
    dataState: DataState<Unit>,
    onNavigateToChatScreen: (index: Int) -> Unit
) {
    when (dataState) {
        DataState.Loading -> {
            LoadingIndicator(
                modifier = modifier
            )
        }
        is DataState.Success -> {
            LatestMessagesList(
                latestMessageList = uiState.latestMessages,
                loggedUserId = uiState.loggedUserId,
                onClick = { index ->
                    onNavigateToChatScreen.invoke(index)
                },
                modifier = modifier
            )
        }
        is DataState.Error -> {
            EmptyListInfo(
                iconResId = R.drawable.ic_message,
                title = stringResource(id = R.string.error),
                description = stringResource(id = R.string.error_get_latest_messages_list),
                modifier = modifier
            )
        }
    }
}

@Composable
private fun LatestMessagesList(
    modifier: Modifier = Modifier,
    latestMessageList: List<LatestMessage>,
    loggedUserId: String,
    onClick: (index: Int) -> Unit
) {
    if (latestMessageList.isNotEmpty()) {
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            itemsIndexed(latestMessageList) { index, latestMessage ->
                LatestMessagesListItem(
                    latestMessage = latestMessage,
                    loggedUserId = loggedUserId,
                    onClick = {
                        onClick(index)
                    }
                )
            }
        }
    } else {
        EmptyListInfo(
            iconResId = R.drawable.ic_message,
            title = stringResource(id = R.string.info_title_empty_latest_messages_list),
            description = stringResource(id = R.string.info_description_empty_latest_messages_list)
        )
    }
}

@Composable
private fun LatestMessagesListItem(
    latestMessage: LatestMessage,
    loggedUserId: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CustomImage(
                imageModel = latestMessage.userProfilePicture,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .fillMaxSize()
            )

            Spacer(Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = latestMessage.username,
                    color = MaterialTheme.colors.TextDefaultColor,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    fontSize = 18.sp,
                )
                Spacer(Modifier.height(4.dp))
                val messageTextColor = if (latestMessage.senderId != loggedUserId) {
                    MaterialTheme.colors.secondary
                } else {
                    MaterialTheme.colors.TextDefaultColor
                }
                Text(
                    text = latestMessage.latestMessage,
                    color = messageTextColor,
                    maxLines = 1,
                    fontSize = 16.sp
                )
            }

            Text(
                text = latestMessage.formattedDate,
                color = MaterialTheme.colors.TextDefaultColor,
                fontSize = 12.sp
            )
        }
    }
}

@CompletePreview
@Composable
private fun Preview() {
    PreviewTheme {
        val uiState = LatestMessagesUiState(
            latestMessages = fakeLatestMessagesList,
            loggedUserId = "123456"
        )
        MainContent(
            uiState = uiState,
            dataState = DataState.Success(),
            onNavigateToChatScreen = {}
        )
    }
}