/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ContactSupportScreen.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 10.39
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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.feature.customer.contract.SupportDataListener
import com.mtv.app.shopme.feature.customer.contract.SupportEventListener
import com.mtv.app.shopme.feature.customer.contract.SupportNavigationListener
import com.mtv.app.shopme.feature.customer.contract.SupportStateListener
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextAlign

@Composable
fun SupportScreen(
    uiState: SupportStateListener,
    uiData: SupportDataListener,
    uiEvent: SupportEventListener,
    uiNavigation: SupportNavigationListener
) {

    val isOpen = remember {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        hour in 8..21
    }

    val haptic = LocalHapticFeedback.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(AppColor.Green, AppColor.GreenSoft)
                )
            )
            .statusBarsPadding()
    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = uiNavigation.onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }

                    Text(
                        "Support Center",
                        fontFamily = PoppinsFont,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }

                Spacer(Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    PulseOnlineDot(isOpen)

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = if (isOpen)
                            "Online • Respon < 2 menit"
                        else
                            "Offline • Buka 08:00",
                        color = Color.White.copy(.95f),
                        fontFamily = PoppinsFont,
                        fontSize = 13.sp
                    )
                }

                Spacer(Modifier.height(16.dp))

                HeroSupportCard(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        uiNavigation.onLiveChat()
                    }
                )
            }


            Spacer(Modifier.height(8.dp))

            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = MaterialTheme.colorScheme.background
            ) {

                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {

                    Text(
                        "Kontak Alternatif",
                        fontFamily = PoppinsFont,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    AnimatedSupportCard(
                        "WhatsApp",
                        "Chat via WhatsApp",
                        Icons.AutoMirrored.Filled.Chat
                    ) {
                        uiEvent.onOpenWhatsapp(uiData.whatsapp)
                    }

                    AnimatedSupportCard(
                        "Email Support",
                        "Kirim laporan + lampiran",
                        Icons.Default.Email
                    ) {
                        uiEvent.onOpenEmail(uiData.email)
                    }

                    AnimatedSupportCard(
                        "Call Center",
                        "Hubungi via telepon",
                        Icons.Default.Call
                    ) {
                        uiEvent.onOpenDial(uiData.phone)
                    }

                    Spacer(Modifier.height(6.dp))

                    HorizontalDivider(color = Color.LightGray.copy(.3f))

                    Text(
                        text = "Jam Operasional",
                        fontFamily = PoppinsFont,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Senin - Minggu • 08:00 - 22:00",
                        fontFamily = PoppinsFont
                    )
                }
            }
        }
    }
}

@Composable
private fun PulseOnlineDot(isOnline: Boolean) {

    val infinite = rememberInfiniteTransition()
    val scale by infinite.animateFloat(
        1f, 1.4f,
        animationSpec = infiniteRepeatable(
            tween(900),
            RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .size(10.dp)
            .scale(if (isOnline) scale else 1f)
            .background(
                if (isOnline) Color(0xFF4CAF50) else Color.Gray,
                RoundedCornerShape(50)
            )
    )
}

@Composable
private fun HeroSupportCard(onClick: () -> Unit) {

    val pressed = remember { MutableInteractionSource() }
    val isPressed = pressed.collectIsPressedAsState().value

    val scale by animateFloatAsState(
        if (isPressed) 0.97f else 1f,
        spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Card(
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColor.GreenSoft.copy(alpha = 0.25f)
        ),
        modifier = Modifier
            .border(
                1.dp,
                Color.White.copy(alpha = 0.18f),
                RoundedCornerShape(26.dp)
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .scale(scale)
                .padding(horizontal = 22.dp, vertical = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(74.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(50)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Chat,
                    contentDescription = null,
                    tint = AppColor.Green,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "Live Chat Support",
                fontFamily = PoppinsFont,
                fontSize = 17.sp,
                color = Color.White
            )

            Spacer(Modifier.height(6.dp))

            Text(
                "Terhubung langsung dengan tim support\nRespon cepat & realtime",
                fontFamily = PoppinsFont,
                fontSize = 12.sp,
                color = Color.White.copy(.9f),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(50),
                        ambientColor = AppColor.Green.copy(alpha = 0.35f),
                        spotColor = AppColor.Green.copy(alpha = 0.35f)
                    )
                    .background(
                        AppColor.Green,
                        RoundedCornerShape(50)
                    )
                    .clickable(
                        interactionSource = pressed,
                        indication = ripple(bounded = true)
                    ) { onClick() }
                    .padding(horizontal = 20.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.AutoMirrored.Filled.Chat,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(Modifier.width(6.dp))

                Text(
                    text = "Mulai Chat",
                    fontFamily = PoppinsFont,
                    fontSize = 13.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun AnimatedSupportCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {

    val press = remember { MutableInteractionSource() }
    val scale by animateFloatAsState(
        if (press.collectIsPressedAsState().value) 0.97f else 1f,
        spring()
    )

    ElevatedCard(shape = RoundedCornerShape(18.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .scale(scale)
                .clickable(interactionSource = press, indication = ripple()) { onClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(icon, null, tint = AppColor.Green)

            Spacer(Modifier.width(12.dp))

            Column {
                Text(title, fontFamily = PoppinsFont)
                Text(
                    subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = PoppinsFont
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SupportPreview() {
    SupportScreen(
        uiState = SupportStateListener(
            isLoading = false
        ),
        uiData = SupportDataListener(
            phone = "081234567890",
            email = "support@shopme.com",
            whatsapp = "6281234567890"
        ),
        uiEvent = SupportEventListener(
            onOpenWhatsapp = {},
            onOpenEmail = {},
            onOpenDial = {}
        ),
        uiNavigation = SupportNavigationListener(
            onBack = {},
            onLiveChat = {}
        )
    )
}