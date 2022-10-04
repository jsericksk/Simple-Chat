package com.kproject.simplechat.presentation.screens.authentication.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kproject.simplechat.presentation.theme.PreviewTheme
import com.kproject.simplechat.presentation.theme.SimplePreview

@Composable
fun Button(
   modifier: Modifier = Modifier,
   text: String,
   onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        contentPadding = PaddingValues(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            color = Color.White
        )
    }
}

@SimplePreview
@Composable
private fun Preview() {
    PreviewTheme {
        Column {
            Button(
                text = "Login",
                onClick = {}
            )
        }
    }
}