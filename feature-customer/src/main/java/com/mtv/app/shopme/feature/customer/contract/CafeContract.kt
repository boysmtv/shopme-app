/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CafeContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.data.dto.FoodItemModel
import com.mtv.app.shopme.data.dto.OwnerCafeModel
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.CafeResponse
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.based.core.network.utils.Resource

data class CafeStateListener(
    val cafeState: Resource<ApiResponse<CafeResponse>> = Resource.Loading,
    val foodsState: Resource<ApiResponse<List<FoodResponse>>> = Resource.Loading,
    val activeDialog: CafeDialog? = null,
)

data class CafeDataListener(
    val cafe: OwnerCafeModel? = null,
    val foods: List<FoodItemModel> = emptyList()
)

data class CafeEventListener(
    val onFoodClick: (FoodItemModel) -> Unit = {},
    val onDismissActiveDialog: () -> Unit = {}
)

data class CafeNavigationListener(
    val onBack: () -> Unit = {},
    val onNavigateToDetail: (String) -> Unit = {},
    val onNavigateToChat: () -> Unit = {},
    val onNavigateToWhatsapp: () -> Unit = {}
)

sealed class CafeDialog {
    object Error : CafeDialog()
}
