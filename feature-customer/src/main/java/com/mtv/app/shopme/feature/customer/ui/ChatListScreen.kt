/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ListChatScreen.kt
 *
 * Last modified by Dedy Wijaya on 13/02/26 13.35
 */

package com.mtv.app.shopme.feature.customer.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
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
import com.mtv.app.shopme.common.base64ToBitmap
import com.mtv.app.shopme.data.ChatListItem
import com.mtv.app.shopme.feature.customer.contract.ChatListDataListener
import com.mtv.app.shopme.feature.customer.contract.ChatListEventListener
import com.mtv.app.shopme.feature.customer.contract.ChatListNavigationListener
import com.mtv.app.shopme.feature.customer.contract.ChatListStateListener
import com.mtv.app.shopme.feature.customer.presentation.mockChatList
import com.mtv.app.shopme.nav.CustomerBottomNavigationBar
import com.mtv.app.shopme.common.R

@Composable
fun ChatListScreen(
    uiState: ChatListStateListener,
    uiData: ChatListDataListener,
    uiEvent: ChatListEventListener,
    uiNavigation: ChatListNavigationListener
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.White)
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = uiNavigation.onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "Chats",
                    fontFamily = PoppinsFont,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            IconButton(onClick = uiNavigation.onBack) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(start = 20.dp, end = 20.dp)
        ) {
            items(uiState.chatList) { item ->
                ListChatItem(
                    data = item,
                    onClick = { uiNavigation.navigateToChat(item.id) }
                )
            }
        }
    }
}

@Composable
fun ListChatItem(
    data: ChatListItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(AppColor.WhiteSoft, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ChatAvatar(
            base64Image = data.avatarBase64,
            placeholderRes = data.id.toInt(),
            modifier = Modifier.size(48.dp)
        )

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                data.name,
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
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
                .fillMaxHeight(),     // sekarang bekerja
            horizontalAlignment = Alignment.End
        ) {
            Text(
                data.time,
                fontFamily = PoppinsFont,
                fontSize = 12.sp,
                color = Color.Gray,
            )

            Spacer(modifier = Modifier.weight(1f))

            if (data.unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .background(AppColor.Orange, RoundedCornerShape(50))
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
    base64Image: String?,
    placeholderRes: Int,
    modifier: Modifier = Modifier
) {
    val bitmap = remember(base64Image) {
        base64Image
            ?.takeIf { it.isNotBlank() }
            ?.let { base64ToBitmap(it) }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(AppColor.Orange)
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            val imageRes = when (placeholderRes) {
                0 -> R.drawable.image_burger
                1 -> R.drawable.image_pizza
                2 -> R.drawable.image_platbread
                3 -> R.drawable.image_cheese_burger
                4 -> R.drawable.image_bakso
                5 -> R.drawable.image_pempek
                6 -> R.drawable.image_padang
                7 -> R.drawable.image_sate
                else -> R.drawable.image_burger
            }

            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun ChatListScreenPreview() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            CustomerBottomNavigationBar(navController)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(bottom = padding.calculateBottomPadding())
                .fillMaxSize()
                .background(Color.White)
        ) {
            ChatListScreen(
                uiState = ChatListStateListener(
                    chatList = mockChatList()
                ),
                uiData = ChatListDataListener(),
                uiEvent = ChatListEventListener(),
                uiNavigation = ChatListNavigationListener()
            )
        }
    }
}