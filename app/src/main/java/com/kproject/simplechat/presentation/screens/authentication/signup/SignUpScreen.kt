package com.kproject.simplechat.presentation.screens.authentication.signup

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kproject.simplechat.R
import com.kproject.simplechat.presentation.screens.authentication.components.Button
import com.kproject.simplechat.presentation.screens.authentication.components.FieldType
import com.kproject.simplechat.presentation.screens.authentication.components.TextField
import com.kproject.simplechat.presentation.theme.PreviewTheme
import com.kproject.simplechat.presentation.theme.CompletePreview
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun SignUpScreen(
    onNavigateToHomeScreen: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    val signUpViewModel: SignUpViewModel = viewModel()
    val signUpUiState = signUpViewModel.signUpUiState

    MainContent(
        signUpUiState = signUpUiState,
        onProfileImageChange = { profileImage ->
            signUpViewModel.onProfileImageChange(profileImage)
        },
        onUsernameChange = { username ->
            signUpViewModel.onUsernameChange(username)
        },
        onEmailChange = { email ->
            signUpViewModel.onEmailChange(email)
        },
        onPasswordChange = { password ->
            signUpViewModel.onPasswordChange(password)
        },
        onConfirmedPasswordChange = { password ->
            signUpViewModel.onConfirmedPasswordChange(password)
        },
        onButtonSignUpClick = {

        },
    )
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    signUpUiState: SignUpUiState,
    onProfileImageChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmedPasswordChange: (String) -> Unit,
    onButtonSignUpClick: () -> Unit
) {
    val spacingHeight = 20.dp

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.secondary,
                    shape = RoundedCornerShape(bottomStart = 34.dp, bottomEnd = 34.dp)
                )
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Text(
                text = stringResource(id = R.string.create_your_account),
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFDDDDDD),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(Modifier.height(spacingHeight))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(24.dp)
        ) {
            ProfileImage(
                signUpUiState = signUpUiState,
                onProfileImageChange = { profileImage ->
                    onProfileImageChange.invoke(profileImage)
                }
            )

            Spacer(Modifier.height(spacingHeight))

            TextField(
                value = signUpUiState.username,
                onValueChange = { value ->
                    onUsernameChange.invoke(value)
                },
                hint = stringResource(id = R.string.user_name),
                leadingIcon = R.drawable.ic_person
            )

            Spacer(Modifier.height(spacingHeight))

            TextField(
                value = signUpUiState.email,
                onValueChange = { value ->
                    onEmailChange.invoke(value)
                },
                hint = stringResource(id = R.string.email),
                leadingIcon = R.drawable.ic_key,
                keyboardType = KeyboardType.Email,
                fieldType = FieldType.Email,
            )

            Spacer(Modifier.height(spacingHeight))

            TextField(
                value = signUpUiState.password,
                onValueChange = { value ->
                    onPasswordChange.invoke(value)
                },
                hint = stringResource(id = R.string.password),
                leadingIcon = R.drawable.ic_key,
                keyboardType = KeyboardType.Password,
                fieldType = FieldType.Password,
            )

            Spacer(Modifier.height(spacingHeight))

            TextField(
                value = signUpUiState.confirmedPassword,
                onValueChange = { value ->
                    onConfirmedPasswordChange.invoke(value)
                },
                hint = stringResource(id = R.string.confirm_password),
                leadingIcon = R.drawable.ic_key,
                keyboardType = KeyboardType.Password,
                fieldType = FieldType.Password,
            )

            Spacer(Modifier.height(spacingHeight))

            Button(
                text = stringResource(id = R.string.sign_up),
                onClick = onButtonSignUpClick
            )
        }
    }
}

@Composable
private fun ProfileImage(
    signUpUiState: SignUpUiState,
    onProfileImageChange: (String) -> Unit,
) {
    val imagePick = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedImageUri ->
            onProfileImageChange.invoke(selectedImageUri.toString())
        }
    }

    val contentScale = if (signUpUiState.profileImage.isEmpty()) {
        ContentScale.Inside
    } else {
        ContentScale.Crop
    }

    CoilImage(
        imageModel = signUpUiState.profileImage.ifEmpty { R.drawable.ic_add_a_photo },
        previewPlaceholder = R.drawable.ic_add_a_photo,
        imageOptions = ImageOptions(contentScale = contentScale),
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
            .background(
                color = if (signUpUiState.profileImage.isNotEmpty())
                    Color.Transparent else MaterialTheme.colors.secondary
            ),
    )
}

@CompletePreview
@Composable
private fun Preview() {
    val signUpUiState = SignUpUiState(
        profileImage = "",
        username = "Simple Chat",
        email = "simplechat@gmail.com.br",
        password = "123456",
        confirmedPassword = "123456"
    )
    PreviewTheme {
        MainContent(
            signUpUiState = signUpUiState,
            onProfileImageChange = {},
            onUsernameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmedPasswordChange = {},
            onButtonSignUpClick = {}
        )
    }
}