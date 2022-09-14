package com.kproject.simplechat.presentation.screens.authentication.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.kproject.simplechat.R
import com.kproject.simplechat.presentation.theme.PreviewTheme
import com.kproject.simplechat.presentation.theme.TextDefaultColor

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    @DrawableRes leadingIcon: Int,
    shape: Shape = CircleShape,
    keyboardType: KeyboardType = KeyboardType.Text,
    fieldType: FieldType = FieldType.None
) {
    var passwordVisible by rememberSaveable { mutableStateOf(fieldType != FieldType.Password) }
    val visibilityIcon =
            if (passwordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility

    Column {
        OutlinedTextField(
            value = value,
            onValueChange = { value ->
                onValueChange.invoke(value)
            },
            textStyle = TextStyle(color = MaterialTheme.colors.TextDefaultColor, fontSize = 18.sp),
            label = {
                Text(text = hint)
            },
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = leadingIcon),
                    contentDescription = null,
                )
            },
            trailingIcon = {
                if (fieldType == FieldType.Password) {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = visibilityIcon),
                            contentDescription = null,
                        )
                    }
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = if (passwordVisible) keyboardType else KeyboardType.Password
            ),
            shape = shape,
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.background,
                cursorColor = MaterialTheme.colors.primary,
                leadingIconColor = MaterialTheme.colors.secondary,
                trailingIconColor = MaterialTheme.colors.onSecondary,
                focusedIndicatorColor = MaterialTheme.colors.secondary,
                unfocusedIndicatorColor = MaterialTheme.colors.onSecondary,
                focusedLabelColor = MaterialTheme.colors.secondary,
                unfocusedLabelColor = MaterialTheme.colors.onSecondary,
            ),
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Preview(name = "LightTheme")
@Preview(name = "DarkTheme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    PreviewTheme {
        Column {
            TextField(
                value = "",
                onValueChange = { },
                hint = "Type your e-mail",
                leadingIcon = R.drawable.ic_email
            )

            TextField(
                value = "simplechat@gmail.com.br",
                onValueChange = { },
                hint = "Type your e-mail",
                leadingIcon = R.drawable.ic_email
            )
        }
    }
}