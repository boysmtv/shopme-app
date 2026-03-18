/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SearchContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.app.shopme.data.remote.response.PageResponse
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

data class SearchStateListener(
    val searchFoodState: Resource<ApiResponse<PageResponse<FoodResponse>>> = Resource.Loading,
)

data class SearchDataListener(
    val query: String = EMPTY_STRING,
    val results: List<FoodResponse> = emptyList()
)

data class SearchEventListener(
    val onQueryChanged: (String) -> Unit = {},
    val onLoadNextPage: () -> Unit = {}
)

data class SearchNavigationListener(
    val onDetailClick: (String) -> Unit = {},
    val onBack: () -> Unit = {},
)

