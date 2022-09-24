package com.kproject.simplechat.presentation

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kproject.simplechat.presentation.navigation.NavigationGraph
import com.kproject.simplechat.presentation.theme.SimpleChatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        loadInitialConfigurations()
        setContent {
            SimpleChatTheme(mainViewModel = mainViewModel) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavigationGraph(mainViewModel = mainViewModel)
                    /**HomeScreen(
                        mainViewModel = mainViewModel,
                        onNavigateToChatScreen = {}
                    )*/
                }
            }
        }
    }

    /**
     * Loads some initial configurations before displaying the first screen, such as checking
     * whether the user is logged in or not to display the corresponding screen.
     */
    private fun loadInitialConfigurations() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (mainViewModel.isContentReady()) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )
    }
}