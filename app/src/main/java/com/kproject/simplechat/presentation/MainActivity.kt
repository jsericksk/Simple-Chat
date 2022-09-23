package com.kproject.simplechat.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.kproject.simplechat.presentation.navigation.NavigationGraph
import com.kproject.simplechat.presentation.screens.home.HomeScreen
import com.kproject.simplechat.presentation.theme.SimpleChatTheme
import com.kproject.simplechat.presentation.theme.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            SimpleChatTheme(themeViewModel = themeViewModel) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // NavigationGraph(themeViewModel = themeViewModel)
                    HomeScreen(
                        themeViewModel = themeViewModel,
                        onNavigateToChatScreen = {}
                    )
                }
            }
        }
    }
}