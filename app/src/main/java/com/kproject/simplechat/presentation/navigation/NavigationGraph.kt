package com.kproject.simplechat.presentation.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.kproject.simplechat.presentation.MainViewModel
import com.kproject.simplechat.presentation.mapper.fromJson
import com.kproject.simplechat.presentation.mapper.toJson
import com.kproject.simplechat.presentation.model.User
import com.kproject.simplechat.presentation.screens.authentication.login.LoginScreen
import com.kproject.simplechat.presentation.screens.authentication.signup.SignUpScreen
import com.kproject.simplechat.presentation.screens.chat.ChatScreen
import com.kproject.simplechat.presentation.screens.home.HomeScreen

private const val ArgUser = "user"

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationGraph(mainViewModel: MainViewModel) {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(route = Screen.HomeScreen.route) {
            if (mainViewModel.isUserLoggedIn) {
                HomeScreen(
                    mainViewModel = mainViewModel,
                    onNavigateToLoginScreen = {
                        navController.navigateWithPopUp(
                            toRoute = Screen.LoginScreen.route,
                            fromRoute = Screen.HomeScreen.route
                        )
                    },
                    onNavigateToChatScreen = { user ->
                        val userJson = user.toJson()
                        navController.navigate(Screen.ChatScreen.route + "/$userJson")
                    },
                )
            } else {
                LoginScreen(
                    onNavigateToHomeScreen = {
                        navController.navigateWithPopUp(
                            toRoute = Screen.HomeScreen.route,
                            fromRoute = Screen.LoginScreen.route
                        )
                    },
                    onNavigateToSignUpScreen = {
                        navController.navigate(Screen.SignUpScreen.route)
                    }
                )
            }
        }

        // LoginScreen
        composable(
            route = Screen.LoginScreen.route,
        ) {
            LoginScreen(
                onNavigateToHomeScreen = {
                    navController.navigate(Screen.HomeScreen.route)
                },
                onNavigateToSignUpScreen = {
                    navController.navigate(Screen.SignUpScreen.route)
                }
            )
        }

        // SignUpScreen
        composable(
            route = Screen.SignUpScreen.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            }
        ) {
            SignUpScreen(
                onNavigateToHomeScreen = {
                    navController.navigateWithPopUp(
                        toRoute = Screen.HomeScreen.route,
                        fromRoute = Screen.SignUpScreen.route
                    )
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // ChatScreen
        composable(
            route = Screen.ChatScreen.route  + "/{$ArgUser}",
            arguments = listOf(
                navArgument(name = ArgUser) {
                    type = NavType.StringType
                },
            ),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Up,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Down,
                    animationSpec = tween(700)
                )
            }
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.getString(ArgUser)?.let { userJson ->
                val user = userJson.fromJson(User::class.java)
                ChatScreen(
                    user = user,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

private fun NavHostController.navigateWithPopUp(toRoute: String, fromRoute: String) {
    navigate(toRoute) {
        popUpTo(fromRoute) {
            inclusive = true
        }
        launchSingleTop = true
    }
}