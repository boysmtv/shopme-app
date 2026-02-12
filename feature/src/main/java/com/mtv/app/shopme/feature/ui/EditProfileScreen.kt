/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: EditProfileScreen.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 14.02
 */

package com.mtv.app.shopme.feature.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.feature.contract.*
import com.mtv.based.uicomponent.core.component.input.compose.BaseTextInput

@Composable
fun EditProfileScreen(
    uiState: EditProfileStateListener,
    uiData: EditProfileDataListener,
    uiEvent: EditProfileEventListener,
    uiNavigation: EditProfileNavigationListener
) {
    var name by remember { mutableStateOf(uiData.name) }
    var phone by remember { mutableStateOf(uiData.phone) }
    var email by remember { mutableStateOf(uiData.email) }
    var address by remember { mutableStateOf(uiData.address) }
    var map by remember { mutableStateOf(uiData.map) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        AppColor.Orange,
                        AppColor.LightOrange
                    )
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { uiNavigation.onBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
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

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp),
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
                FormField("Nama Lengkap", name) { name = it }
                Spacer(modifier = Modifier.height(16.dp))
                FormField("Nomor Telepon", phone, keyboardType = KeyboardType.Phone) { phone = it }
                Spacer(modifier = Modifier.height(16.dp))
                FormField("Email", email, keyboardType = KeyboardType.Phone) { email = it }
                Spacer(modifier = Modifier.height(16.dp))
                FormField("Alamat", address) { address = it }
                Spacer(modifier = Modifier.height(16.dp))
                FormField("Map", map) { map = it }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        uiEvent.onSaveClicked(name, phone, address)
                        uiNavigation.onBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColor.Orange)
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
}

@Composable
fun FormField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontFamily = PoppinsFont,
            fontSize = 14.sp,
            color = Color.Gray
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontFamily = PoppinsFont,
                fontSize = 14.sp,
                color = Color.Black
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                errorContainerColor = Color.White,
                cursorColor = AppColor.Orange,
                focusedIndicatorColor = AppColor.Orange,
                unfocusedIndicatorColor = Color.LightGray,
                disabledIndicatorColor = Color.LightGray,
                errorIndicatorColor = Color.Red,
                focusedLabelColor = AppColor.Orange,
                unfocusedLabelColor = Color.Gray
            )
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen(
        uiState = EditProfileStateListener(),
        uiData = EditProfileDataListener(),
        uiEvent = EditProfileEventListener(
            onSaveClicked = { name, phone, address ->
            }
        ),
        uiNavigation = EditProfileNavigationListener(
            onBack = {}
        )
    )
}
