package com.mtv.app.shopme.feature.seller.contract

import androidx.compose.runtime.Immutable
import com.mtv.app.shopme.domain.model.SellerDiscount

@Immutable
data class SellerDiscountUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val discounts: List<SellerDiscount> = emptyList(),
    val showFormDialog: Boolean = false,
    val editingDiscount: SellerDiscount? = null,
    val formName: String = "",
    val formType: String = "PERCENTAGE",
    val formValue: String = "",
    val formMinOrder: String = "",
    val formMaxDiscount: String = "",
    val formStartDate: String = "",
    val formEndDate: String = "",
    val formIsActive: Boolean = true,
    val isSubmitting: Boolean = false
)

sealed class SellerDiscountEvent {
    object Load : SellerDiscountEvent()
    object DismissDialog : SellerDiscountEvent()
    object ClickAdd : SellerDiscountEvent()
    object ClickCancelForm : SellerDiscountEvent()
    data class ClickEdit(val discount: SellerDiscount) : SellerDiscountEvent()
    data class ClickDelete(val discountId: String) : SellerDiscountEvent()
    data class ChangeFormName(val value: String) : SellerDiscountEvent()
    data class ChangeFormType(val value: String) : SellerDiscountEvent()
    data class ChangeFormValue(val value: String) : SellerDiscountEvent()
    data class ChangeFormMinOrder(val value: String) : SellerDiscountEvent()
    data class ChangeFormMaxDiscount(val value: String) : SellerDiscountEvent()
    data class ChangeFormStartDate(val value: String) : SellerDiscountEvent()
    data class ChangeFormEndDate(val value: String) : SellerDiscountEvent()
    data class ChangeFormIsActive(val value: Boolean) : SellerDiscountEvent()
    object SubmitForm : SellerDiscountEvent()
}

sealed class SellerDiscountEffect {
    object NavigateBack : SellerDiscountEffect()
}
