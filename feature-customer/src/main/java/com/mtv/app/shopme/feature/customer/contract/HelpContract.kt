/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HelpContract.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 10.25
 */

package com.mtv.app.shopme.feature.customer.contract

data class HelpUiState(
    val faq: List<HelpFaq> = emptyList(),
    val isLoading: Boolean = false
)

sealed class HelpEvent {
    object Load : HelpEvent()
    object DismissDialog : HelpEvent()

    object Refresh : HelpEvent()
    data class ToggleFaq(val faq: HelpFaq) : HelpEvent()

    object ClickBack : HelpEvent()
    object ClickAbout : HelpEvent()
    object ClickPrivacy : HelpEvent()
    object ClickShipping : HelpEvent()
    object ClickPayment : HelpEvent()
    object ClickContact : HelpEvent()
}

sealed class HelpEffect {
    object NavigateBack : HelpEffect()
    object NavigateAbout : HelpEffect()
    object NavigatePrivacy : HelpEffect()
    object NavigateShipping : HelpEffect()
    object NavigatePayment : HelpEffect()
    object NavigateContact : HelpEffect()
}

data class HelpFaq(
    val question: String,
    val answer: String,
    val expanded: Boolean = false
)