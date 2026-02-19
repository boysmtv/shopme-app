/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerChatListScreen.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.21
 */

package com.mtv.app.shopme.feature.seller.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.base64ToBitmap
import com.mtv.app.shopme.feature.seller.contract.SellerChatListDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerChatListEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerChatListItem
import com.mtv.app.shopme.feature.seller.contract.SellerChatListNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerChatListStateListener
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerOrderEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerOrderNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerOrderStateListener
import com.mtv.app.shopme.nav.SellerBottomNavigationBar

@Composable
fun SellerChatListScreen(
    uiState: SellerChatListStateListener,
    uiData: SellerChatListDataListener,
    uiEvent: SellerChatListEventListener,
    uiNavigation: SellerChatListNavigationListener
) {
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = uiNavigation.onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text("Chats", fontFamily = PoppinsFont, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            }

            IconButton(onClick = { /* could clear all chats */ }) {
                Icon(Icons.Default.DeleteOutline, contentDescription = null)
            }
        }

        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            items(uiState.chatList) { item ->
                SellerListChatItem(
                    data = item,
                    onClick = { uiNavigation.navigateToChat() }
                )
            }
        }
    }
}

@Composable
fun SellerListChatItem(
    data: SellerChatListItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F7F7), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ChatAvatar(
            base64Image = data.avatarBase64,
            placeholderRes = data.id.toIntOrNull() ?: 0,
            modifier = Modifier.size(48.dp)
        )
        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(data.name, fontFamily = PoppinsFont, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(4.dp))
            Text(data.lastMessage, fontFamily = PoppinsFont, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, color = Color.Gray)
        }

        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.End
        ) {
            Text(data.time, fontFamily = PoppinsFont, fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.weight(1f))
            if (data.unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .background(AppColor.Orange, RoundedCornerShape(50))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(data.unreadCount.toString(), color = Color.White, fontFamily = PoppinsFont, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun ChatAvatar(
    base64Image: String?,
    placeholderRes: Int = 0,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(AppColor.Orange)
    ) {
        if (!base64Image.isNullOrBlank()) {
            val bitmap = remember(base64Image) { base64ToBitmap(base64Image) }
            bitmap?.let {
                androidx.compose.foundation.Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }
        } else {
            // Placeholder image dari resource (sesuaikan project)
            androidx.compose.foundation.Image(
                painter = painterResource(
                    id = when (placeholderRes) {
                        0 -> com.mtv.app.shopme.common.R.drawable.image_burger
                        1 -> com.mtv.app.shopme.common.R.drawable.image_pizza
                        2 -> com.mtv.app.shopme.common.R.drawable.image_platbread
                        3 -> com.mtv.app.shopme.common.R.drawable.image_cheese_burger
                        4 -> com.mtv.app.shopme.common.R.drawable.image_bakso
                        5 -> com.mtv.app.shopme.common.R.drawable.image_pempek
                        6 -> com.mtv.app.shopme.common.R.drawable.image_padang
                        7 -> com.mtv.app.shopme.common.R.drawable.image_sate
                        else -> com.mtv.app.shopme.common.R.drawable.image_burger
                    }
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SellerChatListPreview() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { SellerBottomNavigationBar(navController) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
        ) {
            SellerChatListScreen(
                uiState = SellerChatListStateListener(chatList = mockSellerChatList()),
                uiData = SellerChatListDataListener(),
                uiEvent = SellerChatListEventListener({}),
                uiNavigation = SellerChatListNavigationListener()
            )
        }
    }
}