/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ProfileViewModel.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.46
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SecurePrefs
import com.mtv.app.core.provider.utils.SessionManager
import com.mtv.app.shopme.common.ConstantPreferences.CUSTOMER_RESPONSE
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.common.valueFlowOf
import com.mtv.app.shopme.domain.usecase.CustomerUseCase
import com.mtv.app.shopme.feature.customer.contract.ProfileDataListener
import com.mtv.app.shopme.feature.customer.contract.ProfileDialog
import com.mtv.app.shopme.feature.customer.contract.ProfileStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.update

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val securePrefs: SecurePrefs,
    private val customerUseCase: CustomerUseCase,
) : BaseViewModel(), UiOwner<ProfileStateListener, ProfileDataListener> {

    /** UI STATE : LOADING / ERROR / SUCCESS (API Response) */
    override val uiState = MutableStateFlow(ProfileStateListener())

    /** UI DATA : DATA PERSIST (Prefs) */
    override val uiData = MutableStateFlow(ProfileDataListener())

    init {
        //getCustomer()
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

    fun showLogoutDialog() {
        uiState.value = uiState.value.copy(activeDialog = ProfileDialog.LogoutConfirm)
    }

    fun dismissDialog() {
        uiState.value = uiState.value.copy(activeDialog = null)
    }

}
