package com.mtv.app.shopme.data.sync

import com.mtv.app.shopme.data.remote.request.CartQuantityRequest
import com.mtv.app.shopme.data.remote.request.CustomerUpdateRequest
import com.mtv.app.shopme.data.remote.request.FoodAddToCartRequest
import com.mtv.app.shopme.data.remote.request.SellerAvailabilityRequest
import com.mtv.app.shopme.data.remote.request.SellerPaymentMethodRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class PendingMutationAction {

    @Serializable
    @SerialName("profile_update")
    data class ProfileUpdate(
        val body: CustomerUpdateRequest
    ) : PendingMutationAction()

    @Serializable
    @SerialName("cart_add")
    data class CartAdd(
        val body: FoodAddToCartRequest
    ) : PendingMutationAction()

    @Serializable
    @SerialName("cart_quantity")
    data class CartQuantity(
        val cartId: String,
        val body: CartQuantityRequest
    ) : PendingMutationAction()

    @Serializable
    @SerialName("cart_clear")
    data object CartClear : PendingMutationAction()

    @Serializable
    @SerialName("cart_clear_by_cafe")
    data class CartClearByCafe(
        val cafeId: String
    ) : PendingMutationAction()

    @Serializable
    @SerialName("seller_availability")
    data class SellerAvailability(
        val body: SellerAvailabilityRequest
    ) : PendingMutationAction()

    @Serializable
    @SerialName("seller_payment_methods")
    data class SellerPaymentMethods(
        val body: SellerPaymentMethodRequest
    ) : PendingMutationAction()
}
