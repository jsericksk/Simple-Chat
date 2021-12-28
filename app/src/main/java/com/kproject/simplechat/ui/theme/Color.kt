package com.kproject.simplechat.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Teal200 = Color(0xFF03DAC5)

// Dark Theme
val BackgroundDark = Color(0xFF131313)
val PrimaryDark = Color(0xFF104B8F)
val PrimaryVariantDark = Color(0xFF012E6D)
val TextDefaultColorDark = Color(0xFFC5C5C5)
val IconDefaultColorDark = Color(0xFF888888)
val ChatTextFieldBackgroundColorDark = Color(0xFF252525)

// Light Theme
val BackgroundLight = Color(0xFFE4E2E2)
val PrimaryLight = Color(0xFF1565C0)
val PrimaryVariantLight = Color(0xFF003C8F)
val Secondary = Color(0xFF1565C0)
val TextDefaultColorLight = Color(0xFF222222)
val IconDefaultColorLight = Color(0xFF383838)
val ChatTextFieldBackgroundColorLight = Color(0xFFBEBEBE)

val Colors.TextDefaultColor: Color
    @Composable get() = if (isLight) TextDefaultColorLight else TextDefaultColorDark
val Colors.IconColor: Color
    @Composable get() = if (isLight) IconDefaultColorLight else IconDefaultColorDark
val Colors.TextFieldFocusedIndicatorColor: Color
    @Composable get() = if (isLight) PrimaryLight else PrimaryDark
val Colors.TextFieldUnfocusedIndicatorColor: Color
    @Composable get() = if (isLight) PrimaryVariantLight else PrimaryVariantDark
val Colors.ChatTextFieldBackgroundColor: Color
    @Composable get() = if (isLight) ChatTextFieldBackgroundColorLight else ChatTextFieldBackgroundColorDark
