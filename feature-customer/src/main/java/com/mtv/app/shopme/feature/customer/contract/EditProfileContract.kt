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
import com.mtv.app.shopme.data.remote.response.VillageResponse
import com.mtv.based.core.network.utils.Resource

data class EditProfileStateListener(
    val customerState: Resource<ApiResponse<CustomerResponse>> = Resource.Loading,
    val customerUpdateState: Resource<ApiResponse<Unit>> = Resource.Loading,
    val addressState: Resource<ApiResponse<List<AddressResponse>>> = Resource.Loading,
    val villageState: Resource<ApiResponse<List<VillageResponse>>> = Resource.Loading,
    val addressAddState: Resource<ApiResponse<Unit>> = Resource.Loading,
    val addressDeleteState: Resource<ApiResponse<Unit>> = Resource.Loading,
    val addressDefaultState: Resource<ApiResponse<Unit>> = Resource.Loading,
    val activeDialog: EditProfileDialog? = null
)

data class EditProfileDataListener(
    val customerData: CustomerResponse? = null,
    val addressData: List<AddressResponse>? = null,
    val villageData: List<VillageResponse>? = null
)

class EditProfileEventListener(
    val onUpdateProfile: (String, String, String) -> Unit = { _, _, _ -> },
    val onAddAddress: (String, String, String, String, String, Boolean) -> Unit = { _, _, _, _, _, _ -> },
    val onDeleteAddress: (String) -> Unit = {},
    val onDefaultAddress: (String) -> Unit = {},
    val onDismissActiveDialog: () -> Unit = {}
)

class EditProfileNavigationListener(
    val onBack: () -> Unit = {}
)

sealed class EditProfileDialog {
    object SuccessUpdateProfile : EditProfileDialog()

}