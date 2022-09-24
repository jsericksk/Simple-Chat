package com.kproject.simplechat.presentation.screens.home.registeredusers

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
import com.kproject.simplechat.presentation.model.User
import com.kproject.simplechat.presentation.model.fakeRegisteredUsersList
import com.kproject.simplechat.presentation.screens.components.CustomImage
import com.kproject.simplechat.presentation.screens.components.EmptyListInfo
import com.kproject.simplechat.presentation.screens.components.LoadingIndicator
import com.kproject.simplechat.presentation.theme.PreviewTheme
import com.kproject.simplechat.presentation.theme.SimplePreview
import com.kproject.simplechat.presentation.theme.TextDefaultColor

@Composable
fun RegisteredUsersScreen(
    onNavigateToChatScreen: () -> Unit,
) {
    val registeredUsersViewModel: RegisteredUsersViewModel = hiltViewModel()
    val uiState = registeredUsersViewModel.uiState
    val dataState = registeredUsersViewModel.dataState

    MainContent(
        uiState = uiState,
        dataState = dataState,
        onNavigateToChatScreen = { index ->

        }
    )
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    uiState: RegisteredUsersUiState,
    dataState: DataState<List<User>>,
    onNavigateToChatScreen: (index: Int) -> Unit
) {
    when (dataState) {
        DataState.Loading -> {
            LoadingIndicator(
                modifier = modifier
            )
        }
        is DataState.Success -> {
            RegisteredUsersList(
                registeredUsersList = uiState.registeredUsers,
                onClick = { index ->
                    onNavigateToChatScreen.invoke(index)
                },
                modifier = modifier
            )
        }
        is DataState.Error -> {
            EmptyListInfo(
                iconResId = R.drawable.ic_person_off,
                title = stringResource(id = R.string.error),
                description = stringResource(id = R.string.error_get_user_list),
                modifier = modifier
            )
        }
    }
}

@Composable
private fun RegisteredUsersList(
    modifier: Modifier = Modifier,
    registeredUsersList: List<User>,
    onClick: (index: Int) -> Unit
) {
    if (registeredUsersList.isNotEmpty()) {
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            itemsIndexed(registeredUsersList) { index, user ->
                RegisteredUsersListItem(
                    user = user,
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
private fun RegisteredUsersListItem(
    user: User,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            CustomImage(
                imageModel = user.profilePicture,
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
                    text = user.username,
                    color = MaterialTheme.colors.TextDefaultColor,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    fontSize = 18.sp,
                )
            }

            Text(
                text = user.formattedRegistrationDate,
                color = MaterialTheme.colors.TextDefaultColor,
                fontSize = 12.sp
            )
        }
    }
}

@SimplePreview
@Composable
private fun Preview() {
    PreviewTheme {
        val uiState = RegisteredUsersUiState(
            registeredUsers = fakeRegisteredUsersList
        )
        MainContent(
            uiState = uiState,
            dataState = DataState.Success(),
            onNavigateToChatScreen = {}
        )
    }
}