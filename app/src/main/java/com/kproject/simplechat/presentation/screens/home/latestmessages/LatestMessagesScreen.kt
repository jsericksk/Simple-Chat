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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kproject.simplechat.R
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.presentation.model.LatestMessage
import com.kproject.simplechat.presentation.screens.components.CustomImage
import com.kproject.simplechat.presentation.screens.components.EmptyListInfo
import com.kproject.simplechat.presentation.theme.PreviewTheme
import com.kproject.simplechat.presentation.theme.SimplePreview
import com.kproject.simplechat.presentation.theme.TextDefaultColor

@Composable
fun LatestMessagesScreen(
    onNavigateToChatScreen: () -> Unit,

) {
    val latestMessagesViewModel: LatestMessagesViewModel = viewModel()
    val uiState = latestMessagesViewModel.uiState
    val dataState = latestMessagesViewModel.dataState

    MainContent(
        uiState = uiState,
        dataState = DataState.Success(),
        onNavigateToChatScreen = {

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

        }
        is DataState.Success -> {
            LatestMessagesList(
                latestMessageList = uiState.latestMessagesList,
                onClick = { index ->
                    onNavigateToChatScreen.invoke(index)
                }
            )
        }
        is DataState.Error -> {
            EmptyListInfo(
                iconResId = R.drawable.ic_message,
                title = stringResource(id = R.string.error),
                description = stringResource(id = R.string.error_get_latest_messages_list)
            )
        }
    }
}

@Composable
private fun LatestMessagesList(
    modifier: Modifier = Modifier,
    latestMessageList: List<LatestMessage>,
    onClick: (index: Int) -> Unit
) {
    Column {
        if (latestMessageList.isNotEmpty()) {
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                itemsIndexed(latestMessageList) { index, latestMessage ->
                    LatestMessagesListItem(
                        latestMessage = latestMessage,
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
}

@Composable
private fun LatestMessagesListItem(
    latestMessage: LatestMessage,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
        ) {
            CustomImage(
                imageModel = latestMessage,
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

                Text(
                    text = latestMessage.latestMessage,
                    color = MaterialTheme.colors.TextDefaultColor,
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

@SimplePreview
@Composable
private fun LatestMessagesListItemPreview() {
    PreviewTheme {
        val latestMessage = LatestMessage(
            chatId = "",
            latestMessage = "Hello my friend",
            senderId = "123456",
            receiverId = "123456789",
            username = "Simple Chat",
            userProfilePicture = "",
            timestamp = null
        )

        LatestMessagesListItem(
            latestMessage = latestMessage,
            onClick = {}
        )
    }
}