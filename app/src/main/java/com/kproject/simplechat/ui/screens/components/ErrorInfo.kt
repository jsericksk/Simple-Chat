package com.kproject.simplechat.ui.screens.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kproject.simplechat.ui.theme.IconColor
import com.kproject.simplechat.ui.theme.TextDefaultColor

@Composable
fun ErrorInfo(
    @DrawableRes iconResId: Int,
    @StringRes messageResId: Int,
    @StringRes errorMessageResId: Int
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val iconColor = MaterialTheme.colors.IconColor

        Icon(
            imageVector = ImageVector.vectorResource(id = iconResId),
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(110.dp)
        )
        Text(
            text = stringResource(id = messageResId),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.TextDefaultColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(all = 6.dp)
        )
        Text(
            text = stringResource(id = errorMessageResId),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.TextDefaultColor,
            fontSize = 16.sp,
            modifier = Modifier.padding(all = 6.dp)
        )
    }
}

@Composable
fun EmptyListInfo(
    @DrawableRes iconResId: Int,
    @StringRes messageResId: Int,
    @StringRes errorMessageResId: Int
) {
    ErrorInfo(
        iconResId = iconResId,
        messageResId = messageResId,
        errorMessageResId = errorMessageResId
    )
}