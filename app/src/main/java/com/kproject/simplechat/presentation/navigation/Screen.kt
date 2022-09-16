package com.kproject.simplechat.presentation.navigation

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login_screen")
    object SignUpScreen : Screen("signup_screen")
    object HomeScreen : Screen("home_screen")
}