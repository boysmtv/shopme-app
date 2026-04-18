/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductFormContract.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 13.42
 */

package com.mtv.app.shopme.feature.seller.contract

import com.mtv.app.shopme.domain.model.ProductItem
import com.mtv.app.shopme.domain.model.VariantGroup
import com.mtv.app.shopme.feature.seller.ui.Availability
import com.mtv.app.shopme.feature.seller.ui.ProductStep

data class SellerProductFormUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,

    val step: ProductStep = ProductStep.BASIC,

    val product: ProductItem = ProductItem(),
    val variants: List<VariantGroup> = emptyList(),

    val isActive: Boolean = true,
    val availability: Availability = Availability.READY,

    val images: List<String?> = List(5) { null }
)

sealed class SellerProductFormEvent {

    object Load : SellerProductFormEvent()
    object DismissDialog : SellerProductFormEvent()

    data class ChangeName(val value: String) : SellerProductFormEvent()
    data class ChangePrice(val value: String) : SellerProductFormEvent()
    data class ChangeStock(val value: Int) : SellerProductFormEvent()
    data class ChangeCategory(val value: String) : SellerProductFormEvent()
    data class ChangeDescription(val value: String) : SellerProductFormEvent()

    data class ChangeActive(val value: Boolean) : SellerProductFormEvent()
    data class ChangeAvailability(val value: Availability) : SellerProductFormEvent()

    data class AddImage(val uri: String) : SellerProductFormEvent()
    data class RemoveImage(val index: Int) : SellerProductFormEvent()

    object NextStep : SellerProductFormEvent()
    object PrevStep : SellerProductFormEvent()

    object Save : SellerProductFormEvent()
    object Delete : SellerProductFormEvent()

    object ClickBack : SellerProductFormEvent()

    // GROUP
    object AddVariantGroup : SellerProductFormEvent()
    data class RemoveVariantGroup(val index: Int) : SellerProductFormEvent()
    data class ChangeVariantGroupName(val index: Int, val value: String) : SellerProductFormEvent()

    // OPTION
    data class AddVariantOption(val groupIndex: Int) : SellerProductFormEvent()
    data class RemoveVariantOption(val groupIndex: Int, val optionIndex: Int) : SellerProductFormEvent()
    data class ChangeVariantOptionName(val groupIndex: Int, val optionIndex: Int, val value: String) : SellerProductFormEvent()
    data class ChangeVariantOptionPrice(val groupIndex: Int, val optionIndex: Int, val value: String) : SellerProductFormEvent()
}

sealed class SellerProductFormEffect {
    object NavigateBack : SellerProductFormEffect()
    object SaveSuccess : SellerProductFormEffect()
    object DeleteSuccess : SellerProductFormEffect()
}