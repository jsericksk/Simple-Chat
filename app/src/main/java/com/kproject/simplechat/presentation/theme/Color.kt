package com.kproject.simplechat.presentation.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Dark Colors
val PrimaryDark = Color(0xFF002F6C)
val SecondaryDark = Color(0xFF003B88)
val OnSecondaryDark = Color(0xFF003375)
val BackgroundDark = Color(0xFF1F1F1F)

// Light Colors
val PrimaryLight = Color(0xFF013A83)
val SecondaryLight = Color(0xFF003D8D)
val OnSecondaryLight = Color(0xFF003880)
val BackgroundLight = Color(0xFFF1EDED)

val Colors.TextDefaultColor: Color
    @Composable get() = if (isLight) Color(0xFF2B2B2B) else Color(0xFFCECECE)
val Colors.IconColor: Color
    @Composable get() = if (isLight) Color(0xFF2B2B2B) else Color(0xFFCECECE)
val Colors.ErrorColor: Color
    @Composable get() = if (isLight) Color(0xFF9C1616) else Color(0xFF910505)

