/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerChatListScreen.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.21
 */

package com.mtv.app.shopme.feature.seller.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.mtv.app.shopme.common.SmartImage
import com.mtv.app.shopme.common.navbar.seller.SellerBottomNavigationBar
import com.mtv.app.shopme.common.shimmerBrush
import com.mtv.app.shopme.feature.seller.contract.SellerChatListEvent
import com.mtv.app.shopme.feature.seller.contract.SellerChatListItem
import com.mtv.app.shopme.feature.seller.contract.SellerChatListUiState

@Composable
fun SellerChatListScreen(
    state: SellerChatListUiState,
    event: (SellerChatListEvent) -> Unit
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
                IconButton(onClick = { event(SellerChatListEvent.ClickBack) }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text("Chats", fontFamily = PoppinsFont, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            }

            IconButton(onClick = { event(SellerChatListEvent.ClickClearAll) }) {
                Icon(Icons.Default.DeleteOutline, contentDescription = null)
            }
        }

        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))

        when {
            state.isLoading && state.chatList.isEmpty() -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 20.dp)
                ) {
                    items(5) {
                        SellerChatListShimmerItem()
                    }
                }
            }

            state.chatList.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Chat belum ada",
                        fontFamily = PoppinsFont,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 20.dp)
                ) {
                    items(state.chatList) { item ->
                        SellerListChatItem(
                            data = item,
                            onClick = { event(SellerChatListEvent.ClickChat(item)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SellerChatListShimmerItem() {
    val brush = shimmerBrush()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F7F7), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(brush)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )
        }
        Spacer(Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(12.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(brush)
        )
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
            modifier = Modifier.size(48.dp),
            base64Image = data.avatarBase64,
            placeholderRes = data.id.toIntOrNull() ?: 0,
        )
        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(data.name, fontFamily = PoppinsFont, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                data.lastMessage,
                fontFamily = PoppinsFont,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                data.time,
                fontFamily = PoppinsFont,
                fontSize = 12.sp,
                color = Color.Gray,
            )

            if (data.unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .background(AppColor.Blue, RoundedCornerShape(50))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        data.unreadCount.toString(),
                        color = Color.White,
                        fontFamily = PoppinsFont,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ChatAvatar(
    modifier: Modifier = Modifier,
    base64Image: String?,
    placeholderRes: Int = 0
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(AppColor.Blue)
    ) {
        val placeholder = painterResource(
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
        )

        SmartImage(
            model = base64Image,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholder = placeholder
        )
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
                state = SellerChatListUiState(
                    chatList = listOf(
                        SellerChatListItem("1", "Cafe Kopi Kita", "Pesanan siap dikirim.", "10:32", 2),
                        SellerChatListItem("2", "Bakery Mantap", "Bisa tambah extra cheese?", "09:45", 0),
                        SellerChatListItem("3", "Warung Sederhana", "Pesanan sudah dibayar.", "Kemarin", 1)
                    )
                ),
                event = {}
            )
        }
    }
}
