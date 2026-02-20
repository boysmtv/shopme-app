/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: RegisterScreen.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.53
 */

package com.mtv.app.shopme.feature.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.feature.auth.contract.*

@Composable
fun RegisterScreen(
    uiState: RegisterStateListener,
    uiData: RegisterDataListener,
    uiEvent: RegisterEventListener,
    uiNavigation: RegisterNavigationListener
) {

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.GreenSoft)
    ) {

        Column {

            // Header Image
            Image(
                painter = painterResource(R.drawable.image_cafe_5),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
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
                        "Create Account",
                        fontFamily = PoppinsFont,
                        fontSize = 22.sp,
                        color = AppColor.Black,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(20.dp))

                    // Email
                    Text("Email", fontFamily = PoppinsFont)
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiData.email,
                        onValueChange = uiEvent.onEmailChange,
                        leadingIcon = { Icon(Icons.Outlined.Email, null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    // Password
                    Text("Password", fontFamily = PoppinsFont)
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiData.password,
                        onValueChange = uiEvent.onPasswordChange,
                        leadingIcon = { Icon(Icons.Outlined.Lock, null) },
                        trailingIcon = {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible }
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible)
                                        Icons.Outlined.Visibility
                                    else
                                        Icons.Outlined.VisibilityOff,
                                    contentDescription = null,
                                    tint = AppColor.Gray
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    // Confirm Password
                    Text("Confirm Password", fontFamily = PoppinsFont)
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiData.confirmPassword,
                        onValueChange = uiEvent.onConfirmPasswordChange,
                        leadingIcon = { Icon(Icons.Outlined.Lock, null) },
                        trailingIcon = {
                            IconButton(
                                onClick = { confirmVisible = !confirmVisible }
                            ) {
                                Icon(
                                    imageVector = if (confirmVisible)
                                        Icons.Outlined.Visibility
                                    else
                                        Icons.Outlined.VisibilityOff,
                                    contentDescription = null,
                                    tint = AppColor.Gray
                                )
                            }
                        },
                        visualTransformation = if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = uiEvent.onRegisterClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColor.Green),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("Register", color = Color.White, fontFamily = PoppinsFont)
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "Already have account? Login",
                        color = AppColor.Green,
                        fontFamily = PoppinsFont,
                        modifier = Modifier.clickable { uiNavigation.onNavigateToLogin() }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(
        uiState = RegisterStateListener(),
        uiData = RegisterDataListener(
            email = "john@example.com",
            password = "123456",
            confirmPassword = "123456"
        ),
        uiEvent = RegisterEventListener(
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onRegisterClick = {}
        ),
        uiNavigation = RegisterNavigationListener(
            onNavigateToLogin = {},
            onBack = {}
        )
    )
}