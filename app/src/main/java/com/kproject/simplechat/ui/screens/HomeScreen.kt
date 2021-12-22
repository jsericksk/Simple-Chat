package com.kproject.simplechat.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.auth.FirebaseAuth
import com.kproject.simplechat.R
import com.kproject.simplechat.data.DataStateResult
import com.kproject.simplechat.model.LastMessage
import com.kproject.simplechat.model.User
import com.kproject.simplechat.ui.viewmodels.HomeViewModel
import com.kproject.simplechat.utils.Utils

@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun HomeScreen(
    navigateToChatScreen: (userId: String, userName: String, userProfileImage: String) -> Unit,
    navigateToLoginScreen: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val logout by homeViewModel.logout.observeAsState(false)

    val showLogoutConfirmationDialog = rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.app_name)) },
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White,
                    actions = {
                        IconButton(
                            onClick = {
                                homeViewModel.logout()
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_logout),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    elevation = 0.dp
                )
            },
            modifier = Modifier.weight(1f)
        ) {
            HomeTabs(navigateToChatScreen, homeViewModel)
        }
    }

    if (logout) {
        navigateToLoginScreen.invoke()
    }
}

@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun HomeTabs(
    navigateToChatScreen: (userId: String, userName: String, userProfileImage: String) -> Unit,
    homeViewModel: HomeViewModel
) {
    var tabIndex by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState()

    val pages = listOf(
        stringResource(id = R.string.chat),
        stringResource(id = R.string.users)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            }
        ) {
            pages.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = {
                        tabIndex = index
                    }
                )
            }
        }

        HorizontalPager(
            count = pages.size,
            state = pagerState,
        ) { page ->
            if (page == 0) {
                LatestMessagesTab(navigateToChatScreen, homeViewModel)
            } else {
                UsersTab(navigateToChatScreen, homeViewModel)
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun LatestMessagesTab(
    navigateToChatScreen: (userId: String, userName: String, userProfileImage: String) -> Unit,
    homeViewModel: HomeViewModel
) {
    val context = LocalContext.current
    val latestMessageListState by homeViewModel.latestMessageListState.observeAsState()

    var isRequestFinished by rememberSaveable { mutableStateOf(false) }

    /**
     * Used to avoid making multiple calls on recompositions.
     */
    LaunchedEffect(Unit) {
        if (!isRequestFinished) {
            homeViewModel.getLatestMessages()
        }
    }

    when (latestMessageListState) {
        is DataStateResult.Loading -> {

        }
        is DataStateResult.Success -> {
            isRequestFinished = true
            latestMessageListState?.let { messageList ->
                LazyColumn(
                    Modifier.fillMaxSize()
                ) {
                    itemsIndexed(messageList.data!!) { index, lastMessage ->
                        LastMessageItem(
                            lastMessage = lastMessage,
                            navigateToChatScreen = navigateToChatScreen
                        )
                    }
                }
            }
        }
        is DataStateResult.Error -> {
            isRequestFinished = true
        }
    }
}

@ExperimentalCoilApi
@Composable
fun UsersTab(
    navigateToChatScreen: (userId: String, userName: String, userProfileImage: String) -> Unit,
    homeViewModel: HomeViewModel
) {
    val context = LocalContext.current
    val registeredUsersListState by homeViewModel.registeredUsersListState.observeAsState()

    var isRequestFinished by rememberSaveable { mutableStateOf(false) }

    /**
     * Used to avoid making multiple calls on recompositions.
     */
    LaunchedEffect(Unit) {
        if (!isRequestFinished) {
            homeViewModel.getRegisteredUserList()
        }
    }

    when (registeredUsersListState) {
        is DataStateResult.Loading -> {
            // SimpleProgressDialog(showDialog = showProgressDialog)
        }
        is DataStateResult.Success -> {
            isRequestFinished = true
            registeredUsersListState?.let { userListState ->
                LazyColumn(
                    Modifier.fillMaxSize()
                ) {
                    itemsIndexed(userListState.data!!) { index, user ->
                        UserItem(
                            user = user,
                            navigateToChatScreen = navigateToChatScreen
                        )
                    }
                }
            }
        }
        is DataStateResult.Error -> {
            isRequestFinished = true

        }
    }
}

@ExperimentalCoilApi
@Composable
fun LastMessageItem(
    lastMessage: LastMessage,
    navigateToChatScreen: (userId: String, userName: String, userProfileImage: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val currentUserId = Utils.getCurrentUserId()
                    val userId = if (currentUserId == lastMessage.senderId)
                        lastMessage.receiverId else lastMessage.senderId

                    navigateToChatScreen.invoke(
                        userId,
                        lastMessage.userName,
                        Uri.encode(lastMessage.userProfileImage)
                    )
                }
        ) {
            Image(
                painter = rememberImagePainter(
                    data = lastMessage.userProfileImage
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Transparent, CircleShape)
                    .fillMaxSize()
            )

            Spacer(Modifier.padding(start = 8.dp))

            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)) {
                Text(
                    modifier = Modifier,
                    text = lastMessage.userName,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    fontSize = 18.sp
                )

                Text(
                    text = lastMessage.lastMessage,
                    color = if (lastMessage.senderId == FirebaseAuth.getInstance().currentUser?.uid)
                        Color.DarkGray else Color.Blue,
                    maxLines = 1,
                    fontSize = 16.sp
                )
            }

            Text(
                text = Utils.getFormattedDate(lastMessage.timestamp),
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun UserItem(
    user: User,
    navigateToChatScreen: (userId: String, userName: String, userProfileImage: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navigateToChatScreen.invoke(
                        user.userId,
                        user.userName,
                        Uri.encode(user.profileImage)
                    )
                }
            ) {
            Image(
                painter = rememberImagePainter(
                    data = user.profileImage
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Transparent, CircleShape)
                    .fillMaxSize()
            )

            Spacer(Modifier.padding(start = 8.dp))

            Text(
                modifier = Modifier,
                text = user.userName,
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                fontSize = 18.sp
            )
        }
    }
}


