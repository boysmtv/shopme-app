/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: EditProfileScreen.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 14.02
 */

package com.mtv.app.shopme.feature.customer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.base.BaseSimpleFormField
import com.mtv.app.shopme.feature.customer.contract.*

@Composable
fun EditProfileScreen(
    uiState: EditProfileStateListener,
    uiData: EditProfileDataListener,
    uiEvent: EditProfileEventListener,
    uiNavigation: EditProfileNavigationListener
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        AppColor.Green,
                        AppColor.GreenSoft
                    )
                )
            )
            .padding(top = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        EditProfileHeader(uiNavigation)

        Spacer(modifier = Modifier.height(16.dp))

        EditProfileMenus(uiEvent, uiData, uiNavigation)
    }
}

@Composable
fun EditProfileHeader(uiNavigation: EditProfileNavigationListener) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { uiNavigation.onBack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Edit Profil",
            fontFamily = PoppinsFont,
            fontSize = 22.sp,
            color = Color.White
        )
    }
}

@Composable
fun EditProfileMenus(
    uiEvent: EditProfileEventListener,
    uiData: EditProfileDataListener,
    uiNavigation: EditProfileNavigationListener
) {

    var name by remember { mutableStateOf(uiData.name) }
    var phone by remember { mutableStateOf(uiData.phone) }
    var email by remember { mutableStateOf(uiData.email) }

    Card(
        modifier = Modifier
            .fillMaxSize(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        colors = CardDefaults.cardColors(containerColor = AppColor.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            BaseSimpleFormField(
                label = "Nama Lengkap",
                value = name
            ) { name = it }

            Spacer(modifier = Modifier.height(16.dp))
            BaseSimpleFormField(
                label = "Nomor Telepon",
                value = phone,
                keyboardType = KeyboardType.Phone
            ) { phone = it }

            Spacer(modifier = Modifier.height(16.dp))
            BaseSimpleFormField(
                label = "Email",
                value = email,
                keyboardType = KeyboardType.Phone
            ) { email = it }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    uiEvent.onSaveClicked(name, phone, email)
                    uiNavigation.onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColor.Green)
            ) {
                Text(
                    text = "Simpan",
                    fontFamily = PoppinsFont,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen(
        uiState = EditProfileStateListener(),
        uiData = EditProfileDataListener(),
        uiEvent = EditProfileEventListener(),
        uiNavigation = EditProfileNavigationListener()
    )
}
