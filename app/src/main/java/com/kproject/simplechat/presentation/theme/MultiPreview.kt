package com.kproject.simplechat.presentation.theme

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "LightTheme")
@Preview(name = "DarkTheme", uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class SimplePreview

@Preview(showSystemUi = true, name = "LightTheme")
@Preview(showSystemUi = true, name = "DarkTheme", uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class CompletePreview