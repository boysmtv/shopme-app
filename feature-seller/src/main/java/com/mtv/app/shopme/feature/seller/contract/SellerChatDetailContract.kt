/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerChatDetailContract.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.20
 */

package com.mtv.app.shopme.feature.seller.contract

import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

data class SellerChatDetailStateListener(
    val messages: List<SellerChatDetailMessage> = emptyList()
)

data class SellerChatDetailDataListener(
    val unused: String = EMPTY_STRING
)

class SellerChatDetailEventListener(
    val onSendMessage: ((String) -> Unit)? = null
)

class SellerChatDetailNavigationListener(
    val onBack: () -> Unit = {}
)

data class SellerChatDetailMessage(
    val message: String,
    val isFromSeller: Boolean
)