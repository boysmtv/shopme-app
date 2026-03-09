/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeViewModel.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.45
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SecurePrefs
import com.mtv.app.core.provider.utils.SessionManager
import com.mtv.app.shopme.common.ConstantPreferences.CUSTOMER_RESPONSE
import com.mtv.app.shopme.common.ConstantPreferences.SPLASH_RESPONSE
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.common.valueFlowOf
import com.mtv.app.shopme.data.remote.request.LoginRequest
import com.mtv.app.shopme.domain.usecase.CustomerUseCase
import com.mtv.app.shopme.domain.usecase.HomeFoodUseCase
import com.mtv.app.shopme.feature.customer.contract.HomeDataListener
import com.mtv.app.shopme.feature.customer.contract.HomeStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.update

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val securePrefs: SecurePrefs,
    private val customerUseCase: CustomerUseCase,
    private val homeFoodUseCase: HomeFoodUseCase,
) : BaseViewModel(), UiOwner<HomeStateListener, HomeDataListener> {

    /** UI STATE : LOADING / ERROR / SUCCESS (API Response) */
    override val uiState = MutableStateFlow(HomeStateListener())

    /** UI DATA : DATA PERSIST (Prefs) */
    override val uiData = MutableStateFlow(HomeDataListener())

    init {
        //getCustomer()
        //getFood()
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

    fun getFood() {
        launchUseCase(
            target = uiState.valueFlowOf(
                get = { it.foodState },
                set = { state -> copy(foodState = state) }
            ),
            block = {
                homeFoodUseCase(Unit)
            },
            onSuccess = { data ->
                uiData.update {
                    it.copy(foodData = data.data)
                }
            }
        )
    }

}
