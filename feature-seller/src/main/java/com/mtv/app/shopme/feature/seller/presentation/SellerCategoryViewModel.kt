package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.param.SellerCategoryParam
import com.mtv.app.shopme.domain.usecase.CreateSellerCategoryUseCase
import com.mtv.app.shopme.domain.usecase.DeleteSellerCategoryUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerCategoriesUseCase
import com.mtv.app.shopme.domain.usecase.UpdateSellerCategoryUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerCategoryEffect
import com.mtv.app.shopme.feature.seller.contract.SellerCategoryEvent
import com.mtv.app.shopme.feature.seller.contract.SellerCategoryUiState
import com.mtv.based.core.network.utils.LoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class SellerCategoryViewModel @Inject constructor(
    private val getSellerCategoriesUseCase: GetSellerCategoriesUseCase,
    private val createSellerCategoryUseCase: CreateSellerCategoryUseCase,
    private val updateSellerCategoryUseCase: UpdateSellerCategoryUseCase,
    private val deleteSellerCategoryUseCase: DeleteSellerCategoryUseCase
) : BaseEventViewModel<SellerCategoryEvent, SellerCategoryEffect>() {

    private val _state = MutableStateFlow(SellerCategoryUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerCategoryEvent) {
        when (event) {
            SellerCategoryEvent.Load -> loadCategories()
            SellerCategoryEvent.ClickAdd -> showAddForm()
            SellerCategoryEvent.ClickCancelForm -> hideForm()
            is SellerCategoryEvent.ClickEdit -> showEditForm(event.category)
            is SellerCategoryEvent.ClickDelete -> deleteCategory(event.categoryId)
            is SellerCategoryEvent.ChangeFormName -> _state.update { it.copy(formName = event.value) }
            SellerCategoryEvent.SubmitForm -> submitForm()
        }
    }

    private fun loadCategories() {
        observeIndependentDataFlow(
            flow = getSellerCategoriesUseCase(),
            onState = { result ->
                _state.update {
                    it.copy(
                        categories = if (result is LoadState.Success) result.data else it.categories,
                        isLoading = result is LoadState.Loading && it.categories.isEmpty(),
                        errorMessage = null
                    )
                }
            },
            onError = { _state.update { it.copy(isLoading = false, errorMessage = it.message) } }
        )
    }

    private fun showAddForm() {
        _state.update { it.copy(showFormDialog = true, editingCategory = null, formName = "") }
    }

    private fun showEditForm(category: com.mtv.app.shopme.domain.model.SellerCategory) {
        _state.update { it.copy(showFormDialog = true, editingCategory = category, formName = category.name) }
    }

    private fun hideForm() {
        _state.update { it.copy(showFormDialog = false, editingCategory = null) }
    }

    private fun submitForm() {
        val name = _state.value.formName
        if (name.isBlank()) return

        _state.update { it.copy(isSubmitting = true) }

        val param = SellerCategoryParam(name = name)
        val flow = if (_state.value.editingCategory != null) {
            updateSellerCategoryUseCase(_state.value.editingCategory!!.id, param)
        } else {
            createSellerCategoryUseCase(param)
        }

        observeDataFlow(
            flow = flow,
            onState = { result ->
                if (result is LoadState.Success) {
                    hideForm()
                    loadCategories()
                }
                _state.update { it.copy(isSubmitting = result is LoadState.Loading) }
            },
            onError = { _state.update { it.copy(isSubmitting = false) } }
        )
    }

    private fun deleteCategory(categoryId: String) {
        observeDataFlow(
            flow = deleteSellerCategoryUseCase(categoryId),
            onState = { result ->
                if (result is LoadState.Success) loadCategories()
            }
        )
    }
}
