/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerNotifContract.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.26
 */

package com.mtv.app.shopme.feature.seller.contract

import androidx.compose.runtime.Immutable
import com.mtv.app.shopme.domain.model.SellerNotifItem
import com.mtv.based.core.network.utils.ResourceFirebase
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.ERROR_STRING

@Immutable
data class SellerNotifUiState(
    val isLoading: Boolean = false,

    val notifications: List<SellerNotifItem> = emptyList(),

    val notificationState: ResourceFirebase<String> = ResourceFirebase.Loading,
    val page: Int = 0,
    val isLastPage: Boolean = false,
    val isLoadingMore: Boolean = false,
    val activeDialog: SellerNotifDialog? = null
)

sealed class SellerNotifEvent {
    object Load : SellerNotifEvent()
    object DismissDialog : SellerNotifEvent()

    object GetNotification : SellerNotifEvent()
    object LoadMore : SellerNotifEvent()
    object ClearNotification : SellerNotifEvent()

    data class ClickNotification(val item: SellerNotifItem) : SellerNotifEvent()

    object ClickBack : SellerNotifEvent()
}

sealed class SellerNotifEffect {
    object NavigateBack : SellerNotifEffect()
    data class NavigateToOrderDetail(val orderId: String) : SellerNotifEffect()
}

sealed class SellerNotifDialog {
    data class Error(
        val message: String = ERROR_STRING
    ) : SellerNotifDialog()

    object Success : SellerNotifDialog()
}
