/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: DetailContract.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 08.59
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.request.CartVariantRequest
import com.mtv.app.shopme.data.remote.response.CustomerResponse
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.based.core.network.utils.Resource

data class DetailStateListener(
    val foodState: Resource<ApiResponse<FoodResponse>> = Resource.Loading,
    val foodSimilarState: Resource<ApiResponse<List<FoodResponse>>> = Resource.Loading,
    val foodAddToCartState: Resource<ApiResponse<Unit>> = Resource.Loading,
    val activeDialog: DetailDialog? = null
)

data class DetailDataListener(
    val itemId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val price: Double? = null,
    val imageUrl: String? = null,
    val customerData: CustomerResponse? = null,
    val foodData: FoodResponse? = null,
    val foodSimilarData: List<FoodResponse>? = null
)

data class DetailEventListener(
    val onGetFoodDetail: (String) -> Unit = {},
    val onAddToCart: (String, List<CartVariantRequest>, Int, String) -> Unit = { _, _, _, _ -> },
    val onDismissActiveDialog: () -> Unit = {}
)

data class DetailNavigationListener(
    val onBack: () -> Unit = {},
    val onChatClick: () -> Unit = {},
    val onAddToCart: () -> Unit = {},
    val onClickCafe: (String) -> Unit = {}
)

sealed class DetailDialog {
    object AddedToCart : DetailDialog()
    object Error : DetailDialog()
}
