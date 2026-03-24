/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeViewModel.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.45
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.shopme.domain.usecase.CustomerUseCase
import com.mtv.app.shopme.domain.usecase.HomeFoodUseCase
import com.mtv.app.shopme.feature.customer.contract.HomeUiState
import com.mtv.based.core.provider.based.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val customerUseCase: CustomerUseCase,
    private val homeFoodUseCase: HomeFoodUseCase,
) : BaseViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state

    private var isLoaded = false

    fun load() {
        if (isLoaded) return
        isLoaded = true

        refresh()
    }

    fun refresh() {
        observeCustomer()
        observeFoods()
    }

    private fun observeCustomer() {
        observeDataFlow(
            flow = customerUseCase(),
            onLoad = {
                _state.update {
                    it.copy(
                        isCustomerLoading = true
                    )
                }
            },
            onSuccess = { data ->
                _state.update {
                    it.copy(
                        customer = data,
                        isCustomerLoading = false,
                        isCustomerFresh = true
                    )
                }
            },
            onError = { error, data ->
                _state.update {
                    it.copy(
                        customer = data,
                        isCustomerLoading = false
                    )
                }
                showError(error)
            }
        )
    }

    private fun observeFoods() {
        observeDataFlow(
            flow = homeFoodUseCase(),
            onLoad = {
                _state.update {
                    it.copy(
                        foods = it.foods,
                        isFoodsLoading = true
                    )
                }
            },
            onSuccess = { data ->
                _state.update {
                    it.copy(
                        foods = data,
                        isFoodsLoading = false,
                        isFoodsFresh = true
                    )
                }
            },

            onError = { error, data ->
                _state.update {
                    it.copy(
                        foods = data ?: emptyList(),
                        isFoodsLoading = false
                    )
                }
                showError(error)
            }
        )
    }
}