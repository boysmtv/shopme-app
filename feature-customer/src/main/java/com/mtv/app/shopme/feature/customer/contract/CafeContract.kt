/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CafeContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.domain.model.Cafe
import com.mtv.app.shopme.domain.model.Food
import com.mtv.based.core.network.utils.LoadState

data class CafeUiState(
    val cafe: LoadState<Cafe> = LoadState.Idle,
    val foods: LoadState<List<Food>> = LoadState.Idle
)

sealed class CafeEvent {
    object Load : CafeEvent()
    object DismissDialog : CafeEvent()

    data class ClickFood(val id: String) : CafeEvent()
    object ClickBack : CafeEvent()
    object ClickChat : CafeEvent()
    object ClickWhatsapp : CafeEvent()
    object ClickSearch : CafeEvent()
}

sealed class CafeEffect {
    object NavigateBack : CafeEffect()
    object NavigateToChat : CafeEffect()
    object NavigateToSearch : CafeEffect()
    data class OpenWhatsapp(val phone: String) : CafeEffect()
    data class NavigateToDetail(val id: String) : CafeEffect()
}
