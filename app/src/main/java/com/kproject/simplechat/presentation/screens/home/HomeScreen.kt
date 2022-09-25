package com.kproject.simplechat.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.kproject.simplechat.R
import com.kproject.simplechat.presentation.MainViewModel
import com.kproject.simplechat.presentation.model.User
import com.kproject.simplechat.presentation.screens.components.CustomImage
import com.kproject.simplechat.presentation.screens.home.latestmessages.LatestMessagesScreen
import com.kproject.simplechat.presentation.screens.home.registeredusers.RegisteredUsersScreen
import com.kproject.simplechat.presentation.theme.CompletePreview
import com.kproject.simplechat.presentation.theme.PreviewTheme
import com.kproject.simplechat.presentation.theme.TextDefaultColor
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

        },
        onLogout = {
            homeScreenViewModel.logout()
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
    onLogout: () -> Unit,
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
            uiState = uiState,
            isDarkMode = isDarkMode,
            onChangeTheme = onChangeTheme,
            onLogout = onLogout
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
    uiState: HomeUiState,
    isDarkMode: Boolean,
    onChangeTheme: () -> Unit,
    onLogout: () -> Unit
) {
    var showProfileViewerDialog by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .padding(12.dp)
    ) {
        CustomImage(
            imageModel = uiState.user.profilePicture.ifEmpty { R.drawable.ic_person },
            colorFilter = if (uiState.user.profilePicture.isEmpty()) ColorFilter.tint(Color.White) else null,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    showProfileViewerDialog = true
                }
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

    ProfileViewerDialog(
        showDialog = showProfileViewerDialog,
        onDismiss = { showProfileViewerDialog = false },
        uiState = uiState,
        onLogout = onLogout
    )
}

@Composable
private fun ProfileViewerDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    uiState: HomeUiState,
    onLogout: () -> Unit
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = { onDismiss.invoke() },
            content = {
                Column(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colors.background,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .height(120.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomImage(
                            imageModel = uiState.user.profilePicture.ifEmpty { R.drawable.ic_person },
                            colorFilter = if (uiState.user.profilePicture.isEmpty())
                                ColorFilter.tint(Color.White) else null,
                            modifier = Modifier
                                .size(120.dp)
                                .padding(14.dp)
                                .clip(CircleShape)
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = uiState.user.username.ifEmpty { stringResource(id = R.string.unknown_user) },
                            color = MaterialTheme.colors.TextDefaultColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = stringResource(id = R.string.registration_date),
                            color = MaterialTheme.colors.TextDefaultColor,
                            fontSize = 16.sp
                        )

                        Spacer(Modifier.height(4.dp))

                        val registrationDate = uiState.user.registrationDate?.let {
                            uiState.user.formattedRegistrationDate
                        } ?: stringResource(id = R.string.unknown_registration_date)

                        Text(
                            text = registrationDate,
                            color = MaterialTheme.colors.TextDefaultColor,
                            fontSize = 16.sp
                        )

                        Spacer(Modifier.height(24.dp))

                        Button(
                            onClick = onLogout,
                            shape = CircleShape,
                            contentPadding = PaddingValues(14.dp),
                        ) {
                            Text(
                                text = stringResource(id = R.string.logout),
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        )
    }
}

@CompletePreview
@Composable
private fun ProfileViewerDialogPreview() {
    PreviewTheme {
        val uiState = HomeUiState(
            user = User(username = "Simple Chat")
        )
        ProfileViewerDialog(
            showDialog = true,
            onDismiss = {},
            uiState = uiState,
            onLogout = {}
        )
    }
}