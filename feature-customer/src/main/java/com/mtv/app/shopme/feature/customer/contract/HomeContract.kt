/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.domain.model.Customer
import com.mtv.app.shopme.domain.model.Food
import com.mtv.based.core.network.utils.UiError

data class HomeUiState(
    val customer: Customer? = null,
    val foods: List<Food> = emptyList(),

    val isCustomerLoading: Boolean = false,
    val isFoodsLoading: Boolean = false,

    val isCustomerFresh: Boolean = false,
    val isFoodsFresh: Boolean = false
)

sealed class HomeEvent {
    object Load : HomeEvent()
    object Refresh : HomeEvent()
    object DismissError : HomeEvent()

    object ClickSearch : HomeEvent()
    object ClickNotif : HomeEvent()
    data class ClickFood(val id: String) : HomeEvent()
}

sealed class HomeEffect {
    object NavigateToSearch : HomeEffect()
    object NavigateToNotif : HomeEffect()
    data class NavigateToDetail(val id: String) : HomeEffect()
}
