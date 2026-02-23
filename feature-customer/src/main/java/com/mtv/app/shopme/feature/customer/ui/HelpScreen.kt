/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HelpScreen.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 10.26
 */

package com.mtv.app.shopme.feature.customer.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.feature.customer.contract.HelpDataListener
import com.mtv.app.shopme.feature.customer.contract.HelpEventListener
import com.mtv.app.shopme.feature.customer.contract.HelpFaq
import com.mtv.app.shopme.feature.customer.contract.HelpNavigationListener
import com.mtv.app.shopme.feature.customer.contract.HelpStateListener

@Composable
fun HelpScreen(
    uiState: HelpStateListener,
    uiData: HelpDataListener,
    uiEvent: HelpEventListener,
    uiNavigation: HelpNavigationListener
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

        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = uiNavigation.onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }

            Text(
                "Pusat Bantuan",
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = Color.White
            )
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = MaterialTheme.colorScheme.background
        ) {

            LazyColumn(
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                item {
                    Text(
                        "FAQ",
                        fontFamily = PoppinsFont,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                items(uiData.faq) { faq ->
                    HelpFaqItem(faq)
                }

                item {
                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = uiNavigation.onContact,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Chat, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Hubungi Support", fontFamily = PoppinsFont)
                    }
                }
            }
        }
    }
}

@Composable
fun HelpFaqItem(
    faq: HelpFaq,
    onToggle: (() -> Unit)? = null
) {

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppColor.WhiteSoft)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle?.invoke() }
                .padding(14.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.Help, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(10.dp))

                Text(
                    faq.question,
                    fontFamily = PoppinsFont,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    Icons.Default.ExpandMore,
                    contentDescription = null,
                    modifier = Modifier.rotate(if (faq.expanded) 180f else 0f)
                )
            }

            AnimatedVisibility(
                visible = faq.expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        faq.answer,
                        fontFamily = PoppinsFont,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun HelpScreenExpandedPreview() {
    HelpScreen(
        uiState = HelpStateListener(
            isLoading = false
        ),
        uiData = HelpDataListener(
            faq = listOf(
                HelpFaq(
                    question = "Bagaimana cara melacak pesanan?",
                    answer = "Masuk ke menu Pesanan Saya → pilih pesanan → lihat status pengiriman secara realtime hingga pesanan diterima.",
                    expanded = true
                ),
                HelpFaq(
                    question = "Metode pembayaran apa saja tersedia?",
                    answer = "Kami mendukung transfer bank, e-wallet, virtual account, dan Cash On Delivery (COD) untuk area tertentu.",
                    expanded = true
                ),
                HelpFaq(
                    question = "Bagaimana cara menghubungi support?",
                    answer = "Gunakan tombol Hubungi Support di bagian bawah halaman ini. Tim kami tersedia 24/7 untuk membantu Anda.",
                    expanded = true
                )
            )
        ),
        uiEvent = HelpEventListener(),
        uiNavigation = HelpNavigationListener()
    )
}
