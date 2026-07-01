package com.mtv.app.shopme.feature.seller.contract

import androidx.compose.runtime.Immutable
import com.mtv.app.shopme.domain.model.SellerCategory

@Immutable
data class SellerCategoryUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val categories: List<SellerCategory> = emptyList(),
    val showFormDialog: Boolean = false,
    val editingCategory: SellerCategory? = null,
    val formName: String = "",
    val isSubmitting: Boolean = false
)

sealed class SellerCategoryEvent {
    object Load : SellerCategoryEvent()
    object ClickAdd : SellerCategoryEvent()
    object ClickCancelForm : SellerCategoryEvent()
    data class ClickEdit(val category: SellerCategory) : SellerCategoryEvent()
    data class ClickDelete(val categoryId: String) : SellerCategoryEvent()
    data class ChangeFormName(val value: String) : SellerCategoryEvent()
    object SubmitForm : SellerCategoryEvent()
}

sealed class SellerCategoryEffect {
    object NavigateBack : SellerCategoryEffect()
}
