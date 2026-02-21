/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SecurityScreen.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.12
 */

package com.mtv.app.shopme.feature.customer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.feature.customer.contract.SecurityDataListener
import com.mtv.app.shopme.feature.customer.contract.SecurityEventListener
import com.mtv.app.shopme.feature.customer.contract.SecurityNavigationListener
import com.mtv.app.shopme.feature.customer.contract.SecurityStateListener

@Composable
fun SecurityScreen(
    uiState: SecurityStateListener,
    uiData: SecurityDataListener,
    uiEvent: SecurityEventListener,
    uiNavigation: SecurityNavigationListener
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(AppColor.Green, AppColor.GreenSoft)
                )
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = uiNavigation.onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }

            Text(
                "Keamanan Akun",
                fontFamily = PoppinsFont,
                fontSize = 20.sp,
                color = Color.White
            )
        }

        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                ModernSettingItem(
                    "Ubah Password",
                    "Perbarui password akun",
                    Icons.Default.Lock,
                    uiNavigation.onChangePassword
                )

                ModernSettingItem(
                    title = "Ubah PIN",
                    subtitle = "PIN keamanan transaksi",
                    icon = Icons.Default.Password,
                    onClick = uiNavigation.onChangePin
                )

                ModernSwitchItem(
                    title = "Biometric / Fingerprint",
                    subtitle = "Login lebih cepat & aman",
                    icon = Icons.Default.Fingerprint,
                    checked = uiData.biometricEnabled,
                    onCheckedChange = uiEvent.onToggleBiometric
                )

                Spacer(Modifier.height(20.dp))

                Text("Danger Zone", color = Color.Red)

                Spacer(Modifier.height(10.dp))

                OutlinedButton(
                    onClick = uiEvent.onLogoutAllDevice,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Logout semua perangkat")
                }

                Button(
                    onClick = uiEvent.onDeleteAccount,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Text("Hapus Akun", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ModernSettingItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppColor.WhiteSoft)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(AppColor.Green.copy(.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = AppColor.Green)
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(title, fontFamily = PoppinsFont)
                Text(
                    subtitle,
                    fontSize = 12.sp,
                    color = AppColor.Gray
                )
            }
        }
    }
}

@Composable
fun ModernSwitchItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppColor.WhiteSoft)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(AppColor.Green.copy(.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = AppColor.Green)
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(title, fontFamily = PoppinsFont)
                Text(
                    subtitle,
                    fontSize = 12.sp,
                    color = AppColor.Gray
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SecurityPreview() {
    SecurityScreen(
        uiState = SecurityStateListener(),
        uiData = SecurityDataListener(biometricEnabled = true),
        uiEvent = SecurityEventListener(),
        uiNavigation = SecurityNavigationListener()
    )
}