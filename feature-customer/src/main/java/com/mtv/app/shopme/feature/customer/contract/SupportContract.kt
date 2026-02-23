/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SupportContract.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 10.38
 */

package com.mtv.app.shopme.feature.customer.contract

data class SupportStateListener(
    val isLoading: Boolean = false
)

data class SupportDataListener(
    val phone: String = "081234567890",
    val email: String = "support@shopme.com",
    val whatsapp: String = "6281234567890"
)

data class SupportEventListener(
    val onOpenWhatsapp: (String) -> Unit = {},
    val onOpenEmail: (String) -> Unit = {},
    val onOpenDial: (String) -> Unit = {}
)

data class SupportNavigationListener(
    val onBack: () -> Unit = {},
    val onLiveChat: () -> Unit = {}
)