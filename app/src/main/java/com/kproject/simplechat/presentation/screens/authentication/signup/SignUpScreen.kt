package com.kproject.simplechat.presentation.screens.authentication.signup

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kproject.simplechat.R
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.presentation.screens.authentication.components.Button
import com.kproject.simplechat.presentation.screens.authentication.components.FieldType
import com.kproject.simplechat.presentation.screens.authentication.components.TextField
import com.kproject.simplechat.presentation.screens.components.AlertDialog
import com.kproject.simplechat.presentation.screens.components.ProgressAlertDialog
import com.kproject.simplechat.presentation.theme.*
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun SignUpScreen(
    onNavigateToHomeScreen: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val signUpViewModel: SignUpViewModel = hiltViewModel()
    val uiState = signUpViewModel.uiState
    val signUpState = signUpViewModel.signUpState

    MainContent(
        uiState = uiState,
        onProfilePictureChange = { profileImage ->
            signUpViewModel.onEvent(SignUpEvent.ProfilePictureChanged(profileImage))
        },
        onUsernameChange = { username ->
            signUpViewModel.onEvent(SignUpEvent.UsernameChanged(username))
        },
        onEmailChange = { email ->
            signUpViewModel.onEvent(SignUpEvent.EmailChanged(email))
        },
        onPasswordChange = { password ->
            signUpViewModel.onEvent(SignUpEvent.PasswordChanged(password))
        },
        onRepeatedPasswordChange = { password ->
            signUpViewModel.onEvent(SignUpEvent.RepeatedPasswordChanged(password))
        },
        onButtonSignUpClick = {
            signUpViewModel.signUp()
        },
        onNavigateBack = onNavigateBack
    )

    LaunchedEffect(key1 = signUpState) {
        if (signUpState is DataState.Success) {
            onNavigateToHomeScreen.invoke()
        }
    }

    ProgressAlertDialog(showDialog = uiState.isLoading)

    AlertDialog(
        showDialog = uiState.signUpError,
        onDismiss = {
            signUpViewModel.onEvent(SignUpEvent.OnDismissErrorDialog)
        },
        title = stringResource(id = R.string.error),
        message = uiState.signUpErrorMessage.asString(),
        onClickButtonOk = {}
    )
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    uiState: SignUpUiState,
    onProfilePictureChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatedPasswordChange: (String) -> Unit,
    onButtonSignUpClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val spacingHeight = 16.dp

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .padding(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colors.IconColor,
                    modifier = Modifier.size(34.dp)
                )
            }
            Spacer(Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.create_your_account),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.TextDefaultColor
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            ProfilePicture(
                uiState = uiState,
                onProfilePictureChange = { profilePicture ->
                    onProfilePictureChange.invoke(profilePicture)
                }
            )

            Spacer(Modifier.height(16.dp))

            TextField(
                value = uiState.username,
                onValueChange = { value ->
                    onUsernameChange.invoke(value)
                },
                hint = stringResource(id = R.string.user_name),
                leadingIcon = R.drawable.ic_person,
                errorMessage = uiState.usernameError.asString()
            )

            Spacer(Modifier.height(spacingHeight))

            TextField(
                value = uiState.email,
                onValueChange = { value ->
                    onEmailChange.invoke(value)
                },
                hint = stringResource(id = R.string.email),
                leadingIcon = R.drawable.ic_key,
                keyboardType = KeyboardType.Email,
                fieldType = FieldType.Email,
                errorMessage = uiState.emailError.asString()
            )

            Spacer(Modifier.height(spacingHeight))

            TextField(
                value = uiState.password,
                onValueChange = { value ->
                    onPasswordChange.invoke(value)
                },
                hint = stringResource(id = R.string.password),
                leadingIcon = R.drawable.ic_key,
                keyboardType = KeyboardType.Password,
                fieldType = FieldType.Password,
                errorMessage = uiState.passwordError.asString()
            )

            Spacer(Modifier.height(spacingHeight))

            TextField(
                value = uiState.repeatedPassword,
                onValueChange = { value ->
                    onRepeatedPasswordChange.invoke(value)
                },
                hint = stringResource(id = R.string.confirm_password),
                leadingIcon = R.drawable.ic_key,
                keyboardType = KeyboardType.Password,
                fieldType = FieldType.Password,
                errorMessage = uiState.repeatedPasswordError.asString()
            )

            Spacer(Modifier.height(22.dp))

            Button(
                text = stringResource(id = R.string.sign_up),
                onClick = onButtonSignUpClick
            )
        }
    }
}

@Composable
private fun ProfilePicture(
    uiState: SignUpUiState,
    onProfilePictureChange: (String) -> Unit
) {
    val imagePick = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedImageUri ->
            onProfilePictureChange.invoke(selectedImageUri.toString())
        }
    }
    val profilePictureErrorMessage = uiState.profilePictureError.asString()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CoilImage(
            imageModel = uiState.profilePicture.ifEmpty { R.drawable.ic_add_a_photo },
            previewPlaceholder = R.drawable.ic_add_a_photo,
            imageOptions = ImageOptions(
                contentScale = if (uiState.profilePicture.isEmpty()) ContentScale.Inside else ContentScale.Crop,
                colorFilter = if (uiState.profilePicture.isEmpty()) ColorFilter.tint(Color.White) else null
            ),
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.secondary,
                    shape = CircleShape
                )
                .clickable {
                    imagePick.launch("image/*")
                }
                .background(color = MaterialTheme.colors.secondary)
                .padding(if (uiState.profilePicture.isEmpty()) 24.dp else 0.dp)
        )

        if (profilePictureErrorMessage.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = uiState.profilePictureError.asString(),
                color = MaterialTheme.colors.ErrorColor,
                fontSize = 16.sp
            )
        }
    }
}

@CompletePreview
@Composable
private fun Preview() {
    val signUpUiState = SignUpUiState(
        profilePicture = "",
        username = "Simple Chat",
        email = "simplechat@gmail.com.br",
        password = "123456",
        repeatedPassword = "123456"
    )
    PreviewTheme {
        MainContent(
            uiState = signUpUiState,
            onProfilePictureChange = {},
            onUsernameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onRepeatedPasswordChange = {},
            onButtonSignUpClick = {},
            onNavigateBack = {}
        )
    }
}