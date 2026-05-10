/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SplashScreen.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 14.32
 */

package com.mtv.app.shopme.feature.auth.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.feature.auth.contract.SplashBlockingState
import com.mtv.app.shopme.feature.auth.contract.SplashEvent
import com.mtv.app.shopme.feature.auth.contract.SplashUiState
import com.mtv.based.core.network.utils.LoadState

@Composable
fun SplashScreen(
    state: SplashUiState,
    event: (SplashEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        AppColor.GreenSoft,
                        AppColor.WhiteSoft,
                        AppColor.White
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 54.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier.height(120.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .width(20.dp)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        listOf("S", "H", "O", "P", " ", "M", "E").forEach { letter ->
                            Text(
                                text = letter,
                                fontSize = 12.sp,
                                fontFamily = PoppinsFont,
                                color = Color.Black
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(Color.Black)
                )

                Spacer(modifier = Modifier.width(20.dp))

                Column {
                    Text(
                        text = "Fresh & Tasty",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PoppinsFont,
                        lineHeight = 44.sp
                    )

                    Text(
                        text = "Everyday",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PoppinsFont,
                        lineHeight = 44.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .heightIn(max = 280.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image_food_1),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentScale = ContentScale.Fit
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "A beautiful plant is like having a friend around the house.",
                    fontSize = 14.sp,
                    color = Color.Black.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    fontFamily = PoppinsFont
                )

                Spacer(modifier = Modifier.height(20.dp))

                Canvas(
                    modifier = Modifier
                        .width(60.dp)
                        .height(20.dp)
                ) {
                    drawArc(
                        color = Color.Black.copy(alpha = 0.35f),
                        startAngle = 210f,
                        sweepAngle = 120f,
                        useCenter = false,
                        style = Stroke(width = 3f)
                    )
                }

                Button(
                    onClick = { state.blockingState?.let { event(SplashEvent.Load) } },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColor.Green.copy(alpha = 0.8f)
                    ),
                    enabled = state.blockingState != null,
                    modifier = Modifier.size(70.dp)
                ) {
                    if (state.splash is LoadState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "GO",
                            color = Color.White,
                            fontFamily = PoppinsFont,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        state.blockingState?.let { blockingState ->
            SplashBlockingCard(
                blockingState = blockingState,
                onRetry = { event(SplashEvent.Load) }
            )
        }
    }
}

@Composable
private fun SplashBlockingCard(
    blockingState: SplashBlockingState,
    onRetry: () -> Unit
) {
    val title = when (blockingState) {
        is SplashBlockingState.Maintenance -> "Under Maintenance"
        SplashBlockingState.ForceUpdate -> "Update Required"
    }
    val message = when (blockingState) {
        is SplashBlockingState.Maintenance -> blockingState.message ?: "Shopme sedang maintenance. Coba beberapa saat lagi."
        SplashBlockingState.ForceUpdate -> "Versi aplikasi ini sudah tidak didukung. Update aplikasi untuk lanjut."
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.32f))
            .clickable(enabled = false) {},
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            tonalElevation = 8.dp,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = title,
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Black
                )
                Text(
                    text = message,
                    fontFamily = PoppinsFont,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = Color.Black.copy(alpha = 0.68f)
                )
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(containerColor = AppColor.Green),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = if (blockingState is SplashBlockingState.Maintenance) "Check Again" else "Retry Check",
                        fontFamily = PoppinsFont,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun SplashScreenPreview() {
    SplashScreen(
        state = SplashUiState(),
        event = {}
    )
}
