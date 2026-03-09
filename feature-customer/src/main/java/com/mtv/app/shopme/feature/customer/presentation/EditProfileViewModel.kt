/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: EditProfileViewModel.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 14.04
 */

package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SecurePrefs
import com.mtv.app.core.provider.utils.SessionManager
import com.mtv.app.shopme.common.ConstantPreferences.CUSTOMER_RESPONSE
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.common.valueFlowOf
import com.mtv.app.shopme.data.remote.request.AddressAddRequest
import com.mtv.app.shopme.data.remote.request.AddressDefaultRequest
import com.mtv.app.shopme.data.remote.request.AddressDeleteRequest
import com.mtv.app.shopme.data.remote.request.ChatMessageSendRequest
import com.mtv.app.shopme.domain.usecase.AddressAddUseCase
import com.mtv.app.shopme.domain.usecase.AddressDefaultUseCase
import com.mtv.app.shopme.domain.usecase.AddressDeleteUseCase
import com.mtv.app.shopme.domain.usecase.AddressUseCase
import com.mtv.app.shopme.domain.usecase.CustomerUseCase
import com.mtv.app.shopme.feature.customer.contract.EditProfileDataListener
import com.mtv.app.shopme.feature.customer.contract.EditProfileStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val securePrefs: SecurePrefs,
    private val customerUseCase: CustomerUseCase,
    private val addressUseCase: AddressUseCase,
    private val addressAddUseCase: AddressAddUseCase,
    private val addressDeleteUseCase: AddressDeleteUseCase,
    private val addressDefaultUseCase: AddressDefaultUseCase,
) : BaseViewModel(), UiOwner<EditProfileStateListener, EditProfileDataListener> {

    /** UI STATE : LOADING / ERROR / SUCCESS (API Response) */
    override val uiState = MutableStateFlow(EditProfileStateListener())

    /** UI DATA : DATA PERSIST (Prefs) */
    override val uiData = MutableStateFlow(EditProfileDataListener())

    init {
        //getCustomer()
        //getAddress()
    }

    fun getCustomer() {
        launchUseCase(
            loading = false,
            target = uiState.valueFlowOf(
                get = { it.customerState },
                set = { state -> copy(customerState = state) }
            ),
            block = {
                customerUseCase(Unit)
            },
            onSuccess = { data ->
                securePrefs.putObject(CUSTOMER_RESPONSE, data)

                uiData.update {
                    it.copy(customerData = data.data)
                }
            }
        )
    }

    fun getAddress() {
        launchUseCase(
            loading = false,
            target = uiState.valueFlowOf(
                get = { it.addressState },
                set = { state -> copy(addressState = state) }
            ),
            block = {
                addressUseCase(Unit)
            },
            onSuccess = { data ->
                uiData.update {
                    it.copy(addressData = data.data)
                }
            }
        )
    }

    fun doAddAddress(
        areaId: String,
        block: String,
        number: String,
        rt: String,
        rw: String,
        isDefault: Boolean
    ) {
        launchUseCase(
            target = uiState.valueFlowOf(
                get = { it.addressAddState },
                set = { state -> copy(addressAddState = state) }
            ),
            block = {
                addressAddUseCase(
                    AddressAddRequest(
                        areaId = areaId,
                        block = block,
                        number = number,
                        rt = rt,
                        rw = rw,
                        isDefault = isDefault
                    )
                )
            }
        )
    }

    fun doDeleteAddress(
        id: String,
    ) {
        launchUseCase(
            target = uiState.valueFlowOf(
                get = { it.addressDeleteState },
                set = { state -> copy(addressDeleteState = state) }
            ),
            block = {
                addressDeleteUseCase(
                    AddressDeleteRequest(
                        id = id,
                    )
                )
            }
        )
    }

    fun doDefaultAddress(
        id: String,
    ) {
        launchUseCase(
            target = uiState.valueFlowOf(
                get = { it.addressDefaultState },
                set = { state -> copy(addressDefaultState = state) }
            ),
            block = {
                addressDefaultUseCase(
                    AddressDefaultRequest(
                        id = id,
                    )
                )
            }
        )
    }
}
