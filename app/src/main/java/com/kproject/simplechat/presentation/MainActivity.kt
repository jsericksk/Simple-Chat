package com.kproject.simplechat.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kproject.simplechat.presentation.screens.home.HomeScreen
import com.kproject.simplechat.presentation.theme.SimpleChatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // Set up an OnPreDrawListener to the root view.
        /**
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (themeViewModel.contentReady) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )*/

        setContent {
            //val themeViewModel: ThemeViewModel = hiltViewModel()
            SimpleChatTheme(mainViewModel = mainViewModel) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // NavigationGraph(mainViewModel = mainViewModel)
                    HomeScreen(
                        mainViewModel = mainViewModel,
                        onNavigateToChatScreen = {}
                    )
                }
            }
        }
    }
}