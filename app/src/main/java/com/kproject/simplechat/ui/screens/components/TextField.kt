package com.kproject.simplechat.ui.screens.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kproject.simplechat.R
import com.kproject.simplechat.ui.theme.TextFieldFocusedIndicatorColor
import com.kproject.simplechat.ui.theme.TextFieldUnfocusedIndicatorColor
import com.kproject.simplechat.utils.FieldType
import com.kproject.simplechat.utils.FieldValidator

@Composable
fun LoginTextField(
    textFieldValue: MutableState<String>,
    @StringRes hint: Int,
    keyboardType: KeyboardType = KeyboardType.Text,
    @DrawableRes leadingIcon: Int,
    fieldType: FieldType = FieldType.NONE
) {
    val passwordVisible = rememberSaveable { mutableStateOf(false) }
    if (fieldType != FieldType.PASSWORD) {
        passwordVisible.value = true
    }
    val visibilityIcon =
            if (passwordVisible.value) R.drawable.ic_visibility_off
            else R.drawable.ic_visibility

    Column {
        OutlinedTextField(
            value = textFieldValue.value,
            onValueChange = { value ->
                textFieldValue.value = value
                //isError.value = !FieldValidator.validate(value, fieldType)
                //fieldContainsError.invoke(isError.value)
            },
            textStyle = TextStyle(color = Color.DarkGray, fontSize = 18.sp),
            label = {
                Text(text = stringResource(id = hint))
            },
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = leadingIcon),
                    contentDescription = null,
                    tint = Color.DarkGray
                )
            },
            trailingIcon = {
                if (fieldType == FieldType.PASSWORD) {
                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = visibilityIcon),
                            contentDescription = null,
                            tint = Color.DarkGray
                        )
                    }
                }
            },
            visualTransformation = if (passwordVisible.value)
                VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = if (passwordVisible.value) keyboardType else KeyboardType.Password
            ),
            shape = CircleShape,
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.DarkGray,
                leadingIconColor = Color.White,
                trailingIconColor = Color.White,
                focusedIndicatorColor = MaterialTheme.colors.TextFieldFocusedIndicatorColor,
                unfocusedIndicatorColor = MaterialTheme.colors.TextFieldUnfocusedIndicatorColor,
                focusedLabelColor = MaterialTheme.colors.TextFieldUnfocusedIndicatorColor,
                unfocusedLabelColor = MaterialTheme.colors.TextFieldFocusedIndicatorColor,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp)
        )
    }

}