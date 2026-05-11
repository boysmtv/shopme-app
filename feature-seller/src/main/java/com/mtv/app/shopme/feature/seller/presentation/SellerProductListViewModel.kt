/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductListViewModel.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.58
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.domain.model.ProductItem
import com.mtv.app.shopme.domain.usecase.DeleteFoodUseCase
import com.mtv.app.shopme.domain.usecase.GetFoodsByCafeUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerProfileUseCase
import com.mtv.app.shopme.feature.seller.contract.*
import com.mtv.app.shopme.feature.seller.contract.SellerProductListEffect.*
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.provider.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class SellerProductListViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val getSellerProfileUseCase: GetSellerProfileUseCase,
    private val getFoodsByCafeUseCase: GetFoodsByCafeUseCase,
    private val deleteFoodUseCase: DeleteFoodUseCase
) : BaseEventViewModel<SellerProductListEvent, SellerProductListEffect>() {

    private val _state = MutableStateFlow(SellerProductListUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerProductListEvent) {
        when (event) {

            SellerProductListEvent.Load -> load()

            SellerProductListEvent.DismissDialog -> dismissDialog()

            SellerProductListEvent.ClickAdd ->
                emitEffect(NavigateToAdd)

            is SellerProductListEvent.ClickEdit ->
                emitEffect(NavigateToEdit(event.productId))

            is SellerProductListEvent.ClickDelete ->
                deleteProduct(event.productId)

            SellerProductListEvent.ClickBack ->
                emitEffect(NavigateBack)
        }
    }

    private fun load() {
        observeDataFlow(
            flow = getSellerProfileUseCase(),
            onState = { state ->
                when (state) {
                    is LoadState.Loading -> _state.update { it.copy(isLoading = true) }
                    is LoadState.Success -> {
                        val cafeId = state.data.cafeId
                        if (cafeId.isNullOrBlank()) {
                            _state.update { it.copy(isLoading = false, products = emptyList()) }
                        } else {
                            loadProducts(cafeId)
                        }
                    }
                    is LoadState.Error -> _state.update { it.copy(isLoading = false) }
                    else -> Unit
                }
            },
            onError = { _state.update { it.copy(isLoading = false) } }
        )
    }

    private fun deleteProduct(productId: String) {
        observeDataFlow(
            flow = deleteFoodUseCase(productId),
            onState = { state ->
                _state.update {
                    if (state is LoadState.Success) {
                        it.copy(
                            isLoading = false,
                            products = it.products.filterNot { product -> product.id == productId }
                        )
                    } else {
                        it.copy(isLoading = state is LoadState.Loading)
                    }
                }
            },
            onError = { _state.update { it.copy(isLoading = false) } }
        )
    }

    private fun loadProducts(cafeId: String) {
        observeDataFlow(
            flow = getFoodsByCafeUseCase(cafeId),
            onState = { state ->
                _state.update {
                    if (state is LoadState.Success) {
                        it.copy(
                            isLoading = false,
                            products = state.data.map { food -> food.toProductItem() }
                        )
                    } else {
                        it.copy(isLoading = state is LoadState.Loading)
                    }
                }
            },
            onError = { _state.update { it.copy(isLoading = false) } }
        )
    }

    private fun Food.toProductItem() = ProductItem(
        id = id,
        name = name,
        price = price.stripTrailingZeros().toPlainString(),
        stock = quantity.toInt(),
        category = category.name,
        description = description,
        image = images.firstOrNull().orEmpty()
    )
}
