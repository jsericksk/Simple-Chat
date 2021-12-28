package com.kproject.simplechat.ui.screens.components

import androidx.annotation.StringRes
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kproject.simplechat.R
import com.kproject.simplechat.ui.theme.TextDefaultColor

@Composable
fun SimpleProgressDialog(
    showDialog: MutableState<Boolean>
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {},
            text = { CircularProgressIndicator() },
            buttons = {}
        )
    }
}

@Composable
fun SimpleDialog(
    showDialog: MutableState<Boolean>,
    @StringRes titleResId: Int,
    @StringRes messageResId: Int,
    onClickButtonOk: () -> Unit,
    onClickButtonCancel: () -> Unit
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = {
                Text(
                    text = stringResource(id = titleResId),
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.TextDefaultColor,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = stringResource(id = messageResId),
                    color = MaterialTheme.colors.TextDefaultColor,
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog.value = false
                        onClickButtonOk.invoke()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.button_ok),
                        color = MaterialTheme.colors.onSecondary
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog.value = false
                        onClickButtonCancel.invoke()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.button_cancel).uppercase(),
                        color = MaterialTheme.colors.onSecondary
                    )
                }
            }
        )
    }
}