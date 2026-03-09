/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: EditProfileContract.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 14.02
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.AddressResponse
import com.mtv.app.shopme.data.remote.response.CustomerResponse
import com.mtv.based.core.network.utils.Resource

data class EditProfileStateListener(
    val customerState: Resource<ApiResponse<CustomerResponse>> = Resource.Loading,
    val addressState: Resource<ApiResponse<List<AddressResponse>>> = Resource.Loading,
    val addressAddState: Resource<ApiResponse<Unit>> = Resource.Loading,
    val addressDeleteState: Resource<ApiResponse<Unit>> = Resource.Loading,
    val addressDefaultState: Resource<ApiResponse<Unit>> = Resource.Loading,
)

data class EditProfileDataListener(
    val customerData: CustomerResponse? = null,
    val addressData: List<AddressResponse>? = null
)

class EditProfileEventListener(
    val onAddAddress: (String, String, String, String, String, Boolean) -> Unit = { _, _, _, _, _, _ -> },
    val onDeleteAddress: (String) -> Unit = {},
    val onDefaultAddress: (String) -> Unit = {},
)

class EditProfileNavigationListener(
    val onBack: () -> Unit = {}
)