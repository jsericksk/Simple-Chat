package com.kproject.simplechat.presentation.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.kproject.simplechat.R
import com.kproject.simplechat.presentation.MainViewModel
import com.kproject.simplechat.presentation.model.User
import com.kproject.simplechat.presentation.screens.components.AlertDialog
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
    onNavigateToLoginScreen: () -> Unit,
    onNavigateToChatScreen: (User, String) -> Unit,
) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val uiState = homeViewModel.uiState

    MainContent(
        isDarkMode = mainViewModel.isDarkMode,
        uiState = uiState,
        onChangeTheme = {
            mainViewModel.changeTheme()
        },
        onNavigateToChatScreen = { user ->
            val loggedUserId = uiState.user.userId
            onNavigateToChatScreen.invoke(user, loggedUserId)
        },
        onLogout = {
            homeViewModel.logout()
            onNavigateToLoginScreen.invoke()
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
    onNavigateToChatScreen: (User) -> Unit,
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
                        onNavigateToChatScreen = { user ->
                            onNavigateToChatScreen.invoke(user)
                        }
                    )
                }
                PageUsers -> {
                    RegisteredUsersScreen(
                        onNavigateToChatScreen = { user ->
                            onNavigateToChatScreen.invoke(user)
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
    var showLogoutConfirmDialog by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    showProfileViewerDialog = true
                }
                .padding(6.dp)
        ) {
            CustomImage(
                imageModel = uiState.user.profilePicture.ifEmpty { R.drawable.ic_person },
                colorFilter = if (uiState.user.profilePicture.isEmpty()) ColorFilter.tint(Color.White) else null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.app_name),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.weight(1f))

        val iconResId = if (isDarkMode) R.drawable.ic_light_mode else R.drawable.ic_dark_mode
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
        onLogout = {
            showProfileViewerDialog = false
            showLogoutConfirmDialog = true
        }
    )

    AlertDialog(
        showDialog = showLogoutConfirmDialog,
        onDismiss = { showLogoutConfirmDialog = false },
        title = stringResource(id = R.string.dialog_title_confirm_logout),
        message = stringResource(id = R.string.dialog_message_confirm_logout),
        onClickButtonOk = onLogout
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ProfileViewerDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    uiState: HomeUiState,
    onLogout: () -> Unit
) {
    if (showDialog) {
        val shapeSize = 24.dp
        var showProfilePicture by remember { mutableStateOf(false) }

        Dialog(
            onDismissRequest = { onDismiss.invoke() },
            content = {
                Column(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colors.background,
                            shape = RoundedCornerShape(shapeSize)
                        )
                ) {
                    AnimatedVisibility(
                        visible = showProfilePicture,
                        enter = scaleIn(),
                        exit = scaleOut()
                    ) {
                        ProfilePictureViewer(
                            profilePicture = uiState.user.profilePicture,
                            onDismiss = { showProfilePicture = false },
                            modifier = Modifier.clip(RoundedCornerShape(shapeSize))
                        )
                    }

                    ConstraintLayout {
                        val (box, image) = createRefs()
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(
                                    color = MaterialTheme.colors.secondary,
                                    shape = RoundedCornerShape(
                                        topStart = shapeSize,
                                        topEnd = shapeSize
                                    )
                                )
                                .constrainAs(box) {
                                    top.linkTo(parent.top)
                                }
                        )

                        CustomImage(
                            imageModel = uiState.user.profilePicture.ifEmpty { R.drawable.ic_person },
                            colorFilter = if (uiState.user.profilePicture.isEmpty())
                                ColorFilter.tint(Color.White) else null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .constrainAs(image) {
                                    top.linkTo(box.bottom)
                                    bottom.linkTo(box.bottom)
                                    start.linkTo(box.start)
                                    end.linkTo(box.end)
                                }
                                .clickable { showProfilePicture = true }
                        )
                    }

                    Spacer(Modifier.height(6.dp))

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = uiState.user.username.ifEmpty { stringResource(id = R.string.unknown_user) },
                            color = MaterialTheme.colors.TextDefaultColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(8.dp))

                        val registrationDate = if (uiState.user.registrationDate != null) {
                            stringResource(
                                id = R.string.user_registration_date,
                                formatArgs = arrayOf(uiState.user.formattedRegistrationDate)
                            )
                        } else {
                            stringResource(id = R.string.unknown_registration_date)
                        }

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

@Composable
private fun ProfilePictureViewer(
    modifier: Modifier = Modifier,
    profilePicture: String,
    onDismiss: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        CustomImage(
            imageModel = profilePicture.ifEmpty { R.drawable.ic_person },
            colorFilter = if (profilePicture.isEmpty()) ColorFilter.tint(Color.White) else null,
        )

        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .background(
                    color = MaterialTheme.colors.secondary,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = Color.White
            )
        }
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
