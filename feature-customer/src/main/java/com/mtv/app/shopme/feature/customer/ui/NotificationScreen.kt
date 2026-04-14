/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotificationScreen.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.26
 */

package com.mtv.app.shopme.feature.customer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.feature.customer.contract.NotificationEvent
import com.mtv.app.shopme.feature.customer.contract.NotificationUiState

@Composable
fun NotificationScreen(
    state: NotificationUiState,
    event: (NotificationEvent) -> Unit
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

        // ===== HEADER =====
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { event(NotificationEvent.ClickBack) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }

            Text(
                "Notifikasi",
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
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {

                SectionHeader("Aktivitas Notifikasi")
                Spacer(Modifier.height(8.dp))

                SettingCard {
                    SwitchSettingItem(
                        icon = Icons.Default.LocalShipping,
                        title = "Status Pesanan",
                        subtitle = "Update pengiriman & perubahan status",
                        checked = state.orderNotification,
                        onToggle = { event(NotificationEvent.ToggleOrder(it)) }
                    )

                    SwitchSettingItem(
                        icon = Icons.Default.Campaign,
                        title = "Promo & Diskon",
                        subtitle = "Voucher, promo & flash sale",
                        checked = state.promoNotification,
                        onToggle = { event(NotificationEvent.TogglePromo(it)) }
                    )

                    SwitchSettingItem(
                        icon = Icons.AutoMirrored.Filled.Chat,
                        title = "Chat & Pesan",
                        subtitle = "Pesan dari penjual & sistem",
                        checked = state.chatNotification,
                        onToggle = { event(NotificationEvent.ToggleChat(it)) }
                    )
                }

                Spacer(Modifier.height(20.dp))

                SectionHeader("Metode Notifikasi")

                Spacer(Modifier.height(8.dp))

                SettingCard {
                    SwitchSettingItem(
                        icon = Icons.Default.PhoneIphone,
                        title = "Push Notification",
                        subtitle = "Notifikasi langsung ke perangkat",
                        checked = state.pushEnabled,
                        onToggle = { event(NotificationEvent.TogglePush(it)) }
                    )

                    SwitchSettingItem(
                        icon = Icons.Default.Email,
                        title = "Email Notification",
                        subtitle = "Dikirim ke email terdaftar",
                        checked = state.emailEnabled,
                        onToggle = { event(NotificationEvent.ToggleEmail(it)) }
                    )
                }

                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontFamily = PoppinsFont,
        fontSize = 14.sp,
        color = Color.Gray,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun SettingCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColor.WhiteSoft
        ),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            content = content
        )
    }
}

@Composable
fun SwitchSettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            icon,
            contentDescription = null,
            tint = AppColor.Green,
            modifier = Modifier.size(22.dp)
        )

        Spacer(Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(title, fontFamily = PoppinsFont, fontSize = 14.sp)
            Text(
                subtitle,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onToggle
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun NotificationPreview() {
    NotificationScreen(
        state = NotificationUiState(
            orderNotification = true,
            promoNotification = false,
            chatNotification = true,
            pushEnabled = true,
            emailEnabled = false
        ),
        event = {}
    )
}