/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerChatScreen.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.21
 */

package com.mtv.app.shopme.feature.seller.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerChatListItem
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailMessage
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailStateListener


@Composable
fun SellerChatScreen(
    uiState: SellerChatDetailStateListener,
    uiData: SellerChatDetailDataListener,
    uiEvent: SellerChatDetailEventListener,
    uiNavigation: SellerChatDetailNavigationListener,
) {
    var messageInput by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<SellerChatDetailMessage>().apply { addAll(uiState.messages) } }

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
            IconButton(onClick = { uiNavigation.onBack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            Spacer(Modifier.width(12.dp))
            Column {
                Text("Customer Name", fontFamily = PoppinsFont, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Online", fontFamily = PoppinsFont, fontSize = 12.sp, color = Color(0xFF4CAF50))
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
                onValueChange = { messageInput = it },
                modifier = Modifier.weight(1f).heightIn(min = 48.dp, max = 150.dp),
                placeholder = { Text("Tulis pesan...", fontFamily = PoppinsFont, fontSize = 14.sp) },
                shape = RoundedCornerShape(16.dp),
                maxLines = 4
            )

            Spacer(Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (messageInput.isNotBlank()) {
                        messages.add(SellerChatDetailMessage(messageInput, true))
                        uiEvent.onSendMessage?.invoke(messageInput)
                        messageInput = ""
                    }
                },
                modifier = Modifier
                    .height(48.dp)
                    .width(64.dp)
                    .background(AppColor.Blue, RoundedCornerShape(20.dp))
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White)
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
        uiState = SellerChatDetailStateListener(messages = mockSellerChatMessages()),
        uiData = SellerChatDetailDataListener(),
        uiEvent = SellerChatDetailEventListener(),
        uiNavigation = SellerChatDetailNavigationListener()
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
