/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatSupportScreen.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 11.13
 */

package com.mtv.app.shopme.feature.customer.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.feature.customer.contract.ChatSupportDataListener
import com.mtv.app.shopme.feature.customer.contract.ChatSupportEventListener
import com.mtv.app.shopme.feature.customer.contract.ChatSupportNavigationListener
import com.mtv.app.shopme.feature.customer.contract.ChatSupportStateListener
import com.mtv.app.shopme.feature.customer.contract.SupportMessage

@Composable
fun ChatSupportScreen(
    uiState: ChatSupportStateListener,
    uiData: ChatSupportDataListener,
    uiEvent: ChatSupportEventListener,
    uiNavigation: ChatSupportNavigationListener
) {

    Column(modifier = Modifier.fillMaxSize()) {

        // ================= HEADER =================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = uiNavigation.onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(AppColor.GreenSoft, RoundedCornerShape(50)),
                contentAlignment = Alignment.Center
            ) {
                Text("S", color = AppColor.Green, fontFamily = PoppinsFont)
            }

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    "Support Agent",
                    fontFamily = PoppinsFont,
                    fontSize = 14.sp
                )
                Text(
                    if (uiData.isAgentTyping) "Mengetik..."
                    else "Online • Respon cepat",
                    fontSize = 11.sp,
                    color = Color(0xFF4CAF50),
                    fontFamily = PoppinsFont
                )
            }
        }

        Divider()

        // ================= CHAT LIST =================
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .background(AppColor.WhiteSoft)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(uiData.messages) { message ->
                ChatBubbleSuper(message)
            }

            if (uiData.isAgentTyping) {
                item {
                    TypingIndicatorDots()
                }
            }
        }

        // ================= INPUT BAR =================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppColor.WhiteSoft)
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedTextField(
                value = uiData.currentMessage,
                onValueChange = uiEvent.onMessageChange,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(22.dp),
                placeholder = {
                    Text(
                        "Tulis pesan...",
                        fontFamily = PoppinsFont,
                        fontSize = 13.sp
                    )
                }
            )

            Spacer(Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .background(AppColor.Green, RoundedCornerShape(50))
                    .padding(10.dp)
                    .size(38.dp)
            ) {
                IconButton(onClick = uiEvent.onSendMessage) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatBubbleSuper(message: SupportMessage) {

    val isUser = message.isFromUser

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {

        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {

            Box(
                modifier = Modifier
                    .background(
                        if (isUser) AppColor.Green else AppColor.GreenSoft,
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = if (isUser) 18.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 18.dp
                        )
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    message.message,
                    color = if (isUser) Color.White else Color.Black,
                    fontFamily = PoppinsFont,
                    fontSize = 13.sp
                )
            }

            Spacer(Modifier.height(2.dp))

            Text(
                message.timestamp,
                fontSize = 10.sp,
                color = Color.Gray,
                fontFamily = PoppinsFont
            )
        }
    }
}

@Composable
private fun TypingIndicatorDots() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(18.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                "● ● ●",
                color = Color.Gray,
                fontSize = 12.sp,
                fontFamily = PoppinsFont
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun ChatSupportPreview() {
    ChatSupportScreen(
        uiState = ChatSupportStateListener(),
        uiData = ChatSupportDataListener(
            messages = listOf(
                SupportMessage("1", "Halo 👋 Selamat datang di Shopme Support.", false, "09:58"),
                SupportMessage("2", "Ada yang bisa kami bantu hari ini?", false, "09:58"),
                SupportMessage("3", "Halo kak, saya mau tanya pesanan saya belum sampai.", true, "09:59"),
                SupportMessage("4", "Baik kak 😊 Boleh diinformasikan nomor pesanan nya?", false, "09:59"),
                SupportMessage("5", "ORD-20260222-8891", true, "10:00"),
                SupportMessage("6", "Terima kasih, kami cek dulu ya kak ⏳", false, "10:00"),
                SupportMessage("7", "Baik kak, ditunggu 🙏", true, "10:01"),
                SupportMessage("8", "Pesanan kakak saat ini sedang dalam perjalanan 🚚", false, "10:02"),
                SupportMessage("9", "Estimasi tiba sekitar 20-30 menit lagi.", false, "10:02"),
                SupportMessage("10", "Oh baik, berarti masih normal ya?", true, "10:03"),
                SupportMessage("11", "Betul kak 👍 Tidak ada kendala di pengiriman.", false, "10:03"),
                SupportMessage("12", "Kalau nanti ada masalah saya hubungi lagi ya.", true, "10:04"),
                SupportMessage("13", "Siap kak, kami siap membantu kapan saja 😊", false, "10:04"),
                SupportMessage("14", "Terima kasih supportnya 🙌", true, "10:05"),
                SupportMessage("15", "Sama-sama kak 💚 Semoga harinya menyenangkan.", false, "10:05"),
                SupportMessage("16", "Oh iya kak, apakah ada promo hari ini?", true, "10:06"),
                SupportMessage("17", "Ada kak 🎉 Promo Diskon 20% untuk pembelian di atas 50rb.", false, "10:06"),
                SupportMessage("18", "Wah menarik, berlaku sampai kapan?", true, "10:07"),
                SupportMessage("19", "Promo berlaku sampai malam ini pukul 23:59 ya kak.", false, "10:07"),
                SupportMessage("20", "Siap kak, nanti saya coba order lagi.", true, "10:08"),
                SupportMessage("21", "Baik kak, terima kasih sudah menggunakan Shopme 💚", false, "10:08"),
                SupportMessage("22", "Jika ada kendala, silakan hubungi kami kembali.", false, "10:09"),
                SupportMessage("23", "Oke siap 👍", true, "10:09"),
                SupportMessage("24", "Have a great day kak 🌟", false, "10:09")
            )
        ),
        uiEvent = ChatSupportEventListener(),
        uiNavigation = ChatSupportNavigationListener()
    )
}