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
            SellerProductListEvent.LoadMore -> loadMore()

            SellerProductListEvent.DismissDialog -> dismissDialog()

            SellerProductListEvent.ClickAdd ->
                emitEffect(NavigateToAdd)

            is SellerProductListEvent.ClickEdit ->
                emitEffect(NavigateToEdit(event.productId))

            is SellerProductListEvent.ClickDelete ->
                deleteProduct(event.productId)

            is SellerProductListEvent.ChangeSearchQuery -> {
                _state.update { it.copy(searchQuery = event.value) }
                reloadProducts()
            }

            is SellerProductListEvent.SelectCategoryFilter -> {
                _state.update { it.copy(categoryFilter = event.value) }
                reloadProducts()
            }

            is SellerProductListEvent.SelectStatusFilter -> {
                _state.update { it.copy(statusFilter = event.value) }
                reloadProducts()
            }

            is SellerProductListEvent.SelectActiveFilter -> {
                _state.update { it.copy(activeFilter = event.value) }
                reloadProducts()
            }

            SellerProductListEvent.ClearFilters -> {
                _state.update {
                    it.copy(
                        searchQuery = "",
                        categoryFilter = null,
                        statusFilter = null,
                        activeFilter = null
                    )
                }
                reloadProducts()
            }

            SellerProductListEvent.ClickBack ->
                emitEffect(NavigateBack)
        }
    }

    private fun load() {
        observeDataFlow(
            flow = getSellerProfileUseCase(),
            onState = { state ->
                when (state) {
                    is LoadState.Loading -> _state.update {
                        it.copy(
                            isLoading = it.products.isEmpty(),
                            isRefreshing = it.products.isNotEmpty(),
                            errorMessage = null
                        )
                    }
                    is LoadState.Success -> {
                        val cafeId = state.data.cafeId
                        if (cafeId.isNullOrBlank()) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    isRefreshing = false,
                                    isLoadingMore = false,
                                    products = emptyList(),
                                    cafeId = null,
                                    currentPage = 0,
                                    isLastPage = true,
                                    errorMessage = null
                                )
                            }
                        } else {
                            _state.update { it.copy(cafeId = cafeId) }
                            loadProducts(cafeId, page = 0, append = false)
                        }
                    }
                    is LoadState.Error -> _state.update { it.copy(isLoading = false, isRefreshing = false, errorMessage = state.error.message) }
                    else -> Unit
                }
            },
            onError = { error ->
                _state.update { state ->
                    state.copy(isLoading = false, isRefreshing = false, errorMessage = error.message)
                }
            }
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
                            isRefreshing = false,
                            errorMessage = null,
                            products = it.products.filterNot { product -> product.id == productId }
                        )
                    } else {
                        it.copy(
                            isLoading = state is LoadState.Loading && it.products.isEmpty(),
                            isRefreshing = state is LoadState.Loading && it.products.isNotEmpty(),
                            errorMessage = if (state is LoadState.Loading) null else it.errorMessage
                        )
                    }
                }
            },
            onError = { error ->
                _state.update { state ->
                    state.copy(isLoading = false, isRefreshing = false, errorMessage = error.message)
                }
            }
        )
    }

    private fun loadMore() {
        val state = _state.value
        val cafeId = state.cafeId ?: return
        if (state.isLoading || state.isRefreshing || state.isLoadingMore || state.isLastPage) return
        loadProducts(cafeId, page = state.currentPage + 1, append = true)
    }

    private fun reloadProducts() {
        val cafeId = _state.value.cafeId
        if (cafeId.isNullOrBlank()) {
            load()
        } else {
            loadProducts(cafeId, page = 0, append = false)
        }
    }

    private fun loadProducts(cafeId: String, page: Int, append: Boolean) {
        val filters = _state.value
        observeDataFlow(
            flow = getFoodsByCafeUseCase(
                id = cafeId,
                page = page,
                size = PAGE_SIZE,
                query = filters.searchQuery,
                category = filters.categoryFilter,
                status = filters.statusFilter,
                active = filters.activeFilter
            ),
            onState = { state ->
                _state.update {
                    if (state is LoadState.Success) {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            isLoadingMore = false,
                            errorMessage = null,
                            currentPage = state.data.page,
                            isLastPage = state.data.last,
                            products = if (append) {
                                it.products + state.data.content.map { food -> food.toProductItem() }
                            } else {
                                state.data.content.map { food -> food.toProductItem() }
                            }
                        )
                    } else {
                        it.copy(
                            isLoading = state is LoadState.Loading && it.products.isEmpty() && !append,
                            isRefreshing = state is LoadState.Loading && it.products.isNotEmpty() && !append,
                            isLoadingMore = state is LoadState.Loading && append,
                            errorMessage = if (state is LoadState.Loading) null else it.errorMessage
                        )
                    }
                }
            },
            onError = { error ->
                _state.update { state ->
                    state.copy(isLoading = false, isRefreshing = false, isLoadingMore = false, errorMessage = error.message)
                }
            }
        )
    }

    private fun Food.toProductItem() = ProductItem(
        id = id,
        name = name,
        price = price.stripTrailingZeros().toPlainString(),
        stock = quantity.toInt(),
        category = category.label,
        description = description,
        image = images.firstOrNull().orEmpty()
    )

    private companion object {
        const val PAGE_SIZE = 20
    }
}
