package com.kproject.simplechat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kproject.simplechat.R
import com.kproject.simplechat.data.DataStateResult
import com.kproject.simplechat.ui.screens.components.LoginTextField
import com.kproject.simplechat.ui.screens.components.SimpleProgressDialog
import com.kproject.simplechat.ui.viewmodels.MainViewModel
import com.kproject.simplechat.utils.FieldType
import com.kproject.simplechat.utils.FieldValidator
import com.kproject.simplechat.utils.Utils

@Composable
fun LoginScreen(
    navigateToHomeScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val showProgressDialog = rememberSaveable { mutableStateOf(false) }

    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }

    val dataStateResult by mainViewModel.dataStateResult.observeAsState()
    val errorMessageResId by mainViewModel.errorMessageResId.observeAsState()

    var isRequestFinished by rememberSaveable { mutableStateOf(false) }

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
                showProgressDialog.value = true
                isRequestFinished = false
                if (FieldValidator.validateSignIn(
                        email = email.value,
                        password = password.value
                    ) { errorMessageResId ->
                        Utils.showToast(context, errorMessageResId)
                    }
                ) {
                    mainViewModel.signIn(
                        email = email.value,
                        password = password.value
                    )
                }
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

    if (!isRequestFinished) {
        when (dataStateResult) {
            is DataStateResult.Loading -> {
                SimpleProgressDialog(showDialog = showProgressDialog)
            }
            is DataStateResult.Success -> {
                isRequestFinished = true
                navigateToHomeScreen.invoke()
            }
            is DataStateResult.Error -> {
                isRequestFinished = true
                errorMessageResId?.let {
                    Utils.showToast(context, errorMessageResId!!)
                }
            }
        }
    }

}