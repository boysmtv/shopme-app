/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CafeViewModel.kt
 *
 * Last modified by Dedy Wijaya on 14/02/26 22.34
 */

package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.SavedStateHandle
import com.mtv.based.core.provider.based.BaseViewModel
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.common.valueFlowOf
import com.mtv.app.shopme.domain.mapper.toUiModel
import com.mtv.app.shopme.domain.usecase.GetCafeUseCase
import com.mtv.app.shopme.domain.usecase.GetFoodsByCafeUseCase
import com.mtv.app.shopme.feature.customer.contract.CafeDataListener
import com.mtv.app.shopme.feature.customer.contract.CafeStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class CafeViewModel @Inject constructor(
    private val getCafeUseCase: GetCafeUseCase,
    private val getFoodsByCafeUseCase: GetFoodsByCafeUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(), UiOwner<CafeStateListener, CafeDataListener> {

    override val uiState = MutableStateFlow(CafeStateListener())
    override val uiData = MutableStateFlow(CafeDataListener())

    private val cafeId: String = checkNotNull(savedStateHandle["cafeId"])

    init {
        loadCafe(cafeId)
    }

    fun loadCafe(cafeId: String) {
        launchUseCase(
            target = uiState.valueFlowOf(
                get = { it.cafeState },
                set = { copy(cafeState = it) }
            ),
            block = {
                getCafeUseCase(cafeId)
            },
            onSuccess = { response ->
                response.data?.let { cafe ->
                    uiData.update {
                        it.copy(
                            cafe = cafe.toUiModel()
                        )
                    }
                }
            }
        )

        launchUseCase(
            target = uiState.valueFlowOf(
                get = { it.foodsState },
                set = { copy(foodsState = it) }
            ),
            block = {
                getFoodsByCafeUseCase(cafeId)
            },
            loading = false,
            onSuccess = { response ->
                val foods = response.data.orEmpty()

                uiData.update {
                    it.copy(
                        foods = foods.map { it.toUiModel() }
                    )
                }
            }
        )
    }
}