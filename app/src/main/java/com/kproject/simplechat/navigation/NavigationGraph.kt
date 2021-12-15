package com.kproject.simplechat.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.kproject.simplechat.ui.screens.*

@ExperimentalPagerApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
@Composable
fun NavigationGraph(
    navController: NavHostController
) {

    AnimatedNavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        composable(
            route = Screen.LoginScreen.route,
            popExitTransition = { _, target ->
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            }
        ) {
            /**MainScreen(
                navigateToLoginScreen = { navController.navigate(Screen.LoginScreen.route) },
                navigateToSignUpScreen = { navController.navigate(Screen.SignUpScreen.route) },
                navigateToHomeScreen = { navController.navigate(Screen.HomeScreen.route) },
                navigateToChatScreen = { navController.navigate(Screen.ChatScreen.withArgs("opa", "John")) },
            )*/
            LoginScreen(
                navigateToHomeScreen = { navController.navigate(Screen.HomeScreen.route) },
                navigateToSignUpScreen = { navController.navigate(Screen.SignUpScreen.route) }
            )
        }
        /**
        composable(
            route = Screen.LoginScreen.route,
            enterTransition = { initial, _ ->
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            exitTransition = { _, target ->
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            }
        ) {
            LoginScreen(
                navigateToHome = { navController.navigate(Screen.HomeScreen.route) },
                navigateToSignUp = { navController.navigate(Screen.SignUpScreen.route) }
            )
        }*/

        composable(
            route = Screen.SignUpScreen.route,
            enterTransition = { initial, _ ->
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            exitTransition = { _, target ->
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            }
        ) {
            SignUpScreen(
                navigateToHomeScreen = { navController.navigate(Screen.HomeScreen.route) },
            )
        }

        composable(
            route = Screen.HomeScreen.route,
            enterTransition = { initial, _ ->
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            exitTransition = { _, target ->
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            }
        ) {
            HomeScreen { userId, userName ->
                navController.navigate(Screen.ChatScreen.withArgs(userId, userName))
            }
        }

        composable(
            route = Screen.ChatScreen.route + "/{userId}/{userName}",
            arguments = listOf(
                navArgument(name = "userId") {
                    type = NavType.StringType
                },
                navArgument(name = "userName") {
                    type = NavType.StringType
                }
            ),
            enterTransition = { initial, _ ->
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            exitTransition = { _, target ->
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            }
        ) { entry ->
            ChatScreen(
                userId = entry.arguments!!.getString("userId")!!,
                userName = entry.arguments!!.getString("userName")!!,
                navigateBack = { navController.popBackStack() }
            )
        }

    }
}