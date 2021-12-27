package com.kproject.simplechat.navigation

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object LoginScreen : Screen("login_screen")
    object SignUpScreen : Screen("sign_up_screen")
    object ChatScreen : Screen("chat_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}