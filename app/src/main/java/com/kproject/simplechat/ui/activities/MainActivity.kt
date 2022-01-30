package com.kproject.simplechat.ui.activities

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.kproject.simplechat.model.ReceivedMessage
import com.kproject.simplechat.navigation.NavigationGraph
import com.kproject.simplechat.navigation.Screen
import com.kproject.simplechat.ui.theme.SimpleChatTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalCoilApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    var receivedMessage: ReceivedMessage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receivedMessage = intent.extras?.getSerializable("receivedMessage") as ReceivedMessage?

        setContent {
            SimpleChatTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberAnimatedNavController()
                    NavigationGraph(navController)

                    /**
                     * Opens the chat screen in the specific conversation if the
                     * intent contains data.
                     */
                    receivedMessage?.let { message ->
                        navController.navigate(
                            Screen.ChatScreen.withArgs(
                                message.fromUserId,
                                message.userName,
                                Uri.encode(message.userProfileImage)
                            )
                        )
                        /**
                         * Removes Intent data so that the chat screen is not reopened if a
                         * configuration change (such as screen rotation).
                         */
                        intent.removeExtra("receivedMessage")
                    }
                }
            }
        }
    }
}