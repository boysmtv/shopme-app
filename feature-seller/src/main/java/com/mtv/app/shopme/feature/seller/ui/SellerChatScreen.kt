/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerChatScreen.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.21
 */

package com.mtv.app.shopme.feature.seller.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SellerChatScreen(
    state: SellerChatDetailUiState,
    event: (SellerChatDetailEvent) -> Unit
) {
    val messages = state.messages
    val messageInput = state.currentMessage
    val listState = rememberLazyListState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = { event(SellerChatDetailEvent.Load) }
    )
    val presenceText = if (state.isPeerOnline) {
        "Customer online"
    } else {
        "Customer offline"
    }
    val presenceColor = if (state.isPeerOnline) Color(0xFF4CAF50) else Color.Gray

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
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
                model = state.chatAvatarUrl,
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
                    presenceText,
                    fontFamily = PoppinsFont,
                    fontSize = 12.sp,
                    color = presenceColor
                )
            }

            Spacer(Modifier.weight(1f))
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))

            if (messages.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Chat belum ada", fontFamily = PoppinsFont, fontSize = 14.sp, color = Color.Gray)
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f).padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(messages) { msg ->
                        SellerChatBubble(
                            msg = msg,
                            onRetry = {
                                event(SellerChatDetailEvent.RetryMessage(msg.id, msg.message))
                            }
                        )
                    }
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
                    if (messageInput.isNotBlank()) {
                        event(SellerChatDetailEvent.SendMessage)
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

        PullRefreshIndicator(
            refreshing = state.isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun SellerChatBubble(
    msg: SellerChatDetailMessage,
    onRetry: () -> Unit = {}
) {
    val bubbleColor = if (msg.isFromSeller) AppColor.Blue else AppColor.BlueSoft
    val textColor = if (msg.isFromSeller) Color.White else Color.Black
    val arrangement = if (msg.isFromSeller) Arrangement.End else Arrangement.Start

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = arrangement) {
        Box(
            modifier = Modifier
                .background(bubbleColor, RoundedCornerShape(16.dp))
                .padding(start = 12.dp, end = 10.dp, top = 10.dp, bottom = 8.dp)
                .widthIn(max = 250.dp)
        ) {
            Column {
                Text(msg.message, fontFamily = PoppinsFont, fontSize = 14.sp, color = textColor)
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = msg.time.ifBlank { "Baru saja" },
                        fontFamily = PoppinsFont,
                        fontSize = 10.sp,
                        color = textColor.copy(alpha = 0.72f)
                    )
                    if (msg.isFromSeller) {
                        if (msg.isFailed) {
                            Row(
                                modifier = Modifier.clickable { onRetry() },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Retry",
                                    tint = Color(0xFFFFCDD2),
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "Kirim ulang",
                                    color = Color(0xFFFFCDD2),
                                    fontFamily = PoppinsFont,
                                    fontSize = 10.sp
                                )
                            }
                        } else {
                            SellerChatDeliveryCheck(
                                isPending = msg.isPending,
                                isRead = msg.isRead
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SellerChatDeliveryCheck(
    isPending: Boolean,
    isRead: Boolean
) {
    val icon = if (isPending) Icons.Default.Done else Icons.Default.DoneAll
    val tint = if (isRead && !isPending) Color(0xFF34B7F1) else Color(0xFFE0E0E0)
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = tint,
        modifier = Modifier.size(14.dp)
    )
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
