package com.kproject.simplechat.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.firebase.auth.FirebaseAuth

@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isUserLogged()) {
            startActivity(Intent(this, MainActivity::class.java))
            Log.d("SplashScreenActivity", "User is logged.")
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            Log.d("SplashScreenActivity", "User is not logged.")
        }
        finish()
    }

    private fun isUserLogged(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser != null
    }
}