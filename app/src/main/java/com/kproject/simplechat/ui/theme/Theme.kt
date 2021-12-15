package com.kproject.simplechat.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200,
    background = BackgroundDarkTheme
)

private val LightColorPalette = lightColors(
    primary = PrimaryLight,
    primaryVariant = PrimaryVariantLight,
    secondary = Teal200,
    background = BackgroundLightTheme,
    onPrimary = Color.White,
    onSecondary = Secondary,
    onBackground = Color.Black,
    onSurface = Color.Black,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun TicTacToeOnlineTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = LightColorPalette /**if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }*/

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}