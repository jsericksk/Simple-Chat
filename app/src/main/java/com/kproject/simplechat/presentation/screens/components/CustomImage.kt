package com.kproject.simplechat.presentation.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kproject.simplechat.R
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin

@Composable
fun CustomImage(
    modifier: Modifier = Modifier,
    imageModel: Any,
    contentScale: ContentScale = ContentScale.Crop
) {
    val shimmerBaseColor = MaterialTheme.colors.background
    val shimmerHighlightColor = MaterialTheme.colors.onSecondary

    CoilImage(
        imageModel = imageModel,
        imageOptions = ImageOptions(contentScale = contentScale),
        failure = {
            FailureIndicator()
        },
        component = rememberImageComponent {
            ShimmerPlugin(
                baseColor = shimmerBaseColor,
                highlightColor = shimmerHighlightColor
            )
        },
        previewPlaceholder = R.drawable.ic_photo,
        modifier = modifier
    )
}

@Composable
fun BoxScope.FailureIndicator() {
    Box(
        modifier = Modifier
            .matchParentSize()
            .background(Color(0x99AA0003))
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_broken_image),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color(0xFFCECECE)),
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .padding(12.dp)
        )
    }
}