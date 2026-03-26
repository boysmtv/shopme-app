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
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.UiError

data class HomeUiState(
    val customer: LoadState<Customer> = LoadState.Idle,
    val foods: LoadState<List<Food>> = LoadState.Idle
)

sealed class HomeEvent {
    object Load : HomeEvent()
    object DismissDialog : HomeEvent()
    object ClickSearch : HomeEvent()
    object ClickNotif : HomeEvent()
    data class ClickFood(val id: String) : HomeEvent()
}

sealed class HomeEffect {
    object NavigateToSearch : HomeEffect()
    object NavigateToNotif : HomeEffect()
    data class NavigateToDetail(val id: String) : HomeEffect()
}