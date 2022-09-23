package com.kproject.simplechat.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.kproject.simplechat.presentation.navigation.NavigationGraph
import com.kproject.simplechat.presentation.screens.home.HomeScreen
import com.kproject.simplechat.presentation.theme.SimpleChatTheme
import com.kproject.simplechat.presentation.theme.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            SimpleChatTheme(themeViewModel = themeViewModel) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //NavigationGraph(themeViewModel = themeViewModel)
                    HomeScreen(
                        themeViewModel = themeViewModel,
                        onNavigateToChatScreen = {}
                    )
                }
            }
        }
    }
}