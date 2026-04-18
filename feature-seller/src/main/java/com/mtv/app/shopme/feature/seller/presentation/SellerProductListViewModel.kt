/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductListViewModel.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.58
 */

package com.mtv.app.shopme.feature.seller.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.model.ProductItem
import com.mtv.app.shopme.feature.seller.contract.*
import com.mtv.app.shopme.feature.seller.contract.SellerProductListEffect.*
import com.mtv.based.core.provider.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SellerProductListViewModel @Inject constructor(
    private val sessionManager: SessionManager
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

    private fun update(block: SellerProductListUiState.() -> SellerProductListUiState) {
        _state.update { it.block() }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            delay(1000)

            _state.update {
                it.copy(
                    isLoading = false,
                    products = dummyProducts()
                )
            }
        }
    }

    private fun deleteProduct(productId: String) {
        val updated = _state.value.products.filterNot { it.id == productId }

        _state.update { it.copy(products = updated) }
    }

    private fun dummyProducts(): List<ProductItem> {
        return listOf(
            ProductItem("1", "Coffee Latte", "25000", 10, "Drink", "Hot coffee"),
            ProductItem("2", "Croissant", "15000", 20, "Food", "Butter croissant")
        )
    }
}