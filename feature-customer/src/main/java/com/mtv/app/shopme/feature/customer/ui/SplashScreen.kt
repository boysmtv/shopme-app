/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SplashScreen.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 14.32
 */

package com.mtv.app.shopme.feature.customer.ui

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.feature.customer.contract.SplashDataListener
import com.mtv.app.shopme.feature.customer.contract.SplashEventListener
import com.mtv.app.shopme.feature.customer.contract.SplashNavigationListener
import com.mtv.app.shopme.feature.customer.contract.SplashStateListener
import kotlin.random.Random

@Preview(
    showBackground = true,
    device = Devices.PIXEL_4
)
@Composable
fun SplashScreenPreview() {
    SplashScreen(
        uiState = SplashStateListener(),
        uiData = SplashDataListener(),
        uiEvent = SplashEventListener({}),
        uiNavigation = SplashNavigationListener({})
    )
}

@Composable
fun SplashScreen(
    uiState: SplashStateListener,
    uiData: SplashDataListener,
    uiEvent: SplashEventListener,
    uiNavigation: SplashNavigationListener
) {
    val scale = remember { Animatable(0.8f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            1.1f,
            animationSpec = tween(
                durationMillis = 700,
                easing = { OvershootInterpolator(4f).getInterpolation(it) }
            )
        )
        scale.animateTo(1f, tween(300))
        alpha.animateTo(1f, tween(500))

//        uiEvent.onDoSplash()
        uiNavigation.onNavigateToHome()
    }

//    LaunchedEffect(uiState.splashState) {
//        if (uiState.splashState is ResourceFirebase.Success) {
//            uiNavigation.onNavigateToLogin()
//        }
//    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .scale(scale.value)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            AppColor.LightOrange,
                            AppColor.WhiteSoft
                        )
                    )
                )
        ) {

            AbstractSplashBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 28.dp, top = 40.dp),
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    text = "Shopme",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColor.Orange.copy(alpha = alpha.value)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "App",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Medium,
                    color = AppColor.Orange.copy(alpha = alpha.value)
                )

                Spacer(modifier = Modifier.height(16.dp))

                PulsingLoadingText()
            }
        }
    }
}

@Composable
private fun AbstractSplashBackground() {
    Box(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .size(260.dp)
                .offset(x = 160.dp, y = 220.dp)
                .clip(RoundedCornerShape(140.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            AppColor.Orange,
                            AppColor.WhiteSoft
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .size(220.dp)
                .offset(x = (-60).dp, y = 420.dp)
                .clip(RoundedCornerShape(120.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            AppColor.WhiteSoft,
                            AppColor.Orange
                        )
                    )
                )
        )

        NoiseOverlay()
    }
}

@Composable
private fun NoiseOverlay() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val random = Random(1)

        repeat(7_000) {
            val x = random.nextFloat() * size.width
            val y = random.nextFloat() * size.height

            drawCircle(
                color = Color.White.copy(alpha = 0.015f),
                radius = random.nextFloat() * 1.4f,
                center = androidx.compose.ui.geometry.Offset(x, y)
            )
        }
    }
}

@Composable
fun PulsingLoadingText(text: String = "Loading...") {
    val alpha = remember { Animatable(0.3f) }

    LaunchedEffect(Unit) {
        while (true) {
            alpha.animateTo(1f, tween(600))
            alpha.animateTo(0.3f, tween(600))
        }
    }

    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = AppColor.Orange.copy(alpha = alpha.value)
    )
}