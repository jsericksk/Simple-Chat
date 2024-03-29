package com.kproject.simplechat.presentation.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kproject.simplechat.R
import com.kproject.simplechat.presentation.theme.CompletePreview
import com.kproject.simplechat.presentation.theme.PreviewTheme
import com.kproject.simplechat.presentation.theme.TextDefaultColor

@Composable
fun AlertDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    title: String,
    message: String,
    cancelable: Boolean = true,
    onClickButtonOk: () -> Unit,
    onClickButtonCancel: () -> Unit = {},
    showButtonCancel: Boolean = true,
    shape: Shape = RoundedCornerShape(14.dp)
) {
    if (showDialog) {
        CustomAlertDialog(
            showDialog = showDialog,
            onDismiss = onDismiss,
            title = title,
            cancelable = cancelable,
            onClickButtonOk = onClickButtonOk,
            onClickButtonCancel = onClickButtonCancel,
            showButtonCancel = showButtonCancel,
            shape = shape,
            content = {
                Text(
                    text = message,
                    color = MaterialTheme.colors.TextDefaultColor,
                    fontSize = 16.sp
                )
            }
        )
    }
}

@Composable
private fun CustomAlertDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    title: String,
    cancelable: Boolean = true,
    onClickButtonOk: () -> Unit,
    onClickButtonCancel: () -> Unit = {},
    showButtonCancel: Boolean = true,
    shape: Shape = RoundedCornerShape(14.dp),
    content: @Composable () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                if (cancelable) {
                    onDismiss.invoke()
                }
            },
            title = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        color = MaterialTheme.colors.TextDefaultColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.height(16.dp))
                    content.invoke()
                    Spacer(Modifier.height(16.dp))
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismiss.invoke()
                        onClickButtonOk.invoke()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.button_ok).uppercase(),
                        color = MaterialTheme.colors.secondary
                    )
                }
            },
            dismissButton = {
                if (showButtonCancel) {
                    TextButton(
                        onClick = {
                            onDismiss.invoke()
                            onClickButtonCancel.invoke()
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.button_cancel).uppercase(),
                            color = MaterialTheme.colors.secondary
                        )
                    }
                }
            },
            backgroundColor = MaterialTheme.colors.background,
            shape = shape
        )
    }
}

@Composable
fun ProgressAlertDialog(
    showDialog: Boolean,
    shape: Shape = RoundedCornerShape(14.dp)
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = {},
            content = {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            color = MaterialTheme.colors.background,
                            shape = shape
                        )
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.secondary,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        )
    }
}

@CompletePreview
@Composable
private fun Preview() {
    PreviewTheme {
        AlertDialog(
            showDialog = true,
            onDismiss = { },
            title = "Alert Dialog",
            message = "Alert dialog message.",
            onClickButtonOk = { }
        )
    }
}