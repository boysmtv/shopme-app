/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: EditProfileContract.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 14.02
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.domain.model.Address
import com.mtv.app.shopme.domain.model.Customer
import com.mtv.app.shopme.domain.model.Village
import com.mtv.based.core.network.utils.LoadState

data class EditProfileUiState(
    val customer: LoadState<Customer> = LoadState.Idle,
    val addresses: LoadState<List<Address>> = LoadState.Idle,
    val villages: LoadState<List<Village>> = LoadState.Idle,

    val updateProfile: LoadState<Unit> = LoadState.Idle,
    val addAddress: LoadState<Unit> = LoadState.Idle,
    val deleteAddress: LoadState<Unit> = LoadState.Idle,
    val setDefaultAddress: LoadState<Unit> = LoadState.Idle,

    val activeDialog: EditProfileDialog? = null
)

sealed class EditProfileEvent {
    object Load : EditProfileEvent()
    object DismissDialog : EditProfileEvent()
    object DismissActiveDialog : EditProfileEvent()

    data class UpdateProfile(
        val name: String,
        val phone: String,
        val photo: String
    ) : EditProfileEvent()

    data class AddAddress(
        val villageId: String,
        val block: String,
        val number: String,
        val rt: String,
        val rw: String,
        val isDefault: Boolean
    ) : EditProfileEvent()

    data class DeleteAddress(val id: String) : EditProfileEvent()
    data class SetDefaultAddress(val id: String) : EditProfileEvent()

    object ClickBack : EditProfileEvent()
}

sealed class EditProfileEffect {
    object NavigateBack : EditProfileEffect()
}

sealed class EditProfileDialog {
    object SuccessUpdateProfile : EditProfileDialog()
}
