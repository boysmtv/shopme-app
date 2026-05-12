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
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Lock
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
import com.mtv.app.shopme.feature.auth.contract.ResetEvent
import com.mtv.app.shopme.feature.auth.contract.ResetStage
import com.mtv.app.shopme.feature.auth.contract.ResetUiState
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.uicomponent.core.component.loading.LoadingV1

@Composable
fun ResetScreen(
    state: ResetUiState,
    event: (ResetEvent) -> Unit
) {

    val isLoading = state.reset is LoadState.Loading

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
                        text = when (state.stage) {
                            ResetStage.EMAIL -> "Masukkan email untuk menerima OTP reset password"
                            ResetStage.OTP -> "Masukkan OTP yang dikirim ke email kamu"
                            ResetStage.PASSWORD -> "Masukkan password baru untuk akun kamu"
                        },
                        fontFamily = PoppinsFont,
                        fontSize = 13.sp,
                        color = AppColor.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(20.dp))

                    when (state.stage) {
                        ResetStage.EMAIL -> {
                            ResetField(
                                title = "Email",
                                value = state.email,
                                placeholder = "Enter your email",
                                leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
                                onValueChange = { event(ResetEvent.OnEmailChange(it)) }
                            )
                        }

                        ResetStage.OTP -> {
                            ResetField(
                                title = "OTP Code",
                                value = state.otp,
                                placeholder = "Enter OTP code",
                                leadingIcon = { Icon(Icons.Outlined.Key, contentDescription = null) },
                                onValueChange = { event(ResetEvent.OnOtpChange(it)) }
                            )
                        }

                        ResetStage.PASSWORD -> {
                            ResetField(
                                title = "New Password",
                                value = state.newPassword,
                                placeholder = "Enter new password",
                                leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
                                onValueChange = { event(ResetEvent.OnNewPasswordChange(it)) }
                            )

                            Spacer(Modifier.height(16.dp))

                            ResetField(
                                title = "Confirm Password",
                                value = state.confirmPassword,
                                placeholder = "Confirm new password",
                                leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
                                onValueChange = { event(ResetEvent.OnConfirmPasswordChange(it)) }
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = { event(ResetEvent.OnResetClick) },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColor.Green
                        )
                    ) {
                        if (isLoading) {
                            LoadingV1()
                        } else {
                            Text(
                                when (state.stage) {
                                    ResetStage.EMAIL -> "Send OTP"
                                    ResetStage.OTP -> "Verify OTP"
                                    ResetStage.PASSWORD -> "Update Password"
                                },
                                fontFamily = PoppinsFont,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    TextButton(
                        onClick = { event(ResetEvent.OnBackClick) },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            when (state.stage) {
                                ResetStage.EMAIL -> "Back to Login"
                                ResetStage.OTP -> "Back to Email"
                                ResetStage.PASSWORD -> "Back to OTP"
                            },
                            color = AppColor.Green,
                            fontFamily = PoppinsFont
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ResetField(
    title: String,
    value: String,
    placeholder: String,
    leadingIcon: @Composable () -> Unit,
    onValueChange: (String) -> Unit
) {
    Text(title, fontFamily = PoppinsFont)
    Spacer(Modifier.height(8.dp))

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = leadingIcon,
        placeholder = {
            Text(
                placeholder,
                fontFamily = PoppinsFont,
                fontSize = 12.sp
            )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp)
    )
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun ResetScreenPreview() {
    ResetScreen(
        state = ResetUiState(
            email = "test@mail.com"
        ),
        event = {}
    )
}
