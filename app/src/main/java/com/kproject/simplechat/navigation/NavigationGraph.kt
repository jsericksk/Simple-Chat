package com.kproject.simplechat.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.firebase.auth.FirebaseAuth
import com.kproject.simplechat.ui.screens.ChatScreen
import com.kproject.simplechat.ui.screens.HomeScreen
import com.kproject.simplechat.ui.screens.LoginScreen
import com.kproject.simplechat.ui.screens.SignUpScreen

@ExperimentalPagerApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
@Composable
fun NavigationGraph(navController: NavHostController) {
    AnimatedNavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(route = Screen.HomeScreen.route) {
            /**
             * Check if the user is logged in.
             */
            if (FirebaseAuth.getInstance().currentUser?.uid != null) {
                HomeScreen(
                    navigateToChatScreen = { userId, userName, userProfileImage ->
                        navController.navigate(
                            Screen.ChatScreen.withArgs(
                                userId, userName, userProfileImage
                            )
                        )
                    },
                    navigateToLoginScreen = {
                        navController.navigate(route = Screen.LoginScreen.route) {
                            clearBackStack(Screen.HomeScreen.route)
                        }
                    }
                )
            } else {
                LoginScreen(
                    navigateToHomeScreen = {
                        navController.navigate(Screen.HomeScreen.route) {
                            clearBackStack(Screen.LoginScreen.route)
                        }
                    },
                    navigateToSignUpScreen = { navController.navigate(Screen.SignUpScreen.route) }
                )
            }
        }

        composable(route = Screen.LoginScreen.route) {
            LoginScreen(
                navigateToHomeScreen = {
                    navController.navigate(Screen.HomeScreen.route) {
                        clearBackStack(Screen.LoginScreen.route)
                    }
                },
                navigateToSignUpScreen = { navController.navigate(Screen.SignUpScreen.route) }
            )
        }

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
                navigateToHomeScreen = {
                    navController.navigate(Screen.HomeScreen.route) {
                        clearBackStack(Screen.HomeScreen.route)
                    }
                },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ChatScreen.route + "/{userId}/{userName}/{userProfileImage}",
            arguments = listOf(
                navArgument(name = "userId") {
                    type = NavType.StringType
                },
                navArgument(name = "userName") {
                    type = NavType.StringType
                },
                navArgument(name = "userProfileImage") {
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
                userProfileImage = entry.arguments!!.getString("userProfileImage")!!,
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}

fun NavOptionsBuilder.clearBackStack(route: String) {
    popUpTo(route) {
        inclusive = true
    }
    launchSingleTop = true
}