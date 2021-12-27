package com.kproject.simplechat.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asLiveData
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
import com.kproject.simplechat.ui.screens.components.EmptyListInfo
import com.kproject.simplechat.ui.screens.components.LoadingProgressIndicator
import com.kproject.simplechat.ui.screens.components.SimpleDialog
import com.kproject.simplechat.ui.theme.TextDefaultColor
import com.kproject.simplechat.ui.viewmodels.HomeViewModel
import com.kproject.simplechat.utils.DataStoreUtils
import com.kproject.simplechat.utils.PrefsConstants
import com.kproject.simplechat.utils.Utils
import kotlinx.coroutines.launch

@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun HomeScreen(
    navigateToChatScreen: (userId: String, userName: String, userProfileImage: String) -> Unit,
    navigateToLoginScreen: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val showLogoutConfirmationDialog = rememberSaveable { mutableStateOf(false) }
    val showAppThemeOptionDialog = rememberSaveable { mutableStateOf(false) }

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
                                showLogoutConfirmationDialog.value = true
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_logout),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                        IconButton(
                            onClick = {
                                showAppThemeOptionDialog.value = true
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_dark_mode),
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
    
    AppThemeOption(showDialog = showAppThemeOptionDialog)
    
    SimpleDialog(
        showDialog = showLogoutConfirmationDialog,
        titleResId = R.string.dialog_title_confirm_logout,
        messageResId = R.string.dialog_message_confirm_logout,
        onClickButtonOk = {
            homeViewModel.logout()
            showLogoutConfirmationDialog.value = false
            navigateToLoginScreen.invoke()
        },
        onClickButtonCancel = { showLogoutConfirmationDialog.value = false }
    )
}

@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun HomeTabs(
    navigateToChatScreen: (userId: String, userName: String, userProfileImage: String) -> Unit,
    homeViewModel: HomeViewModel
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

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
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.scrollToPage(index)
                        }
                    }
                )
            }
        }

        HorizontalPager(
            count = pages.size,
            state = pagerState
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
            LoadingProgressIndicator()
        }
        is DataStateResult.Success -> {
            isRequestFinished = true
            latestMessageListState?.let { result ->
                val latestMessageList = result.data!!
                Column {
                    if (latestMessageList.isNotEmpty()) {
                        LazyColumn(
                            Modifier.fillMaxSize()
                        ) {
                            itemsIndexed(latestMessageList) { index, lastMessage ->
                                LastMessageItem(
                                    lastMessage = lastMessage,
                                    navigateToChatScreen = navigateToChatScreen
                                )
                            }
                        }
                    } else {
                        EmptyListInfo(
                            iconResId = R.drawable.ic_chat,
                            messageResId = R.string.info_empty_last_message_list,
                            errorMessageResId = R.string.info_message_empty_last_message_list
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
            LoadingProgressIndicator()
        }
        is DataStateResult.Success -> {
            isRequestFinished = true
            registeredUsersListState?.let { result ->
                val registeredUsersList = result.data!!
                if (registeredUsersList.isNotEmpty()) {
                    LazyColumn(
                        Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(registeredUsersList) { index, user ->
                            UserItem(
                                user = user,
                                navigateToChatScreen = navigateToChatScreen
                            )
                        }
                    }
                } else {
                    EmptyListInfo(
                        iconResId = R.drawable.ic_person,
                        messageResId = R.string.info_empty_registered_user_list,
                        errorMessageResId = R.string.info_message_empty_registered_user_list
                    )
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
                    color = MaterialTheme.colors.TextDefaultColor,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    fontSize = 18.sp
                )

                Text(
                    text = lastMessage.lastMessage,
                    color = if (lastMessage.senderId == FirebaseAuth.getInstance().currentUser?.uid)
                        MaterialTheme.colors.TextDefaultColor else MaterialTheme.colors.primary,
                    maxLines = 1,
                    fontSize = 16.sp
                )
            }

            Text(
                text = Utils.getFormattedDate(lastMessage.timestamp),
                color = MaterialTheme.colors.TextDefaultColor,
                fontSize = 12.sp
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
                color = MaterialTheme.colors.TextDefaultColor,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun AppThemeOption(
    showDialog: MutableState<Boolean>
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var selectedTheme by remember { mutableStateOf(0) }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = {
                Text(
                    text = stringResource(id = R.string.app_theme),
                    color = MaterialTheme.colors.TextDefaultColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                ThemeOptions {
                    selectedTheme = it
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog.value = false
                        coroutineScope.launch {
                            DataStoreUtils.savePreference(
                                context = context,
                                key = PrefsConstants.APP_THEME,
                                value = selectedTheme
                            )
                        }
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.button_ok),
                        color = MaterialTheme.colors.onSecondary
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog.value = false
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.button_cancel).uppercase(),
                        color = MaterialTheme.colors.onSecondary
                    )
                }
            }
        )
    }
    
}

@Composable
fun ThemeOptions(
    selectedTheme: (Int) -> Unit
) {
    val context = LocalContext.current

    val appThemeState by DataStoreUtils.readPreference(
        context = context,
        key = PrefsConstants.APP_THEME,
        defaultValue = PrefsConstants.THEME_SYSTEM_DEFAULT
    ).asLiveData().observeAsState(
        // Gets an initial value without Flow so there is no small delay
        initial = DataStoreUtils.readPreferenceWithoutFlow(
            context = context,
            key = PrefsConstants.APP_THEME,
            defaultValue = PrefsConstants.THEME_SYSTEM_DEFAULT
        )
    )

    val radioButtonOptions = listOf(
        stringResource(id = R.string.theme_system),
        stringResource(id = R.string.theme_light),
        stringResource(id = R.string.theme_dark)
    )

    var currentSelectedOption by rememberSaveable { mutableStateOf(appThemeState) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column {
            radioButtonOptions.forEachIndexed { index, text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = currentSelectedOption == index,
                            onClick = {
                                selectedTheme.invoke(index)
                                currentSelectedOption = index
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currentSelectedOption == index,
                        modifier = Modifier.padding(all = Dp(value = 8F)),
                        onClick = {
                            selectedTheme.invoke(index)
                            currentSelectedOption = index
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colors.onSecondary
                        )
                    )

                    Text(
                        text = text,
                        color = MaterialTheme.colors.TextDefaultColor,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}