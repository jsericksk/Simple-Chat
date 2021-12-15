package com.kproject.simplechat.ui.screens.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(title: String, navigateBack: () -> Unit) {
    /**
     * Evita de ocorrer um bug se o usuário tocar muitas vezes em voltar em um curto
     * período de tempo, para evitar de navController.popBackStack() ser
     * chamado antes da tela ser renderizada e causar uma tela preta.
     */
    val clicks = remember { mutableStateOf(0) }

    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = {
                clicks.value++
                if (clicks.value == 1) {
                    // navController.popBackStack()
                    navigateBack.invoke()
                }
            }
            ) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White,
        elevation = 0.dp
    )
}