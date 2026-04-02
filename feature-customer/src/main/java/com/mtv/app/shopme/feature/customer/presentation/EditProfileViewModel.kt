/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: EditProfileViewModel.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 14.04
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.param.AddressAddParam
import com.mtv.app.shopme.domain.param.AddressDefaultParam
import com.mtv.app.shopme.domain.param.AddressDeleteParam
import com.mtv.app.shopme.domain.param.CustomerUpdateParam
import com.mtv.app.shopme.domain.usecase.CreateAddressUseCase
import com.mtv.app.shopme.domain.usecase.UpdateAddressDefaultUseCase
import com.mtv.app.shopme.domain.usecase.DeleteAddressUseCase
import com.mtv.app.shopme.domain.usecase.GetAddressUseCase
import com.mtv.app.shopme.domain.usecase.UpdateCustomerUseCase
import com.mtv.app.shopme.domain.usecase.GetCustomerUseCase
import com.mtv.app.shopme.domain.usecase.GetVillageUseCase
import com.mtv.app.shopme.feature.customer.contract.EditProfileDialog
import com.mtv.app.shopme.feature.customer.contract.EditProfileEffect
import com.mtv.app.shopme.feature.customer.contract.EditProfileEvent
import com.mtv.app.shopme.feature.customer.contract.EditProfileUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val customerUseCase: GetCustomerUseCase,
    private val customerUpdateUseCase: UpdateCustomerUseCase,
    private val addressUseCase: GetAddressUseCase,
    private val addressAddUseCase: CreateAddressUseCase,
    private val addressDeleteUseCase: DeleteAddressUseCase,
    private val addressDefaultUseCase: UpdateAddressDefaultUseCase,
    private val villageUseCase: GetVillageUseCase,
) : BaseEventViewModel<EditProfileEvent, EditProfileEffect>() {

    private val _state = MutableStateFlow(EditProfileUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.Load -> load()
            is EditProfileEvent.DismissDialog -> dismissDialog()
            is EditProfileEvent.DismissActiveDialog -> dismissActiveDialog()
            is EditProfileEvent.UpdateProfile -> updateProfile(event)
            is EditProfileEvent.AddAddress -> addAddress(event)
            is EditProfileEvent.DeleteAddress -> deleteAddress(event)
            is EditProfileEvent.SetDefaultAddress -> setDefault(event)
            is EditProfileEvent.ClickBack -> emitEffect(EditProfileEffect.NavigateBack)
        }
    }

    private fun load() {
        observeCustomer()
        observeAddress()
        observeVillage()
    }

    private fun observeCustomer() {
        observeDataFlow(
            flow = customerUseCase(),
            onState = { state ->
                _state.update { it.copy(customer = state) }
            },
            onError = { showError(it) }
        )
    }

    private fun observeAddress() {
        observeDataFlow(
            flow = addressUseCase(),
            onState = { state ->
                _state.update { it.copy(addresses = state) }
            },
            onError = { showError(it) }
        )
    }

    private fun observeVillage() {
        observeDataFlow(
            flow = villageUseCase(),
            onState = { state ->
                _state.update { it.copy(villages = state) }
            },
            onError = { showError(it) }
        )
    }

    private fun updateProfile(event: EditProfileEvent.UpdateProfile) {
        observeDataFlow(
            flow = customerUpdateUseCase(
                CustomerUpdateParam(
                    name = event.name,
                    phone = event.phone,
                    photo = event.photo
                )
            ),
            onState = { state ->
                _state.update { it.copy(updateProfile = state) }
            },
            onError = { showError(it) },
            onSuccess = {
                _state.update {
                    it.copy(activeDialog = EditProfileDialog.SuccessUpdateProfile)
                }
            }
        )
    }

    private fun addAddress(event: EditProfileEvent.AddAddress) {
        observeDataFlow(
            flow = addressAddUseCase(
                AddressAddParam(
                    villageId = event.villageId,
                    block = event.block,
                    number = event.number,
                    rt = event.rt,
                    rw = event.rw,
                    isDefault = event.isDefault
                )
            ),
            onState = { state ->
                _state.update { it.copy(addAddress = state) }
            },
            onError = { showError(it) },
            onSuccess = { observeAddress() }
        )
    }

    private fun deleteAddress(event: EditProfileEvent.DeleteAddress) {
        observeDataFlow(
            flow = addressDeleteUseCase(
                AddressDeleteParam(event.id)
            ),
            onState = { state ->
                _state.update { it.copy(deleteAddress = state) }
            },
            onError = { showError(it) },
            onSuccess = { observeAddress() }
        )
    }

    private fun setDefault(event: EditProfileEvent.SetDefaultAddress) {
        observeDataFlow(
            flow = addressDefaultUseCase(
                AddressDefaultParam(event.id)
            ),
            onState = { state ->
                _state.update { it.copy(setDefaultAddress = state) }
            },
            onError = { showError(it) },
            onSuccess = { observeAddress() }
        )
    }

    private fun dismissActiveDialog() {
        _state.update { it.copy(activeDialog = null) }
    }

    private fun showError(error: UiError) {
        setDialog(
            UiDialog.Center(
                state = DialogStateV1(
                    type = DialogType.ERROR,
                    title = ErrorMessages.GENERIC_ERROR,
                    message = error.message
                ),
                onPrimary = { dismissDialog() }
            )
        )
    }
}