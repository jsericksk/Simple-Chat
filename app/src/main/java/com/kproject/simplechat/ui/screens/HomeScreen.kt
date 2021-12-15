package com.kproject.simplechat.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.kproject.simplechat.R

@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun HomeScreen(
    navigateToChatScreen: (userId: String, userName: String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.app_name)) },
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White,
                    elevation = 0.dp
                )
            },
            modifier = Modifier.weight(1f)
        ) {
            HomeTabs(navigateToChatScreen)
        }
    }
}

@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun HomeTabs(
    navigateToChatScreen: (userId: String, userName: String) -> Unit
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
                LastMessagesTab(navigateToChatScreen)
            } else {
                UsersTab(navigateToChatScreen)
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun LastMessagesTab(
    navigateToChatScreen: (userId: String, userName: String) -> Unit
) {
    val items = listOf<Int>(
        1, 2, 4, 5, 6, 7, 8, 9, 10,
        1, 2, 4, 5, 6, 7, 8, 9, 10
    )

    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        itemsIndexed(items) { index, message ->
            MessageItem(
                profileImage = "",
                userName = "Naruto",
                lastMessage = "Hello, Naruto! How are you?",
                date = "12/12/21",
                navigateToChatScreen = navigateToChatScreen
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun UsersTab(
    navigateToChatScreen: (userId: String, userName: String) -> Unit
) {
    val items = listOf<Int>(
        1, 2, 4, 5, 6, 7, 8, 9, 10,
        1, 2, 4, 5, 6, 7, 8, 9, 10
    )

    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        itemsIndexed(items) { index, user ->
            UserItem(
                profileImage = "",
                userName = "Naruto",
                navigateToChatScreen = navigateToChatScreen
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun MessageItem(
    profileImage: String,
    userName: String,
    lastMessage: String,
    date: String,
    navigateToChatScreen: (userId: String, userName: String) -> Unit
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
                    navigateToChatScreen.invoke("opa", userName)
                }
        ) {
            Image(
                painter = rememberImagePainter(
                    data = R.drawable.naruto
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
                    text = userName,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    fontSize = 18.sp
                )

                Text(
                    text = lastMessage,
                    color = Color.DarkGray,
                    maxLines = 1,
                    fontSize = 16.sp
                )
            }

            Text(
                text = date,
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun UserItem(
    profileImage: String,
    userName: String,
    navigateToChatScreen: (userId: String, userName: String) -> Unit
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
                .clickable { navigateToChatScreen.invoke("opa", userName) }
            ) {
            Image(
                painter = rememberImagePainter(
                    data = R.drawable.naruto
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
                text = userName,
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                fontSize = 18.sp
            )
        }
    }
}


