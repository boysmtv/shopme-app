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
import com.mtv.app.shopme.domain.usecase.UploadMediaUseCase
import com.mtv.app.shopme.feature.customer.contract.EditProfileDialog
import com.mtv.app.shopme.feature.customer.contract.EditProfileEffect
import com.mtv.app.shopme.feature.customer.contract.EditProfileEvent
import com.mtv.app.shopme.feature.customer.contract.EditProfileUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.provider.utils.SessionManager
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val customerUseCase: GetCustomerUseCase,
    private val customerUpdateUseCase: UpdateCustomerUseCase,
    private val addressUseCase: GetAddressUseCase,
    private val addressAddUseCase: CreateAddressUseCase,
    private val addressDeleteUseCase: DeleteAddressUseCase,
    private val addressDefaultUseCase: UpdateAddressDefaultUseCase,
    private val villageUseCase: GetVillageUseCase,
    private val uploadMediaUseCase: UploadMediaUseCase
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
        observeIndependentDataFlow(
            flow = customerUseCase(),
            onState = { state ->
                _state.update { it.copy(customer = state) }
            },
            onError = { showError(it) }
        )
    }

    private fun observeAddress() {
        observeIndependentDataFlow(
            flow = addressUseCase(),
            onState = { state ->
                _state.update { it.copy(addresses = state) }
            },
            onError = { showError(it) }
        )
    }

    private fun observeVillage() {
        observeIndependentDataFlow(
            flow = villageUseCase(),
            onState = { state ->
                _state.update { it.copy(villages = state) }
            },
            onError = { showError(it) }
        )
    }

    private fun updateProfile(event: EditProfileEvent.UpdateProfile) {
        viewModelScope.launch {
            _state.update { it.copy(updateProfile = LoadState.Loading) }
            val resolvedPhoto = runCatching {
                resolveUploadedReference(event.photo, "customer-profile")
            }.getOrElse { throwable ->
                val uploadError = UiError.Validation(message = throwable.message ?: "Failed uploading image")
                _state.update { state -> state.copy(updateProfile = LoadState.Error(uploadError)) }
                showError(uploadError)
                return@launch
            }

            observeDataFlow(
                flow = customerUpdateUseCase(
                    CustomerUpdateParam(
                        name = event.name,
                        phone = event.phone,
                        photo = resolvedPhoto,
                        fcmToken = null
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
    }

    private suspend fun resolveUploadedReference(reference: String, scope: String): String {
        if (!reference.startsWith("content://")) {
            return reference
        }

        return when (val result = uploadMediaUseCase(reference, scope).first { it !is Resource.Loading }) {
            is Resource.Success -> result.data.originalUrl
            is Resource.Error -> throw IllegalStateException(result.error.message)
            else -> reference
        }
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
        handleSessionError(error, sessionManager) {
            setDialog(
                UiDialog.Center(
                    state = DialogStateV1(
                        type = DialogType.ERROR,
                        title = ErrorMessages.GENERIC_ERROR,
                        message = it.message
                    ),
                    onPrimary = { dismissDialog() }
                )
            )
        }
    }
}
