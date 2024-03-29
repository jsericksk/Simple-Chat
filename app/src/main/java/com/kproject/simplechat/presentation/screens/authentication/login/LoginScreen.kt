package com.kproject.simplechat.presentation.screens.authentication.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
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
import com.kproject.simplechat.presentation.theme.CompletePreview
import com.kproject.simplechat.presentation.theme.PreviewTheme
import com.kproject.simplechat.presentation.theme.TextDefaultColor

@Composable
fun LoginScreen(
    onNavigateToHomeScreen: () -> Unit,
    onNavigateToSignUpScreen: () -> Unit,
) {
    val context = LocalContext.current
    val loginViewModel: LoginViewModel = hiltViewModel()
    val uiState = loginViewModel.uiState
    val loginState = loginViewModel.loginState

    MainContent(
        loginUiState = uiState,
        onEmailChange = { email ->
            loginViewModel.onEvent(LoginEvent.EmailChanged(email))
        },
        onPasswordChange = { password ->
            loginViewModel.onEvent(LoginEvent.PasswordChanged(password))
        },
        onButtonLoginClick = {
            loginViewModel.login()
        },
        onNavigateToSignUpScreen = onNavigateToSignUpScreen
    )

    LaunchedEffect(key1 = loginState) {
        if (loginState is DataState.Success) {
            onNavigateToHomeScreen.invoke()
        }
    }

    ProgressAlertDialog(showDialog = uiState.isLoading)

    AlertDialog(
        showDialog = uiState.loginError,
        onDismiss = {
            loginViewModel.onEvent(LoginEvent.OnDismissErrorDialog)
        },
        title = stringResource(id = R.string.error),
        message = uiState.loginErrorMessage.asString(),
        onClickButtonOk = {}
    )
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    loginUiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onButtonLoginClick: () -> Unit,
    onNavigateToSignUpScreen: () -> Unit
) {
    val spacingHeight = 16.dp

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_send),
            contentDescription = null,
            tint = MaterialTheme.colors.secondary,
            modifier = Modifier.size(45.dp)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(id = R.string.login),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.TextDefaultColor
        )

        Spacer(Modifier.height(spacingHeight))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(24.dp)
        ) {
            TextField(
                value = loginUiState.email,
                onValueChange = { value ->
                    onEmailChange.invoke(value)
                },
                hint = stringResource(id = R.string.email),
                leadingIcon = R.drawable.ic_email,
                keyboardType = KeyboardType.Email,
                fieldType = FieldType.Email,
                errorMessage = loginUiState.emailError.asString()
            )

            Spacer(Modifier.height(spacingHeight))

            TextField(
                value = loginUiState.password,
                onValueChange = { value ->
                    onPasswordChange.invoke(value)
                },
                hint = stringResource(id = R.string.password),
                leadingIcon = R.drawable.ic_key,
                keyboardType = KeyboardType.Password,
                fieldType = FieldType.Password,
                errorMessage = loginUiState.passwordError.asString()
            )

            Spacer(Modifier.height(22.dp))

            Button(
                text = stringResource(id = R.string.login),
                onClick = onButtonLoginClick
            )

            Spacer(Modifier.height(32.dp))
            
            SignUpText(onNavigateToSignUpScreen = onNavigateToSignUpScreen)
        }
    }
}

@Composable
private fun SignUpText(onNavigateToSignUpScreen: () -> Unit) {
    val tag = "Sign Up"
    val annotatedText = buildAnnotatedString {
        append(stringResource(id = R.string.dont_have_an_account) + " ")
        pushStringAnnotation(
            tag =tag,
            annotation = "https://developer.android.com"
        )
        withStyle(
            style = SpanStyle(color = MaterialTheme.colors.secondary, fontWeight = FontWeight.Bold)
        ) {
            append(stringResource(id = R.string.sign_up))
        }
        pop()
    }

    ClickableText(
        text = annotatedText,
        style = TextStyle(
            color = MaterialTheme.colors.TextDefaultColor, fontSize = 16.sp
        ),
        onClick = { offset ->
            annotatedText.getStringAnnotations(
                tag = tag, start = offset, end = offset
            ).firstOrNull()?.let { annotation ->
                onNavigateToSignUpScreen()
            }
        }
    )
}

@CompletePreview
@Composable
private fun Preview() {
    val loginUiState = LoginUiState(email = "simplechat@gmail.com.br", password = "123456")
    PreviewTheme {
        MainContent(
            loginUiState = loginUiState,
            onEmailChange = {},
            onPasswordChange = {},
            onButtonLoginClick = {},
            onNavigateToSignUpScreen = {}
        )
    }
}