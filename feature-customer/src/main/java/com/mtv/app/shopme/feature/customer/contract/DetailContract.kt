/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: DetailContract.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 08.59
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.based.core.network.utils.ResourceFirebase

data class DetailStateListener(
    val loadingState: ResourceFirebase<Unit> = ResourceFirebase.Loading,
    val activeDialog: com.mtv.app.shopme.feature.customer.contract.DetailDialog? = null
)

data class DetailDataListener(
    val itemId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val price: Double? = null,
    val imageUrl: String? = null
)

data class DetailEventListener(
    val onDismissActiveDialog: () -> Unit = {}
)

data class DetailNavigationListener(
    val onBack: () -> Unit = {},
    val onChatClick: () -> Unit = {},
    val onAddToCart: () -> Unit = {},
    val onClickCafe: () -> Unit = {}
)

sealed class DetailDialog {
    object AddedToCart : DetailDialog()
    object Error : DetailDialog()
}
