package com.kproject.simplechat.presentation.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.kproject.simplechat.presentation.screens.authentication.login.LoginScreen
import com.kproject.simplechat.presentation.screens.authentication.signup.SignUpScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationGraph() {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        // LoginScreen
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(
                onNavigateToHomeScreen = {},
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
                onNavigateToHomeScreen = {},
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}