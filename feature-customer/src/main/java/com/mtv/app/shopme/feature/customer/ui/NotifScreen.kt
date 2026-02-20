/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotifScreen.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 15.16
 */

package com.mtv.app.shopme.feature.customer.ui

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.feature.customer.contract.NotifDataListener
import com.mtv.app.shopme.feature.customer.contract.NotifDialog
import com.mtv.app.shopme.feature.customer.contract.NotifEventListener
import com.mtv.app.shopme.feature.customer.contract.NotifNavigationListener
import com.mtv.app.shopme.feature.customer.contract.NotifStateListener
import com.mtv.app.shopme.feature.customer.firebase.NotifItem
import com.mtv.app.shopme.feature.customer.presentation.previewNotification
import com.mtv.based.core.network.utils.ResourceFirebase
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogCenterV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import com.mtv.based.uicomponent.core.component.toolbar.BaseToolbar
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.OK_STRING
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.WARNING_STRING

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NotificationScreen(
    uiState: NotifStateListener,
    uiData: NotifDataListener,
    uiEvent: NotifEventListener,
    uiNavigation: NotifNavigationListener
) {
    ShowNotificationDialog(uiState, uiEvent)

    val isRefreshing = uiState.notificationState is ResourceFirebase.Loading

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { uiEvent.onGetNotification() }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
            .background(Color(0xFFF2F2F2))
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            BaseToolbar(
                title = "Notification",
                showRightIcon = true,
                rightIcon = Icons.Default.DeleteSweep,
                onLeftClick = { uiNavigation.onBack() },
                onRightClick = { uiEvent.onClearNotification() }
            )

            Text(
                text = "Previously",
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
                    item {
                        EmptyNotificationState()
                    }
                } else {
                    items(uiData.localNotification) { item ->
                        NotificationItemCard(
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
fun NotificationItemCard(
    item: NotifItem,
    onClick: () -> Unit = {}
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

        InitialAvatar(text = item.signatureName, size = 44.dp)

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
                    text = item.signatureDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.width(6.dp))

                UnreadDot()
            }

            Spacer(modifier = Modifier.height(6.dp))

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

@Composable
private fun EmptyNotificationState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No notification",
            color = Color.DarkGray.copy(alpha = 0.7f)
        )
    }
}


@Composable
private fun ShowNotificationDialog(
    uiState: NotifStateListener,
    uiEvent: NotifEventListener
) {
    uiState.activeDialog?.let { dialog ->
        when (dialog) {
            is NotifDialog.Success -> {
                DialogCenterV1(
                    state = DialogStateV1(
                        type = DialogType.SUCCESS,
                        title = stringResource(R.string.success),
                        message = stringResource(R.string.success_clear_notif),
                        primaryButtonText = OK_STRING
                    ),
                    onDismiss = { uiEvent.onDismissActiveDialog() }
                )
            }

            is NotifDialog.Error -> {
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
fun UnreadDot() {
    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(Color(0xFF1E88E5))
    )
}

@Composable
fun InitialAvatar(
    text: String,
    size: Dp = 44.dp,
    backgroundColor: Color = AppColor.LightOrange,
    textColor: Color = Color.Black
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
            color = textColor,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun NotificationItemPreview() {
    NotificationItemCard(item = previewNotification)
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun NotificationScreenPreview() {
    NotificationScreen(
        uiState = NotifStateListener(),
        uiData = NotifDataListener(
            localNotification = List(9) { previewNotification }
        ),
        uiEvent = NotifEventListener({}, {}, {}, {}),
        uiNavigation = NotifNavigationListener {}
    )
}