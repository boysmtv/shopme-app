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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.CircularProgressIndicator
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
import com.mtv.app.shopme.common.shimmerBrush
import com.mtv.app.shopme.feature.customer.contract.ChatSupportEvent
import com.mtv.app.shopme.feature.customer.contract.ChatSupportUiState
import com.mtv.app.shopme.feature.customer.contract.SupportMessage

@Composable
fun ChatSupportScreen(
    state: ChatSupportUiState,
    event: (ChatSupportEvent) -> Unit
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

            IconButton(onClick = { event(ChatSupportEvent.ClickBack) }) {
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
                    state.title,
                    fontFamily = PoppinsFont,
                    fontSize = 14.sp
                )
                Text(
                    if (state.isAgentTyping) "Mengetik..."
                    else state.statusLabel,
                    fontSize = 11.sp,
                    color = Color(0xFF4CAF50),
                    fontFamily = PoppinsFont
                )
            }
        }

        HorizontalDivider()

        // ================= CHAT LIST =================
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .background(AppColor.WhiteSoft)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (state.isLoading) {
                items(4) {
                    SupportBubbleShimmer()
                }
            } else if (state.messages.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Belum ada pesan",
                            color = Color.Gray,
                            fontFamily = PoppinsFont
                        )
                    }
                }
            }

            items(state.messages) { message ->
                ChatBubbleSuper(message)
            }

            if (state.isAgentTyping) {
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
                value = state.currentMessage,
                onValueChange = { event(ChatSupportEvent.OnMessageChange(it)) },
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
                IconButton(onClick = { event(ChatSupportEvent.SendMessage) }) {
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
private fun SupportBubbleShimmer() {
    val brush = shimmerBrush()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(54.dp)
                .background(brush, RoundedCornerShape(18.dp))
        )
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
        state = ChatSupportUiState(
            messages = listOf(
                SupportMessage("1", "Halo 👋 Selamat datang di Shopme Support.", false, "09:58"),
                SupportMessage("2", "Ada yang bisa kami bantu?", false, "09:58"),
                SupportMessage("3", "Saya mau tanya pesanan", true, "09:59")
            ),
            currentMessage = "",
            isAgentTyping = true
        ),
        event = {}
    )}
