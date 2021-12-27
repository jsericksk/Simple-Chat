package com.kproject.simplechat.ui.screens.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi

@ExperimentalCoilApi
@Composable
fun ChatTopBar(
    userName: String,
    navigateBack: () -> Unit
) {
    /**
     * Prevents a bug from occurring if the user taps back many times in a short period of time,
     * to avoid navController.popBackStack() being called before the screen is rendered and
     * causing a black screen.
     */
    var clicks by remember { mutableStateOf(0) }

    TopAppBar(
        title = {
            Text(text = userName)
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    clicks++
                    if (clicks == 1) {
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