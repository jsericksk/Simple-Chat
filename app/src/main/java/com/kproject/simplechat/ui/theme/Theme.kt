package com.kproject.simplechat.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.asLiveData
import com.kproject.simplechat.utils.DataStoreUtils
import com.kproject.simplechat.utils.PrefsConstants
import com.kproject.simplechat.utils.Utils

private val DarkColorPalette = darkColors(
    primary = PrimaryDark,
    primaryVariant = PrimaryVariantDark,
    secondary = Teal200,
    background = BackgroundDark,
    onSecondary = Secondary
)

private val LightColorPalette = lightColors(
    primary = PrimaryLight,
    primaryVariant = PrimaryVariantLight,
    secondary = Teal200,
    background = BackgroundLight,
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
fun SimpleChatTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val context = LocalContext.current
    val appThemeState by DataStoreUtils.readPreference(
        context = context,
        key = PrefsConstants.APP_THEME,
        defaultValue = PrefsConstants.THEME_SYSTEM_DEFAULT
    ).asLiveData().observeAsState(
        // Gets an initial value without Flow so there is no small delay
        initial = DataStoreUtils.readPreferenceWithoutFlow(
            context = context,
            key = PrefsConstants.APP_THEME,
            defaultValue = PrefsConstants.THEME_SYSTEM_DEFAULT
        )
    )

    var colors = LightColorPalette
    if (appThemeState == PrefsConstants.THEME_SYSTEM_DEFAULT) {
        if (Utils.getCurrentSystemTheme(context) == PrefsConstants.THEME_DARK) {
            colors = DarkColorPalette
        }
    } else if (appThemeState == PrefsConstants.THEME_DARK) {
        colors = DarkColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}