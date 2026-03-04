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
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.domain.usecase.HomeFoodCategoryUseCase
import com.mtv.app.shopme.domain.usecase.HomeFoodUseCase
import com.mtv.app.shopme.feature.customer.contract.HomeDataListener
import com.mtv.app.shopme.feature.customer.contract.HomeStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val securePrefs: SecurePrefs,
    private val getHomeFoodUseCase: HomeFoodUseCase,
    private val getHomeFoodCategoryUseCase: HomeFoodCategoryUseCase
) : BaseViewModel(), UiOwner<HomeStateListener, HomeDataListener> {

    /** UI STATE : LOADING / ERROR / SUCCESS (API Response) */
    override val uiState = MutableStateFlow(HomeStateListener())

    /** UI DATA : DATA PERSIST (Prefs) */
    override val uiData = MutableStateFlow(HomeDataListener())

//    fun getFood() = launchUseCase(
//        loading = false,
//        target = uiState.valueFlowOf(
//            get = { it.nowPlayingState },
//            set = { state -> copy(nowPlayingState = state) }
//        ),
//        block = { getHomeFoodUseCase(Unit) }
//    )

}
