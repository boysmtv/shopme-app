/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerNotificationScreen.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.30
 */

package com.mtv.app.shopme.feature.seller.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.feature.seller.contract.*
import com.mtv.app.shopme.feature.seller.model.SellerNotifItem
import com.mtv.based.core.network.utils.ResourceFirebase
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogCenterV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import com.mtv.based.uicomponent.core.component.toolbar.BaseToolbar
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.OK_STRING
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.WARNING_STRING

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SellerNotificationScreen(
    uiState: SellerNotifState,
    uiData: SellerNotifData,
    uiEvent: SellerNotifEvent,
    uiNavigation: SellerNotifNavigation
) {

    ShowSellerDialog(uiState, uiEvent)

    val isRefreshing = uiState.notificationState is ResourceFirebase.Loading

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { uiEvent.onGetNotification() }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
            .background(Color(0xFFF5F5F5))
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            BaseToolbar(
                title = "Seller Notification",
                showRightIcon = true,
                rightIcon = Icons.Default.DeleteSweep,
                onLeftClick = { uiNavigation.onBack() },
                onRightClick = { uiEvent.onClearNotification() }
            )

            Text(
                text = "Recent Orders",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (uiData.localNotification.isEmpty()) {
                    item { EmptySellerState() }
                } else {
                    items(uiData.localNotification) { item ->
                        SellerNotificationItemCard(
                            item = item,
                            onClick = { uiEvent.onNotificationClicked(item) }
                        )
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun ShowSellerDialog(
    uiState: SellerNotifState,
    uiEvent: SellerNotifEvent
) {
    uiState.activeDialog?.let { dialog ->
        when (dialog) {
            is SellerNotifDialog.Success -> {
                DialogCenterV1(
                    state = DialogStateV1(
                        type = DialogType.SUCCESS,
                        title = "Success",
                        message = "Seller notifications cleared",
                        primaryButtonText = OK_STRING
                    ),
                    onDismiss = { uiEvent.onDismissActiveDialog() }
                )
            }

            is SellerNotifDialog.Error -> {
                DialogCenterV1(
                    state = DialogStateV1(
                        type = DialogType.ERROR,
                        title = WARNING_STRING,
                        message = dialog.message,
                        primaryButtonText = OK_STRING
                    ),
                    onDismiss = { uiEvent.onDismissActiveDialog() }
                )
            }
        }
    }
}

@Composable
private fun EmptySellerState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No seller notification",
            color = Color.DarkGray.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun SellerInitialAvatar(
    text: String,
    size: Dp,
    backgroundColor: Color = AppColor.GreenSoft
) {
    val initial = remember(text) {
        text.trim().firstOrNull()?.uppercase()?.plus(".") ?: ""
    }

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SellerUnreadDot() {
    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(Color(0xFF1E88E5))
    )
}

@Composable
fun SellerNotificationItemCard(
    item: SellerNotifItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {

        SellerInitialAvatar(
            text = item.buyerName,
            size = 44.dp
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = item.date,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )

                if (!item.isRead) {
                    Spacer(modifier = Modifier.width(6.dp))
                    SellerUnreadDot()
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Order #${item.orderId}",
                style = MaterialTheme.typography.labelMedium,
                color = AppColor.Blue
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.message,
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SellerNotificationScreenPreview() {
    val dummyList = List(5) {
        SellerNotifItem(
            title = "New Order Received",
            message = "You have a new order from John Doe",
            orderId = "INV-20260220-00$it",
            buyerName = "John Doe",
            date = "20 Feb 2026",
            time = "15:30",
            isRead = it % 2 == 0
        )
    }

    SellerNotificationScreen(
        uiState = SellerNotifState(),
        uiData = SellerNotifData(
            localNotification = dummyList
        ),
        uiEvent = SellerNotifEvent(
            onNotificationClicked = {},
            onGetNotification = {},
            onClearNotification = {},
            onDismissActiveDialog = {}
        ),
        uiNavigation = SellerNotifNavigation(
            onBack = {}
        )
    )
}