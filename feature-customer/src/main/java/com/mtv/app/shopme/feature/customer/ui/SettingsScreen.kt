/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AccountSettingsScreen.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 22.59
 */

package com.mtv.app.shopme.feature.customer.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.feature.customer.contract.SettingsDataListener
import com.mtv.app.shopme.feature.customer.contract.SettingsEventListener
import com.mtv.app.shopme.feature.customer.contract.SettingsNavigationListener
import com.mtv.app.shopme.feature.customer.contract.SettingsStateListener

@Composable
fun SettingsScreen(
    uiState: SettingsStateListener,
    uiData: SettingsDataListener,
    uiEvent: SettingsEventListener,
    uiNavigation: SettingsNavigationListener
) {

    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(AppColor.Green, AppColor.GreenSoft)
                )
            )
            .statusBarsPadding()
    ) {

        TopBar(uiNavigation)

        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = AppColor.WhiteSoft
        ) {

            if (uiState.isLoading) {
                SettingsSkeleton()
            } else {

                LazyColumn(
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    item {
                        SettingsGroup("Keamanan") {
                            SettingsItem(
                                icon = Icons.Default.Lock,
                                title = "Keamanan Akun",
                                subtitle = "Password, PIN & verifikasi",
                                onClick = uiNavigation.onSecurity
                            )
                        }
                    }

                    item {
                        SettingsGroup("Preferensi") {

                            SettingsItem(
                                icon = Icons.Default.Notifications,
                                title = "Notifikasi",
                                subtitle = "Atur pemberitahuan",
                                onClick = uiNavigation.onNotification
                            )

                            SettingsSwitchItem(
                                icon = Icons.Default.DarkMode,
                                title = "Dark Mode",
                                subtitle = "Tema gelap otomatis",
                                checked = uiData.darkMode,
                                onCheckedChange = uiEvent.onToggleDarkMode
                            )
                        }
                    }

                    item {
                        SettingsGroup("Bantuan") {
                            SettingsItem(
                                icon = Icons.AutoMirrored.Filled.Help,
                                title = "Pusat Bantuan",
                                subtitle = "FAQ & Support",
                                onClick = uiNavigation.onHelp
                            )
                        }
                    }

                    item {
                        Spacer(Modifier.height(8.dp))

                        LogoutButton(uiEvent.onLogout)
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar(nav: SettingsNavigationListener) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = nav.onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
        }

        Text(
            "Pengaturan Akun",
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = Color.White
        )
    }
}

@Composable
fun SettingsGroup(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {

        Text(
            title,
            fontFamily = PoppinsFont,
            fontSize = 14.sp,
            color = AppColor.Gray
        )

        Spacer(Modifier.height(8.dp))

        ElevatedCard(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = AppColor.WhiteSoft
            ),
        ) {
            Column(
                modifier = Modifier.padding(vertical = 6.dp),
                content = content
            )
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {

    val interaction = remember { MutableInteractionSource() }
    val scale by animateFloatAsState(
        targetValue = if (interaction.collectIsPressedAsState().value) 0.97f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = interaction,
                indication = ripple(bounded = true)
            ) { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(icon, null, tint = AppColor.Green)

        Spacer(Modifier.width(14.dp))

        Column(Modifier.weight(1f)) {
            Text(title, fontFamily = PoppinsFont)
            Text(
                subtitle,
                fontFamily = PoppinsFont,
                fontSize = 12.sp,
                color = AppColor.Gray
            )
        }
    }
}

@Composable
fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    val haptic = LocalHapticFeedback.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(icon, null, tint = AppColor.Green)

        Spacer(Modifier.width(14.dp))

        Column(Modifier.weight(1f)) {
            Text(title, fontFamily = PoppinsFont)
            Text(
                subtitle,
                fontFamily = PoppinsFont,
                fontSize = 12.sp,
                color = AppColor.Gray
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = {
                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                onCheckedChange(it)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = AppColor.Green,
                checkedTrackColor = AppColor.Green.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
private fun LogoutButton(onClick: () -> Unit) {

    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE53935)
        )
    ) {
        Text(
            "Logout",
            fontFamily = PoppinsFont,
            color = Color.White
        )
    }
}

@Composable
fun SettingsSkeleton() {

    val shimmer = rememberInfiniteTransition()
    val alpha by shimmer.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            tween(800),
            RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(4) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Gray.copy(alpha = alpha))
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SettingsScreenProPreview() {
    SettingsScreen(
        uiState = SettingsStateListener(),
        uiData = SettingsDataListener(
            notificationEnabled = true,
            darkMode = false
        ),
        uiEvent = SettingsEventListener(),
        uiNavigation = SettingsNavigationListener()
    )
}