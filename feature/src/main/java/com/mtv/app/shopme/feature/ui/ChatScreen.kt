/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: ChatScreen.kt
 *
 * Last modified by ChatGPT on 12/02/26
 */

package com.mtv.app.shopme.feature.ui

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.feature.contract.ChatDataListener
import com.mtv.app.shopme.feature.contract.ChatEventListener
import com.mtv.app.shopme.feature.contract.ChatNavigationListener
import com.mtv.app.shopme.feature.contract.ChatStateListener
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

data class ChatMessage(
    val message: String,
    val isFromUser: Boolean
)

@Composable
fun ChatScreen(
    uiState: ChatStateListener,
    uiData: ChatDataListener,
    uiEvent: ChatEventListener,
    uiNavigation: ChatNavigationListener
) {
    var userMessage by remember { mutableStateOf(EMPTY_STRING) }
    val messages = remember {
        mutableStateListOf(
            ChatMessage("Halo! Ada yang bisa saya bantu?", false),
            ChatMessage("Saya ingin memesan kopi latte, ada?", true),
            ChatMessage("Ada dong! Mau ukuran regular atau large?", false),
            ChatMessage("Halo! Ada yang bisa saya bantu?", false),
            ChatMessage("Saya ingin memesan kopi latte, ada?", true),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 32.dp)
    ) {
        // --- AppBar ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { uiNavigation.onBack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            Column {
                Text(
                    "Cafe Kopi Kita",
                    fontFamily = PoppinsFont,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Online",
                    fontFamily = PoppinsFont,
                    fontSize = 12.sp,
                    color = Color(0xFF4CAF50)
                )
            }

        }

        HorizontalDivider(modifier = Modifier.height(1.dp))

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                ChatBubble(message = msg)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = userMessage,
                onValueChange = { userMessage = it },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp, max = 150.dp),
                placeholder = {
                    Text(
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        text = "Tulis pesan... ",
                        fontSize = 14.sp,
                        fontFamily = PoppinsFont
                    )
                },
                shape = RoundedCornerShape(16.dp),
                maxLines = 4
            )

            Spacer(Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (userMessage.isNotEmpty()) {
                        messages.add(ChatMessage(userMessage, true))
                        userMessage = ""
                    }
                },
                modifier = Modifier
                    .height(48.dp)
                    .width(64.dp)
                    .align(Alignment.Bottom)
                    .background(AppColor.Orange, RoundedCornerShape(20.dp))
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = AppColor.White
                )
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val bubbleColor = if (message.isFromUser) AppColor.Orange else AppColor.LightOrange
    val alignment = if (message.isFromUser) Arrangement.End else Arrangement.Start
    val textColor = if (message.isFromUser) AppColor.White else Color.Black

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = alignment
    ) {
        Box(
            modifier = Modifier
                .background(bubbleColor, RoundedCornerShape(16.dp))
                .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp)
                .widthIn(max = 250.dp)
        ) {
            Text(
                text = message.message,
                color = textColor,
                fontFamily = PoppinsFont,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun ChatScreenPreview() {
    ChatScreen(
        uiState = ChatStateListener(),
        uiData = ChatDataListener(),
        uiEvent = ChatEventListener(),
        uiNavigation = ChatNavigationListener()
    )
}
