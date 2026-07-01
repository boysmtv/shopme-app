package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.param.DiscountParam
import com.mtv.app.shopme.domain.usecase.CreateDiscountUseCase
import com.mtv.app.shopme.domain.usecase.DeleteDiscountUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerDiscountsUseCase
import com.mtv.app.shopme.domain.usecase.UpdateDiscountUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerDiscountEffect
import com.mtv.app.shopme.feature.seller.contract.SellerDiscountEvent
import com.mtv.app.shopme.feature.seller.contract.SellerDiscountUiState
import com.mtv.based.core.network.utils.LoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class SellerDiscountViewModel @Inject constructor(
    private val getSellerDiscountsUseCase: GetSellerDiscountsUseCase,
    private val createDiscountUseCase: CreateDiscountUseCase,
    private val updateDiscountUseCase: UpdateDiscountUseCase,
    private val deleteDiscountUseCase: DeleteDiscountUseCase
) : BaseEventViewModel<SellerDiscountEvent, SellerDiscountEffect>() {

    private val _state = MutableStateFlow(SellerDiscountUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerDiscountEvent) {
        when (event) {
            SellerDiscountEvent.Load -> loadDiscounts()
            SellerDiscountEvent.DismissDialog -> dismissDialog()
            SellerDiscountEvent.ClickAdd -> showAddForm()
            SellerDiscountEvent.ClickCancelForm -> hideForm()
            is SellerDiscountEvent.ClickEdit -> showEditForm(event.discount)
            is SellerDiscountEvent.ClickDelete -> deleteDiscount(event.discountId)
            is SellerDiscountEvent.ChangeFormName -> _state.update { it.copy(formName = event.value) }
            is SellerDiscountEvent.ChangeFormType -> _state.update { it.copy(formType = event.value) }
            is SellerDiscountEvent.ChangeFormValue -> _state.update { it.copy(formValue = event.value) }
            is SellerDiscountEvent.ChangeFormMinOrder -> _state.update { it.copy(formMinOrder = event.value) }
            is SellerDiscountEvent.ChangeFormMaxDiscount -> _state.update { it.copy(formMaxDiscount = event.value) }
            is SellerDiscountEvent.ChangeFormStartDate -> _state.update { it.copy(formStartDate = event.value) }
            is SellerDiscountEvent.ChangeFormEndDate -> _state.update { it.copy(formEndDate = event.value) }
            is SellerDiscountEvent.ChangeFormIsActive -> _state.update { it.copy(formIsActive = event.value) }
            SellerDiscountEvent.SubmitForm -> submitForm()
        }
    }

    private fun loadDiscounts() {
        observeIndependentDataFlow(
            flow = getSellerDiscountsUseCase(),
            onState = { result ->
                _state.update {
                    it.copy(
                        discounts = if (result is LoadState.Success) result.data else it.discounts,
                        isLoading = result is LoadState.Loading && it.discounts.isEmpty(),
                        errorMessage = null
                    )
                }
            },
            onError = { _state.update { it.copy(isLoading = false, errorMessage = it.message) } }
        )
    }

    private fun showAddForm() {
        _state.update {
            it.copy(
                showFormDialog = true,
                editingDiscount = null,
                formName = "",
                formType = "PERCENTAGE",
                formValue = "",
                formMinOrder = "",
                formMaxDiscount = "",
                formStartDate = "",
                formEndDate = "",
                formIsActive = true
            )
        }
    }

    private fun showEditForm(discount: com.mtv.app.shopme.domain.model.SellerDiscount) {
        _state.update {
            it.copy(
                showFormDialog = true,
                editingDiscount = discount,
                formName = discount.name,
                formType = discount.type.name,
                formValue = discount.value,
                formMinOrder = discount.minOrder.orEmpty(),
                formMaxDiscount = discount.maxDiscount.orEmpty(),
                formStartDate = discount.startDate,
                formEndDate = discount.endDate,
                formIsActive = discount.isActive
            )
        }
    }

    private fun hideForm() {
        _state.update { it.copy(showFormDialog = false, editingDiscount = null) }
    }

    private fun submitForm() {
        val s = _state.value
        if (s.formName.isBlank() || s.formValue.isBlank()) return

        _state.update { it.copy(isSubmitting = true) }

        val param = DiscountParam(
            name = s.formName,
            type = try { com.mtv.app.shopme.domain.model.DiscountType.valueOf(s.formType) } catch (e: Exception) { com.mtv.app.shopme.domain.model.DiscountType.PERCENTAGE },
            value = s.formValue,
            minOrder = s.formMinOrder.ifBlank { null },
            maxDiscount = s.formMaxDiscount.ifBlank { null },
            startDate = s.formStartDate,
            endDate = s.formEndDate,
            isActive = s.formIsActive
        )

        val flow = if (s.editingDiscount != null) {
            updateDiscountUseCase(s.editingDiscount.id, param)
        } else {
            createDiscountUseCase(param)
        }

        observeDataFlow(
            flow = flow,
            onState = { result ->
                if (result is LoadState.Success) {
                    hideForm()
                    loadDiscounts()
                }
                _state.update { it.copy(isSubmitting = result is LoadState.Loading) }
            },
            onError = { _state.update { it.copy(isSubmitting = false) } }
        )
    }

    private fun deleteDiscount(discountId: String) {
        observeDataFlow(
            flow = deleteDiscountUseCase(discountId),
            onState = { result ->
                if (result is LoadState.Success) loadDiscounts()
            }
        )
    }
}
