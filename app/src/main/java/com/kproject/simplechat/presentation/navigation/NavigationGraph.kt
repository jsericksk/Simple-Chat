package com.kproject.simplechat.presentation.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.kproject.simplechat.presentation.MainViewModel
import com.kproject.simplechat.presentation.screens.authentication.login.LoginScreen
import com.kproject.simplechat.presentation.screens.authentication.signup.SignUpScreen
import com.kproject.simplechat.presentation.screens.home.HomeScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationGraph(mainViewModel: MainViewModel) {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(route = Screen.HomeScreen.route) {
            if (mainViewModel.isUserLoggedIn) {
                HomeScreen(
                    mainViewModel = mainViewModel,
                    onNavigateToChatScreen = {},
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
        composable(route = Screen.LoginScreen.route) {
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