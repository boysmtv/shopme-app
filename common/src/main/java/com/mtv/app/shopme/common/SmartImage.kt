package com.mtv.app.shopme.common

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.painter.Painter
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun SmartImage(
    model: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: Painter? = null,
    error: Painter? = placeholder
) {
    val bitmap by produceState<android.graphics.Bitmap?>(initialValue = null, key1 = model) {
        value = withContext(Dispatchers.Default) {
            model
                ?.takeIf { it.startsWith("data:image") || it.contains("base64,") }
                ?.let(::base64ToBitmap)
        }
    }

    when {
        bitmap != null -> {
            val resolvedBitmap = bitmap ?: return
            Image(
                bitmap = resolvedBitmap.asImageBitmap(),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
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
