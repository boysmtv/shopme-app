/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: ChatContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.contract

import com.mtv.based.core.network.utils.ResourceFirebase

data class ChatStateListener(
    val emptyState: ResourceFirebase<Unit> = ResourceFirebase.Loading,
    val activeDialog: ChatDialog? = null
)

data class ChatDataListener(
    val emptyData: String? = null
)

data class ChatEventListener(
    val onDismissActiveDialog: () -> Unit = {}
)

data class ChatNavigationListener(
    val onBack: () -> Unit = {}
)

sealed class ChatDialog {
    object Success : ChatDialog()
}
