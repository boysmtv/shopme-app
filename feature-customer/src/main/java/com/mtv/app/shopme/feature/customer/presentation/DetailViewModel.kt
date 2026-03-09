/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: DetailViewModel.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 09.01
 */

package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.SavedStateHandle
import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SecurePrefs
import com.mtv.app.core.provider.utils.SessionManager
import com.mtv.app.shopme.common.ConstantPreferences.CUSTOMER_RESPONSE
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.common.valueFlowOf
import com.mtv.app.shopme.data.remote.request.FoodAddToCartRequest
import com.mtv.app.shopme.domain.usecase.FoodAddToCartUseCase
import com.mtv.app.shopme.domain.usecase.FoodDetailUseCase
import com.mtv.app.shopme.domain.usecase.FoodSimilarUseCase
import com.mtv.app.shopme.feature.customer.contract.DetailDataListener
import com.mtv.app.shopme.feature.customer.contract.DetailStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.update

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val foodDetailUseCase: FoodDetailUseCase,
    private val foodSimilarUseCase: FoodSimilarUseCase,
    private val foodAddToCartUseCase: FoodAddToCartUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel(), UiOwner<DetailStateListener, DetailDataListener> {

    /** UI STATE : LOADING / ERROR / SUCCESS (API Response) */
    override val uiState = MutableStateFlow(DetailStateListener())

    /** UI DATA : DATA PERSIST (Prefs) */
    override val uiData = MutableStateFlow(DetailDataListener())

    private val foodId: String = checkNotNull(savedStateHandle["foodId"])

    init {
        doGetFoodDetail()
    }

    fun doGetFoodDetail() {
        launchUseCase(
            loading = false,
            target = uiState.valueFlowOf(
                get = { it.foodState },
                set = { state -> copy(foodState = state) }
            ),
            block = {
                foodDetailUseCase(foodId)
            },
            onSuccess = { data ->
                uiData.update {
                    it.copy(foodData = data.data)
                }

                data.data?.let { doGetSimilarFoods(it.cafeId) }
            }
        )
    }

    fun doGetSimilarFoods(
        cafeId: String
    ) {
        launchUseCase(
            loading = false,
            target = uiState.valueFlowOf(
                get = { it.foodSimilarState },
                set = { state -> copy(foodSimilarState = state) }
            ),
            block = {
                foodSimilarUseCase(cafeId)
            },
            onSuccess = { data ->
                uiData.update {
                    it.copy(foodSimilarData = data.data)
                }
            }
        )
    }

    fun doAddToCart(
        foodId: String,
        sizeId: String,
        variantId: String,
        additionalId: String,
    ) {
        launchUseCase(
            loading = false,
            target = uiState.valueFlowOf(
                get = { it.foodAddToCartState },
                set = { state -> copy(foodAddToCartState = state) }
            ),
            block = {
                foodAddToCartUseCase(
                    FoodAddToCartRequest(
                        foodId = foodId,
                        sizeId = sizeId,
                        variantId = variantId,
                        additionalId = additionalId
                    )
                )
            }
        )
    }
}
