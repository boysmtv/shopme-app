/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HelpContract.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 10.25
 */

package com.mtv.app.shopme.feature.customer.contract

data class HelpStateListener(
    val isLoading: Boolean = false
)

data class HelpDataListener(
    val faq: List<HelpFaq> = emptyList()
)

data class HelpEventListener(
    val onRefresh: () -> Unit = {},
    val onToggleFaq: (faq: HelpFaq) -> Unit = {},
)

data class HelpNavigationListener(
    val onBack: () -> Unit = {},
    val onAbout: () -> Unit = {},
    val onPrivacy: () -> Unit = {},
    val onShipping: () -> Unit = {},
    val onPayment: () -> Unit = {},
    val onContact: () -> Unit = {}
)

data class HelpFaq(
    val question: String,
    val answer: String,
    val expanded: Boolean = false
)