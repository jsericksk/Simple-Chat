package com.kproject.simplechat.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.kproject.simplechat.R
import com.kproject.simplechat.data.DataStateResult
import com.kproject.simplechat.ui.screens.components.LoginTextField
import com.kproject.simplechat.ui.screens.components.SimpleProgressDialog
import com.kproject.simplechat.ui.viewmodels.LoginViewModel
import com.kproject.simplechat.utils.FieldType
import com.kproject.simplechat.utils.FieldValidator
import com.kproject.simplechat.utils.Utils

@ExperimentalCoilApi
@Composable
fun SignUpScreen(
    navigateToHomeScreen: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val showProgressDialog = rememberSaveable { mutableStateOf(false) }

    var isFieldWithError by rememberSaveable { mutableStateOf(false) }

    val profileImage = rememberSaveable {  mutableStateOf<Uri?>(null)  }
    val userName = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val confirmedPassword = rememberSaveable { mutableStateOf("") }

    val chooseProfileImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            profileImage.value = it
        }
    }

    val dataStateResult by loginViewModel.dataStateResult.observeAsState()
    val errorMessageResId by loginViewModel.errorMessageResId.observeAsState()

    var isRequestFinished by rememberSaveable { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Image(
            painter = rememberImagePainter(
                data = if (profileImage.value == null) R.drawable.ic_person else profileImage.value
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colors.primary, CircleShape)
                .clickable {
                    chooseProfileImage.launch("image/*")
                }
                .fillMaxSize()
        )

        Spacer(modifier = Modifier.padding(top = 6.dp))

        LoginTextField(
            textFieldValue = userName,
            hint = R.string.user_name,
            leadingIcon =  R.drawable.ic_person
        )

        LoginTextField(
            textFieldValue = email,
            hint = R.string.email,
            keyboardType = KeyboardType.Email,
            leadingIcon =  R.drawable.ic_email,
            fieldType = FieldType.EMAIL
        ) {
            isFieldWithError = it
        }

        LoginTextField(
            textFieldValue = password,
            hint = R.string.password,
            keyboardType = KeyboardType.Password,
            leadingIcon =  R.drawable.ic_key,
            fieldType = FieldType.PASSWORD
        ) {
            isFieldWithError = it
        }

        LoginTextField(
            textFieldValue = confirmedPassword,
            hint = R.string.confirm_password,
            leadingIcon =  R.drawable.ic_key,
            fieldType = FieldType.PASSWORD
        ) {
            isFieldWithError = it
        }

        Spacer(modifier = Modifier.padding(top = 18.dp))

        Button(
            onClick = {
                showProgressDialog.value = true
                isRequestFinished = false
                if (FieldValidator.validateSignUp(
                    email = email.value,
                    password = password.value,
                    confirmedPassword = confirmedPassword.value,
                    userName = userName.value,
                    profileImage = profileImage.value
                ) { errorMessageResId ->
                    Utils.showToast(context, errorMessageResId)
                }) {
                    loginViewModel.signUp(
                        email = email.value,
                        password = password.value,
                        userName = userName.value,
                        profileImage = profileImage.value!!
                    )
                }
            },
            shape = CircleShape,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.sign_up), color = Color.White)
        }
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

