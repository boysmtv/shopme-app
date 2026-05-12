/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerChatScreen.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.21
 */

package com.mtv.app.shopme.feature.seller.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.common.SmartImage
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailEvent
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailMessage
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailUiState
import com.mtv.app.shopme.feature.seller.contract.SellerChatListItem
import com.mtv.based.uicomponent.core.component.loading.LoadingV1
import com.mtv.based.uicomponent.core.component.loading.LoadingV2

@Composable
fun SellerChatScreen(
    state: SellerChatDetailUiState,
    event: (SellerChatDetailEvent) -> Unit
) {
    val messages = state.messages
    val messageInput = state.currentMessage

    if (state.isLoading && messages.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingV2()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .statusBarsPadding()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { event(SellerChatDetailEvent.ClickBack) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            Spacer(Modifier.width(12.dp))
            SmartImage(
                model = state.chatAvatarBase64,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.image_burger),
                error = painterResource(R.drawable.image_burger)
            )

            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    state.chatName.ifBlank { "Customer" },
                    fontFamily = PoppinsFont,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Percakapan aktif",
                    fontFamily = PoppinsFont,
                    fontSize = 12.sp,
                    color = Color(0xFF4CAF50)
                )
            }

            Spacer(Modifier.weight(1f))
        }

        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                SellerChatBubble(msg)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = messageInput,
                onValueChange = { event(SellerChatDetailEvent.ChangeMessage(it)) },
                modifier = Modifier.weight(1f).heightIn(min = 48.dp, max = 150.dp),
                placeholder = { Text("Tulis pesan...", fontFamily = PoppinsFont, fontSize = 14.sp) },
                shape = RoundedCornerShape(16.dp),
                maxLines = 4
            )

            Spacer(Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (messageInput.isNotBlank() && !state.isSending) {
                        event(SellerChatDetailEvent.SendMessage)
                    }
                },
                modifier = Modifier
                    .height(48.dp)
                    .width(64.dp)
                    .background(AppColor.Blue, RoundedCornerShape(20.dp))
            ) {
                if (state.isSending) {
                    LoadingV1(modifier = Modifier.size(20.dp))
                } else {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun SellerChatBubble(msg: SellerChatDetailMessage) {
    val bubbleColor = if (msg.isFromSeller) AppColor.Blue else AppColor.BlueSoft
    val textColor = if (msg.isFromSeller) Color.White else Color.Black
    val arrangement = if (msg.isFromSeller) Arrangement.End else Arrangement.Start

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = arrangement) {
        Box(
            modifier = Modifier
                .background(bubbleColor, RoundedCornerShape(16.dp))
                .padding(12.dp)
                .widthIn(max = 250.dp)
        ) {
            Text(msg.message, fontFamily = PoppinsFont, fontSize = 14.sp, color = textColor)
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SellerChatDetailPreview() {
    SellerChatScreen(
        state = SellerChatDetailUiState(
            messages = mockSellerChatMessages()
        ),
        event = {}
    )
}

fun mockSellerChatList(): List<SellerChatListItem> = listOf(
    SellerChatListItem(
        id = "1",
        name = "Dedy Wijaya",
        lastMessage = "Terima kasih pesanan sudah sampai",
        time = "10:32",
        unreadCount = 2
    ),
    SellerChatListItem(
        id = "2",
        name = "Rina Sari",
        lastMessage = "Bisa tambah extra cheese?",
        time = "09:45",
        unreadCount = 0
    ),
    SellerChatListItem(
        id = "3",
        name = "Andi Saputra",
        lastMessage = "Pesanan sudah dibayar",
        time = "Kemarin",
        unreadCount = 1
    )
)

fun mockSellerChatMessages(): List<SellerChatDetailMessage> = listOf(
    SellerChatDetailMessage("Halo kak! Ada yang bisa kami bantu?", true),
    SellerChatDetailMessage("Saya mau pesan nasi goreng.", false),
    SellerChatDetailMessage("Siap kak, mau level pedasnya bagaimana?", true),
    SellerChatDetailMessage("Sedang saja.", false),
    SellerChatDetailMessage("Oke 😊 Pesanan akan segera dikirim.", true)
)
