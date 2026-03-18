/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ProfileContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.CustomerResponse
import com.mtv.based.core.network.utils.Resource

data class ProfileStateListener(
    val customerState: Resource<ApiResponse<CustomerResponse>> = Resource.Loading,
    val activeDialog: ProfileDialog? = null
)

data class ProfileDataListener(
    val customerData: CustomerResponse? = null,
)

data class ProfileEventListener(
    val onDismissDialog: () -> Unit = {},
    val onCheckTncCafe: () -> Unit = {},
)

data class ProfileNavigationListener(
    val onEditProfile: () -> Unit = {},
    val onOrderHistory: () -> Unit = {},
    val onSettings: () -> Unit = {},
    val onHelpCenter: () -> Unit = {},
    val onOrder: () -> Unit = {},
    val onNavigateToTnc: () -> Unit = {},
    val onNavigateToSeller: () -> Unit = {}
)

sealed class ProfileDialog {
    object LogoutConfirm : ProfileDialog()
}
