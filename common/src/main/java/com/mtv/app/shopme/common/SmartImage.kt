package com.mtv.app.shopme.common

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.painter.Painter
import coil.compose.AsyncImage

@Composable
fun SmartImage(
    model: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: Painter? = null,
    error: Painter? = placeholder
) {
    when {
        !model.isNullOrBlank() -> {
            AsyncImage(
                model = model,
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale,
                placeholder = placeholder,
                error = error
            )
        }
        placeholder != null -> {
            Image(
                painter = placeholder,
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
    }
}
