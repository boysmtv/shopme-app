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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.mtv.app.shopme.feature.auth.contract.SplashDataListener
import com.mtv.app.shopme.feature.auth.contract.SplashEventListener
import com.mtv.app.shopme.feature.auth.contract.SplashNavigationListener
import com.mtv.app.shopme.feature.auth.contract.SplashStateListener

@Preview(showBackground = true, device = Devices.PIXEL_4)
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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            AppColor.GreenSoft,
                            AppColor.WhiteSoft,
                            AppColor.White
                        )
                    )
                )
        )

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
                    .height(420.dp),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.image_food_1),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp),
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
                    onClick = { uiNavigation.onNavigateToLogin() },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColor.Green.copy(alpha = 0.8f)
                    ),
                    modifier = Modifier.size(70.dp)
                ) {
                    Text(
                        text = "GO",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        fontFamily = PoppinsFont
                    )
                }
            }
        }
    }
}