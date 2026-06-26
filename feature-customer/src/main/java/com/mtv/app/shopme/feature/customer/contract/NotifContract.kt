/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotifContract.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 14.56
 */

package com.mtv.app.shopme.feature.customer.contract

import androidx.compose.runtime.Immutable
import com.mtv.app.shopme.domain.model.NotificationItem
import com.mtv.based.core.network.utils.LoadState

@Immutable
data class NotifUiState(
    val localNotification: List<NotificationItem> = emptyList(),
    val notificationState: LoadState<String> = LoadState.Loading,
    val page: Int = 0,
    val isLastPage: Boolean = false,
    val isLoadingMore: Boolean = false,
    val activeDialog: NotifDialog? = null
)

sealed class NotifEvent {
    object Load : NotifEvent()
    object DismissDialog : NotifEvent()

    object GetNotification : NotifEvent()
    object LoadMore : NotifEvent()
    object ClearNotification : NotifEvent()

    data class ClickNotification(val item: NotificationItem) : NotifEvent()

    object ClickBack : NotifEvent()
}

sealed class NotifEffect {
    object NavigateBack : NotifEffect()
    data class NavigateToOrderDetail(val orderId: String) : NotifEffect()
}

sealed class NotifDialog {
    data class Error(
        val message: String
    ) : NotifDialog()

    object Success : NotifDialog()
}
