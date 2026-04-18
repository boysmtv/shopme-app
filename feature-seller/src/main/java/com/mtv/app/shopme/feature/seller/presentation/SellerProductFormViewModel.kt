/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductFormViewModel.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 13.46
 */

package com.mtv.app.shopme.feature.seller.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.model.ProductItem
import com.mtv.app.shopme.domain.model.VariantGroup
import com.mtv.app.shopme.domain.model.VariantOption
import com.mtv.app.shopme.feature.seller.contract.*
import com.mtv.app.shopme.feature.seller.ui.ProductStep
import com.mtv.based.core.provider.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SellerProductFormViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    savedStateHandle: SavedStateHandle
) : BaseEventViewModel<SellerProductFormEvent, SellerProductFormEffect>() {

    private val productId: String? = savedStateHandle["productId"]

    private val _state = MutableStateFlow(SellerProductFormUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerProductFormEvent) {
        when (event) {

            SellerProductFormEvent.Load -> load()

            SellerProductFormEvent.DismissDialog -> dismissDialog()

            is SellerProductFormEvent.ChangeName ->
                update { copy(product = product.copy(name = event.value)) }

            is SellerProductFormEvent.ChangePrice ->
                update { copy(product = product.copy(price = event.value)) }

            is SellerProductFormEvent.ChangeStock ->
                update { copy(product = product.copy(stock = event.value)) }

            is SellerProductFormEvent.ChangeCategory ->
                update { copy(product = product.copy(category = event.value)) }

            is SellerProductFormEvent.ChangeDescription ->
                update { copy(product = product.copy(description = event.value)) }

            SellerProductFormEvent.Save -> save()

            SellerProductFormEvent.Delete -> delete()

            SellerProductFormEvent.ClickBack ->
                emitEffect(SellerProductFormEffect.NavigateBack)

            is SellerProductFormEvent.ChangeActive ->
                update { copy(isActive = event.value) }

            is SellerProductFormEvent.ChangeAvailability ->
                update { copy(availability = event.value) }

            SellerProductFormEvent.NextStep ->
                update {
                    val next = step.ordinal + 1
                    if (next < ProductStep.entries.size) {
                        copy(step = ProductStep.entries[next])
                    } else this
                }

            SellerProductFormEvent.PrevStep ->
                update {
                    val prev = step.ordinal - 1
                    if (prev >= 0) {
                        copy(step = ProductStep.entries[prev])
                    } else this
                }

            is SellerProductFormEvent.AddImage ->
                update {
                    val list = images.toMutableList()
                    val index = list.indexOfFirst { it == null }
                    if (index != -1) list[index] = event.uri
                    copy(images = list)
                }

            is SellerProductFormEvent.RemoveImage ->
                update {
                    copy(
                        images = images.toMutableList().apply {
                            this[event.index] = null
                        }
                    )
                }

            is SellerProductFormEvent.AddVariantGroup ->
                update {
                    copy(
                        variants = variants + VariantGroup()
                    )
                }

            is SellerProductFormEvent.RemoveVariantGroup ->
                update {
                    copy(
                        variants = variants.toMutableList().apply {
                            removeAt(event.index)
                        }
                    )
                }

            is SellerProductFormEvent.ChangeVariantGroupName ->
                update {
                    copy(
                        variants = variants.mapIndexed { i, group ->
                            if (i == event.index)
                                group.copy(name = event.value)
                            else group
                        }
                    )
                }

            is SellerProductFormEvent.AddVariantOption ->
                update {
                    copy(
                        variants = variants.mapIndexed { i, group ->
                            if (i == event.groupIndex)
                                group.copy(
                                    options = group.options + VariantOption()
                                )
                            else group
                        }
                    )
                }

            is SellerProductFormEvent.RemoveVariantOption ->
                update {
                    copy(
                        variants = variants.mapIndexed { i, group ->
                            if (i == event.groupIndex)
                                group.copy(
                                    options = group.options.toMutableList().apply {
                                        removeAt(event.optionIndex)
                                    }
                                )
                            else group
                        }
                    )
                }

            is SellerProductFormEvent.ChangeVariantOptionName ->
                update {
                    copy(
                        variants = variants.mapIndexed { gi, group ->
                            if (gi == event.groupIndex)
                                group.copy(
                                    options = group.options.mapIndexed { oi, opt ->
                                        if (oi == event.optionIndex)
                                            opt.copy(name = event.value)
                                        else opt
                                    }
                                )
                            else group
                        }
                    )
                }

            is SellerProductFormEvent.ChangeVariantOptionPrice ->
                update {
                    copy(
                        variants = variants.mapIndexed { gi, group ->
                            if (gi == event.groupIndex)
                                group.copy(
                                    options = group.options.mapIndexed { oi, opt ->
                                        if (oi == event.optionIndex)
                                            opt.copy(price = event.value)
                                        else opt
                                    }
                                )
                            else group
                        }
                    )
                }
        }
    }

    private fun update(block: SellerProductFormUiState.() -> SellerProductFormUiState) {
        _state.update { it.block() }
    }

    private fun load() {
        productId?.let { id ->
            _state.update {
                it.copy(
                    product = ProductItem(
                        id = id,
                        name = "Nasi Goreng",
                        price = "20000",
                        stock = 10,
                        category = "Food",
                        description = "Enak banget"
                    )
                )
            }
        }
    }

    private fun save() {
        val product = _state.value.product

        if (product.name.isBlank() || product.price.isBlank()) {
            // bisa pakai dialog error kalau mau
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            delay(1000)

            _state.update { it.copy(isSaving = false) }

            emitEffect(SellerProductFormEffect.SaveSuccess)
        }
    }

    private fun delete() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(1000)
            _state.update { it.copy(isLoading = false) }
            emitEffect(SellerProductFormEffect.DeleteSuccess)
        }
    }
}