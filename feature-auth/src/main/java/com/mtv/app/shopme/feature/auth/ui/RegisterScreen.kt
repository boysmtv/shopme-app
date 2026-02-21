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
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.mtv.app.shopme.feature.auth.contract.RegisterDataListener
import com.mtv.app.shopme.feature.auth.contract.RegisterEventListener
import com.mtv.app.shopme.feature.auth.contract.RegisterNavigationListener
import com.mtv.app.shopme.feature.auth.contract.RegisterStateListener

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

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .heightIn(max = 220.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image_cafe_5),
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
                        text = "Create Account",
                        fontSize = 22.sp,
                        fontFamily = PoppinsFont,
                        color = AppColor.Black,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    // EMAIL
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
                                "Enter Your Email",
                                fontFamily = PoppinsFont,
                                fontSize = 12.sp
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    // PASSWORD
                    Text("Password", fontFamily = PoppinsFont)
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiData.password,
                        onValueChange = uiEvent.onPasswordChange,
                        leadingIcon = {
                            Icon(Icons.Outlined.Lock, contentDescription = null)
                        },
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
                        placeholder = {
                            Text(
                                "Enter your password",
                                fontFamily = PoppinsFont,
                                fontSize = 12.sp
                            )
                        },
                        visualTransformation =
                            if (passwordVisible) VisualTransformation.None
                            else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    // CONFIRM PASSWORD
                    Text("Confirm Password", fontFamily = PoppinsFont)
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiData.confirmPassword,
                        onValueChange = uiEvent.onConfirmPasswordChange,
                        leadingIcon = {
                            Icon(Icons.Outlined.Lock, contentDescription = null)
                        },
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
                        placeholder = {
                            Text(
                                "Enter your confirm password",
                                fontFamily = PoppinsFont,
                                fontSize = 12.sp
                            )
                        },
                        visualTransformation =
                            if (confirmVisible) VisualTransformation.None
                            else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = uiEvent.onRegisterClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColor.Green
                        )
                    ) {
                        Text(
                            "Register",
                            fontFamily = PoppinsFont,
                            color = Color.White
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "Already have account? Login",
                        fontFamily = PoppinsFont,
                        color = AppColor.Green,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                uiNavigation.onNavigateToLogin()
                            }
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
            email = "",
            password = "",
            confirmPassword = ""
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