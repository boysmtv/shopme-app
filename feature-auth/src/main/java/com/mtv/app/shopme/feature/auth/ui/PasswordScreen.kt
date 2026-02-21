/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: PasswordScreen.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 09.26
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
import com.mtv.app.shopme.feature.auth.contract.PasswordDataListener
import com.mtv.app.shopme.feature.auth.contract.PasswordEventListener
import com.mtv.app.shopme.feature.auth.contract.PasswordNavigationListener
import com.mtv.app.shopme.feature.auth.contract.PasswordStateListener

@Composable
fun PasswordScreen(
    uiState: PasswordStateListener,
    uiData: PasswordDataListener,
    uiEvent: PasswordEventListener,
    uiNavigation: PasswordNavigationListener
) {

    var currentVisible by remember { mutableStateOf(false) }
    var newVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }

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
                    painter = painterResource(id = R.drawable.image_cafe_4),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            // CARD FORM (FULL HEIGHT)
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
                        text = "Change Password",
                        fontSize = 22.sp,
                        fontFamily = PoppinsFont,
                        color = AppColor.Black,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    PasswordField(
                        title = "Current Password",
                        value = uiData.currentPassword,
                        visible = currentVisible,
                        onToggle = { currentVisible = !currentVisible },
                        onChange = uiEvent.onCurrentPasswordChange
                    )

                    Spacer(Modifier.height(16.dp))

                    PasswordField(
                        title = "New Password",
                        value = uiData.newPassword,
                        visible = newVisible,
                        onToggle = { newVisible = !newVisible },
                        onChange = uiEvent.onNewPasswordChange
                    )

                    Spacer(Modifier.height(16.dp))

                    PasswordField(
                        title = "Confirm Password",
                        value = uiData.confirmPassword,
                        visible = confirmVisible,
                        onToggle = { confirmVisible = !confirmVisible },
                        onChange = uiEvent.onConfirmPasswordChange
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = uiEvent.onSubmitClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColor.Green
                        )
                    ) {
                        Text(
                            "Update Password",
                            fontFamily = PoppinsFont,
                            color = Color.White
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "Back",
                        fontFamily = PoppinsFont,
                        color = AppColor.Green,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable { uiNavigation.onBack() }
                    )
                }
            }
        }
    }
}

/* ===== Reusable Password Field ===== */

@Composable
private fun PasswordField(
    title: String,
    value: String,
    visible: Boolean,
    onToggle: () -> Unit,
    onChange: (String) -> Unit
) {

    Text(title, fontFamily = PoppinsFont)
    Spacer(Modifier.height(8.dp))

    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        leadingIcon = {
            Icon(Icons.Outlined.Lock, contentDescription = null)
        },
        trailingIcon = {
            IconButton(onClick = onToggle) {
                Icon(
                    imageVector = if (visible)
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
                "Enter $title",
                fontFamily = PoppinsFont,
                fontSize = 12.sp
            )
        },
        visualTransformation =
            if (visible) VisualTransformation.None
            else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp)
    )
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun PasswordScreenPreview() {
    PasswordScreen(
        uiState = PasswordStateListener(),
        uiData = PasswordDataListener(
            currentPassword = "",
            newPassword = "",
            confirmPassword = ""
        ),
        uiEvent = PasswordEventListener(
            onCurrentPasswordChange = {},
            onNewPasswordChange = {},
            onConfirmPasswordChange = {},
            onSubmitClick = {}
        ),
        uiNavigation = PasswordNavigationListener(
            onBack = {}
        )
    )
}