/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ShimmerUtil.kt
 *
 * Last modified by Dedy Wijaya on 27/03/26 09.38
 */

package com.mtv.app.shopme.common

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun shimmerBrush(): Brush {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.4f),
        Color.LightGray.copy(alpha = 0.15f),
        Color.LightGray.copy(alpha = 0.4f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )
}