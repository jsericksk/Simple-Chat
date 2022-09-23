package com.kproject.simplechat.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.kproject.simplechat.R
import com.kproject.simplechat.presentation.MainViewModel
import com.kproject.simplechat.presentation.screens.components.CustomImage
import com.kproject.simplechat.presentation.screens.home.latestmessages.LatestMessagesScreen
import com.kproject.simplechat.presentation.screens.home.registeredUserss.RegisteredUsersScreen
import com.kproject.simplechat.presentation.theme.CompletePreview
import com.kproject.simplechat.presentation.theme.PreviewTheme
import kotlinx.coroutines.launch

private const val PageChat = 0
private const val PageUsers = 1

@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    onNavigateToChatScreen: () -> Unit,
) {
    val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()

    MainContent(
        isDarkMode = mainViewModel.isDarkMode,
        uiState = homeScreenViewModel.uiState,
        onChangeTheme = {
            mainViewModel.changeTheme()
        },
        onNavigateToChatScreen = {

        }

    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    isDarkMode: Boolean,
    onChangeTheme: () -> Unit,
    onNavigateToChatScreen: () -> Unit,
) {
    val pages = listOf(
        stringResource(id = R.string.chat),
        stringResource(id = R.string.users)
    )
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        TopBar(
            userProfilePicture = uiState.userProfilePicture,
            isDarkMode = isDarkMode,
            onChangeTheme = onChangeTheme
        )

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            },
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = Color.White
        ) {
            pages.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        }

        HorizontalPager(
            count = pages.size,
            state = pagerState,
        ) { page ->
            when (page) {
                PageChat -> {
                    LatestMessagesScreen(
                        onNavigateToChatScreen = {

                        }
                    )
                }
                PageUsers -> {
                    RegisteredUsersScreen(
                        onNavigateToChatScreen = {

                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    userProfilePicture: String,
    isDarkMode: Boolean,
    onChangeTheme: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .padding(12.dp)
    ) {
        CustomImage(
            imageModel = userProfilePicture.ifEmpty { R.drawable.ic_person },
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp)),
            colorFilter = if (userProfilePicture.isEmpty()) ColorFilter.tint(Color.White) else null
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(id = R.string.app_name),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        val iconResId = if (isDarkMode) R.drawable.ic_dark_mode else R.drawable.ic_light_mode
        IconButton(onClick = onChangeTheme) {
            Icon(
                imageVector = ImageVector.vectorResource(id = iconResId),
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
        MainContent(
            uiState = HomeUiState(),
            isDarkMode = true,
            onChangeTheme = {},
            onNavigateToChatScreen = {}
        )
    }
}