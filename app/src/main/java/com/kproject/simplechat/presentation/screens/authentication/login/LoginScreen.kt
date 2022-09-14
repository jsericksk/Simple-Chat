package com.kproject.simplechat.presentation.screens.authentication.login

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kproject.simplechat.R
import com.kproject.simplechat.presentation.screens.authentication.components.FieldType
import com.kproject.simplechat.presentation.screens.authentication.components.TextField
import com.kproject.simplechat.presentation.screens.components.AlertDialog
import com.kproject.simplechat.presentation.theme.PreviewTheme
import com.kproject.simplechat.presentation.theme.SimpleChatTheme

@Composable
fun LoginScreen(

) {

}


@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    loginUiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
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
            fieldType = FieldType.Email
        )

        Spacer(Modifier.height(8.dp))

        TextField(
            value = loginUiState.password,
            onValueChange = { value ->
                onPasswordChange.invoke(value)
            },
            hint = stringResource(id = R.string.password),
            leadingIcon = R.drawable.ic_key,
            keyboardType = KeyboardType.Password,
            fieldType = FieldType.Password
        )

    }
}


@Preview(showSystemUi = true, name = "LightTheme")
@Preview(showSystemUi = true, name = "DarkTheme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    val loginUiState = LoginUiState("simplechat@gmail.com.br", "123456")
    PreviewTheme {
        MainContent(
            loginUiState = loginUiState,
            onEmailChange = {},
            onPasswordChange = {}
        )
    }
}