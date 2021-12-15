package com.kproject.simplechat.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.kproject.simplechat.R
import com.kproject.simplechat.ui.screens.components.LoginTextField
import com.kproject.simplechat.utils.FieldType

@Composable
fun LoginScreen(
    navigateToHomeScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        LoginTextField(
            textFieldValue = email,
            hint = R.string.email,
            keyboardType = KeyboardType.Email,
            leadingIcon = R.drawable.ic_email,
            fieldType = FieldType.EMAIL
        )

        LoginTextField(
            textFieldValue = password,
            hint = R.string.password,
            keyboardType = KeyboardType.Password,
            leadingIcon = R.drawable.ic_key,
            fieldType = FieldType.PASSWORD
        )

        Spacer(modifier = Modifier.padding(top = 18.dp))

        Button(
            onClick = {
                navigateToHomeScreen.invoke()
            },
            shape = CircleShape,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.sign_in), color = Color.White)
        }

        Spacer(modifier = Modifier.padding(top = 18.dp))

        val annotatedString = buildAnnotatedString {
            append(stringResource(id = R.string.dont_have_an_account) + " ")
            pushStringAnnotation(tag = "Sign Up", annotation = "Sign Up")
            withStyle(style = SpanStyle(color = Color.Blue)) {
                append(stringResource(id = R.string.sign_up))
            }
            pop()
        }

        ClickableText(
            text = annotatedString,
            style = TextStyle(fontSize = 16.sp),
            onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "Sign Up", start = offset, end = offset)
                    .firstOrNull()?.let {
                        navigateToSignUpScreen.invoke()
                }
            })

    }

}

fun loginIn(email: String, password: String, navigateToHome: () -> Unit) {
    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("Login", "signInWithEmail:success")
                val user = FirebaseAuth.getInstance().currentUser
                navigateToHome.invoke()
            } else {
                // If sign in fails, display a message to the user.
                Log.w("Login", "signInWithEmail:failure", task.exception)
            }
        }
}
