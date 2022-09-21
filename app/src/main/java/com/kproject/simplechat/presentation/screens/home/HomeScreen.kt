package com.kproject.simplechat.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.kproject.simplechat.presentation.navigation.Screen
import com.kproject.simplechat.presentation.screens.authentication.login.LoginUiState
import com.kproject.simplechat.presentation.screens.home.latestmessages.LatestMessagesScreen

@Composable
fun HomeScreen(
    onNavigateToChatScreen: () -> Unit,
) {
    MainContent()
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
) {
    val pages = listOf("Chats", "Users")
    val pagerState = rememberPagerState()

    Column() {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            }
        ) {
            pages.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = pagerState.currentPage == index,
                    onClick = { /* TODO */ },
                )
            }
        }

        HorizontalPager(
            count = pages.size,
            state = pagerState,
        ) { page ->
           LatestMessagesScreen {

           }
        }
    }
}