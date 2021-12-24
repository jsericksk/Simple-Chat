package com.kproject.simplechat.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

// Dark Theme
val BackgroundDarkTheme = Color(0xFF131313)
val SurfaceDarkTheme = Color(0xFF1D1D1D)
val SecondaryDarkTheme = Color(0xFFD1D1D1)

// Light Theme
val PrimaryLight = Color(0xFF1565C0)
val PrimaryVariantLight = Color(0xFF003C8F)
val Secondary = Color(0xFF1565C0)

val BackgroundLightTheme = Color(0xFFE4E2E2)
val SecondaryLightTheme = Color(0xFF131313)

val TextDefaultColorLightTheme = Color(0xFF222222)
val TextDefaultColorDarkTheme = Color(0xFFC5C5C5)

val Colors.TextDefaultColor: Color
    @Composable get() = if (isLight) TextDefaultColorLightTheme else TextDefaultColorDarkTheme

val Colors.TextFieldFocusedIndicatorColor: Color
    @Composable get() = if (isLight) Color(0xFF1565C0) else Color(0xFF003C8F)
val Colors.TextFieldUnfocusedIndicatorColor: Color
    @Composable get() = if (isLight) Color(0xFF1565C0) else Color(0xFF003C8F)
