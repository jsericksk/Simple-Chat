package com.kproject.simplechat.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.kproject.simplechat.presentation.MainViewModel

private val DarkColorPalette = darkColors(
    primary = PrimaryDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    background = BackgroundDark
)

private val LightColorPalette = lightColors(
    primary = PrimaryLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    background = BackgroundLight
)

@Composable
fun SimpleChatTheme(
    mainViewModel: MainViewModel,
    content: @Composable () -> Unit
) {
    val colors = if (mainViewModel.isDarkMode) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun PreviewTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = {
            Surface(color = MaterialTheme.colors.background) {
                content()
            }
        }
    )
}