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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

        Column {

            Image(
                painter = painterResource(R.drawable.image_cafe_1),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )

            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = AppColor.White)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {

                    Text(
                        "Reset Password",
                        fontFamily = PoppinsFont,
                        fontSize = 22.sp,
                        color = AppColor.Black,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(20.dp))

                    Text("Email", fontFamily = PoppinsFont)
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiData.email,
                        onValueChange = uiEvent.onEmailChange,
                        leadingIcon = { Icon(Icons.Outlined.Email, null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = uiEvent.onResetClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColor.Green),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("Send Reset Link", color = Color.White, fontFamily = PoppinsFont)
                    }

                    Spacer(Modifier.height(16.dp))

                    TextButton(onClick = uiNavigation.onNavigateToLogin) {
                        Text("Back to Login", color = AppColor.Green, fontFamily = PoppinsFont)
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
            email = "john@example.com"
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