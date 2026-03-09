/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.CustomerResponse
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.based.core.network.utils.Resource

data class HomeStateListener(
    val customerState: Resource<ApiResponse<CustomerResponse>> = Resource.Loading,
    val foodState: Resource<ApiResponse<List<FoodResponse>>> = Resource.Loading,
    val activeDialog: HomeDialog? = null
)

data class HomeDataListener(
    val customerData: CustomerResponse? = null,
    val foodData: List<FoodResponse>? = null
)

data class HomeEventListener(
    val onDismissActiveDialog: () -> Unit
)

data class HomeNavigationListener(
    val onNavigateToDetail: (String) -> Unit = {},
    val onNavigateToSearch: () -> Unit = {},
    val onNavigateToNotif: () -> Unit = {},
)

sealed class HomeDialog {
    object Success : HomeDialog()
}
