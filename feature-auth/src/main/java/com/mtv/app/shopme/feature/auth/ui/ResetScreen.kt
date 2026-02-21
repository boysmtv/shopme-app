/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ResetScreen.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.55
 */

package com.mtv.app.shopme.feature.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.mtv.app.shopme.feature.auth.contract.ResetDataListener
import com.mtv.app.shopme.feature.auth.contract.ResetEventListener
import com.mtv.app.shopme.feature.auth.contract.ResetNavigationListener
import com.mtv.app.shopme.feature.auth.contract.ResetStateListener

@Composable
fun ResetScreen(
    uiState: ResetStateListener,
    uiData: ResetDataListener,
    uiEvent: ResetEventListener,
    uiNavigation: ResetNavigationListener
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.GreenSoft)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // HEADER IMAGE (max 220)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .heightIn(max = 220.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image_cafe_1),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            // CARD FORM (FULL SISA HEIGHT)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = AppColor.White)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {

                    Text(
                        text = "Reset Password",
                        fontFamily = PoppinsFont,
                        fontSize = 22.sp,
                        color = AppColor.Black,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Enter your email to receive reset link",
                        fontFamily = PoppinsFont,
                        fontSize = 13.sp,
                        color = AppColor.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(20.dp))

                    Text("Email", fontFamily = PoppinsFont)
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiData.email,
                        onValueChange = uiEvent.onEmailChange,
                        leadingIcon = {
                            Icon(Icons.Outlined.Email, contentDescription = null)
                        },
                        placeholder = {
                            Text(
                                "Enter your email",
                                fontFamily = PoppinsFont,
                                fontSize = 12.sp
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = uiEvent.onResetClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColor.Green
                        )
                    ) {
                        Text(
                            "Send Reset Link",
                            fontFamily = PoppinsFont,
                            color = Color.White
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    TextButton(
                        onClick = uiNavigation.onNavigateToLogin,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            "Back to Login",
                            color = AppColor.Green,
                            fontFamily = PoppinsFont
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun ResetScreenPreview() {
    ResetScreen(
        uiState = ResetStateListener(),
        uiData = ResetDataListener(
            email = ""
        ),
        uiEvent = ResetEventListener(
            onEmailChange = {},
            onResetClick = {}
        ),
        uiNavigation = ResetNavigationListener(
            onNavigateToLogin = {},
            onBack = {}
        )
    )
}