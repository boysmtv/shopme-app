/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: LoginScreen.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.50
 */

package com.mtv.app.shopme.feature.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.feature.auth.contract.LoginDataListener
import com.mtv.app.shopme.feature.auth.contract.LoginEventListener
import com.mtv.app.shopme.feature.auth.contract.LoginNavigationListener
import com.mtv.app.shopme.feature.auth.contract.LoginStateListener

@Composable
fun LoginScreen(
    uiState: LoginStateListener,
    uiData: LoginDataListener,
    uiEvent: LoginEventListener,
    uiNavigation: LoginNavigationListener
) {

    var remember by remember { mutableStateOf(false) }

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
                    painter = painterResource(id = R.drawable.image_cafe_3),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
            }

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
                        text = "Sign in Your Account",
                        fontSize = 22.sp,
                        fontFamily = PoppinsFont,
                        color = AppColor.Black,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    // EMAIL TITLE
                    Text(
                        text = "Email",
                        fontFamily = PoppinsFont,
                        fontSize = 14.sp,
                        color = AppColor.Black
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiData.email,
                        onValueChange = uiEvent.onEmailChange,
                        placeholder = {
                            Text(
                                "Enter your email",
                                fontFamily = PoppinsFont,
                                fontSize = 12.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Email,
                                contentDescription = null,
                                tint = AppColor.Gray
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Password",
                        fontFamily = PoppinsFont,
                        fontSize = 14.sp,
                        color = AppColor.Black
                    )

                    Spacer(Modifier.height(8.dp))

                    var passwordVisible by remember { mutableStateOf(false) }

                    OutlinedTextField(
                        value = uiData.password,
                        onValueChange = uiEvent.onPasswordChange,
                        placeholder = {
                            Text(
                                "Enter your password",
                                fontFamily = PoppinsFont,
                                fontSize = 12.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = null,
                                tint = AppColor.Gray
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
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
                        visualTransformation = if (passwordVisible)
                            androidx.compose.ui.text.input.VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(Modifier.height(18.dp))

                    // Remember + Forgot
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Checkbox(
                                checked = remember,
                                onCheckedChange = { remember = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = AppColor.Green
                                ),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Remember me",
                                fontFamily = PoppinsFont,
                                fontSize = 13.sp
                            )
                        }

                        Text(
                            "Forget Password?",
                            color = AppColor.Green,
                            fontFamily = PoppinsFont,
                            fontSize = 13.sp,
                            modifier = Modifier.clickable {
                                uiNavigation.onNavigateToForgotPassword()
                            }
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Login Button
                    Button(
                        onClick = uiEvent.onLoginClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColor.Green
                        )
                    ) {
                        Text(
                            "Sign In",
                            fontFamily = PoppinsFont,
                            color = Color.White
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Divider
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f))
                        Text(
                            "  Or sign in with  ",
                            fontSize = 12.sp,
                            fontFamily = PoppinsFont,
                            color = AppColor.Gray
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f))
                    }

                    Spacer(Modifier.height(16.dp))

                    // Social Login
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        SocialButton(R.drawable.icon_google)
                        Spacer(Modifier.width(22.dp))
                        SocialButton(R.drawable.icon_facebook)
                        Spacer(Modifier.width(22.dp))
                        SocialButton(R.drawable.icon_apple)
                    }

                    Spacer(Modifier.height(16.dp))

                    // Sign Up
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            "Don’t have an account? ",
                            fontFamily = PoppinsFont,
                            fontSize = 13.sp
                        )

                        Text(
                            "Sign Up",
                            color = AppColor.Green,
                            fontFamily = PoppinsFont,
                            fontSize = 13.sp,
                            modifier = Modifier.clickable {
                                uiNavigation.onNavigateToRegister()
                            }
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun SocialButton(icon: Int) {
    Box(
        modifier = Modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(AppColor.GreenSoft),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        uiState = LoginStateListener(),
        uiData = LoginDataListener(
            email = "",
            password = ""
        ),
        uiEvent = LoginEventListener(
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {}
        ),
        uiNavigation = LoginNavigationListener(
            onNavigateToRegister = {},
            onNavigateToForgotPassword = {},
            onBack = {}
        )
    )
}