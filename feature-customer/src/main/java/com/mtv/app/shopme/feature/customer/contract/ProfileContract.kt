/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ProfileContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.domain.model.Customer
import com.mtv.based.core.network.utils.LoadState

data class ProfileUiState(
    val customer: LoadState<Customer> = LoadState.Idle,
    val isRefreshing: Boolean = false
)

sealed class ProfileEvent {
    object Load : ProfileEvent()
    object DismissDialog : ProfileEvent()

    object ClickEditProfile : ProfileEvent()
    object ClickOrderHistory : ProfileEvent()
    object ClickFavorites : ProfileEvent()
    object ClickSettings : ProfileEvent()
    object ClickHelpCenter : ProfileEvent()
    data class ClickOrder(val filter: String = "") : ProfileEvent()

    object ClickCheckTncCafe : ProfileEvent()
    object ClickLogout : ProfileEvent()
}

sealed class ProfileEffect {
    object NavigateToEditProfile : ProfileEffect()
    object NavigateToOrderHistory : ProfileEffect()
    object NavigateToFavorites : ProfileEffect()
    object NavigateToSettings : ProfileEffect()
    object NavigateToHelpCenter : ProfileEffect()
    data class NavigateToOrder(val filter: String = "") : ProfileEffect()
    object NavigateToTnc : ProfileEffect()
    object NavigateToSeller : ProfileEffect()
    object NavigateToLogin : ProfileEffect()
}
