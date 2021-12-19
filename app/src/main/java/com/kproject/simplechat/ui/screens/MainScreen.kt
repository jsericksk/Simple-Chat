package com.kproject.simplechat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Tela temporária apenas para testes de UI.
 */
@Composable
fun MainScreen(
    navigateToLoginScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
    navigateToHomeScreen: () -> Unit,
    navigateToChatScreen: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Main Screen") },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White,
                elevation = 0.dp
            )
        }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
        ) {

            Button(onClick = navigateToLoginScreen) {
                Text(text = "Login Screen")
            }

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Button(onClick = navigateToSignUpScreen) {
                Text(text = "SignUp Screen")
            }

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Button(onClick = navigateToHomeScreen) {
                Text(text = "Home Screen")
            }

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Button(onClick = navigateToChatScreen) {
                Text(text = "LastMessage Screen")
            }
        }
    }
}
