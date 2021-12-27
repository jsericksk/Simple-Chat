package com.kproject.simplechat.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    navigateBack: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val showProgressDialog = rememberSaveable { mutableStateOf(false) }

    val profileImage = rememberSaveable { mutableStateOf<Uri?>(null) }
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

    var clicksToBack by remember { mutableStateOf(0) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            clicksToBack++
                            if (clicksToBack == 1) {
                                navigateBack.invoke()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                            tint = MaterialTheme.colors.primary
                        )
                    }
                },
                backgroundColor = Color.Transparent,
                contentColor = Color.Transparent,
                elevation = 0.dp
            )
        }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = stringResource(id = R.string.create_your_account),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
            )

            Spacer(modifier = Modifier.padding(top = 22.dp))

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

            Spacer(modifier = Modifier.padding(top = 18.dp))

            LoginTextField(
                textFieldValue = userName,
                hint = R.string.user_name,
                leadingIcon = R.drawable.ic_person
            )

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

            LoginTextField(
                textFieldValue = confirmedPassword,
                hint = R.string.confirm_password,
                leadingIcon = R.drawable.ic_key,
                fieldType = FieldType.PASSWORD
            )

            Spacer(modifier = Modifier.padding(top = 22.dp))

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
                        }
                    ) {
                        loginViewModel.signUp(
                            email = email.value,
                            password = password.value,
                            userName = userName.value,
                            profileImage = profileImage.value!!
                        )
                    }
                },
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth()
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
}

