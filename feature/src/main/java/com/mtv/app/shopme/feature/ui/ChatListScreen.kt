/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: ListChatScreen.kt
 *
 * Last modified by Dedy Wijaya on 13/02/26 13.35
 */

package com.mtv.app.shopme.feature.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.data.ChatListItem
import com.mtv.app.shopme.feature.contract.ChatListDataListener
import com.mtv.app.shopme.feature.contract.ChatListEventListener
import com.mtv.app.shopme.feature.contract.ChatListNavigationListener
import com.mtv.app.shopme.feature.contract.ChatListStateListener
import com.mtv.app.shopme.feature.contract.HomeDataListener
import com.mtv.app.shopme.feature.contract.HomeEventListener
import com.mtv.app.shopme.feature.contract.HomeNavigationListener
import com.mtv.app.shopme.feature.contract.HomeStateListener
import com.mtv.app.shopme.feature.presentation.mockChatList
import com.mtv.app.shopme.nav.BottomNavigationBar

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
            .padding(top = 32.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
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
                    "Chats",
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
            .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(AppColor.Orange, RoundedCornerShape(50.dp))
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

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun ChatListScreenPreview() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
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