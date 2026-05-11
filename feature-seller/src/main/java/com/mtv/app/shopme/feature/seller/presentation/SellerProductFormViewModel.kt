/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductFormViewModel.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 13.46
 */

package com.mtv.app.shopme.feature.seller.presentation

import androidx.lifecycle.SavedStateHandle
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.domain.model.FoodCategory
import com.mtv.app.shopme.domain.model.FoodStatus
import com.mtv.app.shopme.domain.model.ProductItem
import com.mtv.app.shopme.domain.model.VariantGroup
import com.mtv.app.shopme.domain.model.VariantOption
import com.mtv.app.shopme.domain.param.FoodOptionParam
import com.mtv.app.shopme.domain.param.FoodUpsertParam
import com.mtv.app.shopme.domain.param.FoodVariantParam
import com.mtv.app.shopme.domain.usecase.CreateFoodUseCase
import com.mtv.app.shopme.domain.usecase.DeleteFoodUseCase
import com.mtv.app.shopme.domain.usecase.GetFoodDetailUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerProfileUseCase
import com.mtv.app.shopme.domain.usecase.UpdateFoodUseCase
import com.mtv.app.shopme.feature.seller.contract.*
import com.mtv.app.shopme.feature.seller.ui.ProductStep
import com.mtv.app.shopme.feature.seller.ui.Availability
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SessionManager
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.math.BigDecimal

@HiltViewModel
class SellerProductFormViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val getSellerProfileUseCase: GetSellerProfileUseCase,
    private val getFoodDetailUseCase: GetFoodDetailUseCase,
    private val createFoodUseCase: CreateFoodUseCase,
    private val updateFoodUseCase: UpdateFoodUseCase,
    private val deleteFoodUseCase: DeleteFoodUseCase,
    savedStateHandle: SavedStateHandle
) : BaseEventViewModel<SellerProductFormEvent, SellerProductFormEffect>() {

    private val productId: String? = savedStateHandle["productId"]
    private var cafeId: String? = null

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
        observeDataFlow(
            flow = getSellerProfileUseCase(),
            onState = { state ->
                when (state) {
                    is LoadState.Loading -> _state.update { it.copy(isLoading = true) }
                    is LoadState.Success -> {
                        cafeId = state.data.cafeId
                        if (productId != null) {
                            loadProductDetail(productId)
                        } else {
                            _state.update { it.copy(isLoading = false) }
                        }
                    }
                    is LoadState.Error -> _state.update { it.copy(isLoading = false) }
                    else -> Unit
                }
            },
            onError = ::showError
        )
    }

    private fun save() {
        val product = _state.value.product

        if (product.name.isBlank() || product.price.isBlank() || product.stock <= 0) {
            showError(UiError.Validation(message = "Nama, harga, dan stok wajib valid"))
            return
        }

        val currentCafeId = cafeId
        if (currentCafeId.isNullOrBlank()) {
            showError(UiError.Unknown(message = "Cafe seller belum tersedia"))
            return
        }

        val request = runCatching { buildUpsertParam(currentCafeId) }
            .getOrElse {
                showError(UiError.Validation(message = it.message ?: "Data produk tidak valid"))
                return
            }

        val flow = if (productId.isNullOrBlank()) {
            createFoodUseCase(request)
        } else {
            updateFoodUseCase(productId, request)
        }

        observeDataFlow(
            flow = flow,
            onState = { state ->
                _state.update { it.copy(isSaving = state is LoadState.Loading) }
                if (state is LoadState.Success) {
                    emitEffect(SellerProductFormEffect.SaveSuccess)
                }
            },
            onError = ::showError
        )
    }

    private fun delete() {
        val id = productId ?: run {
            emitEffect(SellerProductFormEffect.NavigateBack)
            return
        }

        observeDataFlow(
            flow = deleteFoodUseCase(id),
            onState = { state ->
                _state.update { it.copy(isLoading = state is LoadState.Loading) }
                if (state is LoadState.Success) {
                    emitEffect(SellerProductFormEffect.DeleteSuccess)
                }
            },
            onError = ::showError
        )
    }

    private fun loadProductDetail(id: String) {
        observeDataFlow(
            flow = getFoodDetailUseCase(id),
            onState = { state ->
                _state.update {
                    if (state is LoadState.Success) {
                        state.data.toUiState()
                    } else {
                        it.copy(isLoading = state is LoadState.Loading)
                    }
                }
            },
            onError = ::showError
        )
    }

    private fun buildUpsertParam(currentCafeId: String): FoodUpsertParam {
        val state = _state.value
        val price = state.product.price.toBigDecimal()
        val status = when (state.availability) {
            Availability.READY -> FoodStatus.READY
            Availability.PREORDER -> FoodStatus.PREORDER
            Availability.JASTIP -> FoodStatus.JASTIP
        }
        val category = state.product.category
            .takeIf { it.isNotBlank() }
            ?.uppercase()
            ?.let { value -> FoodCategory.entries.firstOrNull { it.name == value } }
            ?: FoodCategory.OTHER

        return FoodUpsertParam(
            cafeId = currentCafeId,
            images = state.images.filterNotNull().filter {
                it.startsWith("http://") || it.startsWith("https://") || it.startsWith("data:image")
            },
            name = state.product.name.trim(),
            description = state.product.description.trim(),
            category = category,
            estimate = "",
            isActive = state.isActive,
            price = price,
            quantity = state.product.stock.toLong(),
            status = status,
            variants = state.variants
                .filter { it.name.isNotBlank() }
                .map { group ->
                    FoodVariantParam(
                        name = group.name.trim(),
                        options = group.options
                            .filter { it.name.isNotBlank() }
                            .map { option ->
                                FoodOptionParam(
                                    name = option.name.trim(),
                                    price = option.price.ifBlank { "0" }.toBigDecimal()
                                )
                            }
                    )
                }
        )
    }

    private fun Food.toUiState(): SellerProductFormUiState {
        this@SellerProductFormViewModel.cafeId = this.cafeId
        val remoteImages = images.take(5)
        val filledImages = List(5) { index -> remoteImages.getOrNull(index) }

        return _state.value.copy(
            isLoading = false,
            product = ProductItem(
                id = id,
                name = name,
                price = price.stripTrailingZeros().toPlainString(),
                stock = quantity.toInt(),
                category = category.name,
                description = description
            ),
            variants = variants.map { variant ->
                VariantGroup(
                    name = variant.name,
                    options = variant.options.map { option ->
                        VariantOption(
                            name = option.name,
                            price = option.price.stripTrailingZeros().toPlainString()
                        )
                    }
                )
            },
            isActive = isActive,
            availability = when (status) {
                FoodStatus.PREORDER -> Availability.PREORDER
                FoodStatus.JASTIP -> Availability.JASTIP
                else -> Availability.READY
            },
            images = filledImages
        )
    }

    private fun showError(error: UiError) {
        handleSessionError(
            error = error,
            sessionManager = sessionManager,
            beforeLogout = { _state.update { it.copy(isLoading = false, isSaving = false) } }
        ) {
            setDialog(
                UiDialog.Center(
                    state = DialogStateV1(
                        type = DialogType.ERROR,
                        title = ErrorMessages.GENERIC_ERROR,
                        message = it.message
                    ),
                    onPrimary = { dismissDialog() }
                )
            )
        }
    }
}
