package com.kproject.simplechat.presentation.screens.authentication.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
import com.kproject.simplechat.presentation.screens.authentication.components.Button
import com.kproject.simplechat.presentation.screens.authentication.components.FieldType
import com.kproject.simplechat.presentation.screens.authentication.components.TextField
import com.kproject.simplechat.presentation.theme.PreviewTheme
import com.kproject.simplechat.presentation.theme.CompletePreview
import com.kproject.simplechat.presentation.theme.TextDefaultColor

@Composable
fun LoginScreen(
    onNavigateToHomeScreen: () -> Unit,
    onNavigateToSignUpScreen: () -> Unit,
) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginUiState = loginViewModel.loginUiState

    MainContent(
        loginUiState = loginUiState,
        onEmailChange = { email ->
            loginViewModel.onEmailChange(email)
        },
        onPasswordChange = { password ->
            loginViewModel.onPasswordChange(password)
        },
        onButtonLoginClick = {
            loginViewModel.login()
        },
        onNavigateToSignUpScreen = onNavigateToSignUpScreen
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
    val spacingHeight = 20.dp

    Column(
        modifier = modifier
            .fillMaxSize()
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
                text = stringResource(id = R.string.sign_in),
                fontSize = 40.sp,
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
            TextField(
                value = loginUiState.email,
                onValueChange = { value ->
                    onEmailChange.invoke(value)
                },
                hint = stringResource(id = R.string.email),
                leadingIcon = R.drawable.ic_email,
                keyboardType = KeyboardType.Email,
                fieldType = FieldType.Email,
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
            )

            Spacer(Modifier.height(spacingHeight))

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
    val loginUiState = LoginUiState("simplechat@gmail.com.br", "123456")
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