/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: RequestMapper.kt
 *
 * Last modified by Dedy Wijaya on 28/03/26 23.55
 */

package com.mtv.app.shopme.data.mapper

import com.mtv.app.shopme.data.remote.request.AddressAddRequest
import com.mtv.app.shopme.data.remote.request.CafeAddRequest
import com.mtv.app.shopme.data.remote.request.CafeAddressUpsertRequest
import com.mtv.app.shopme.data.remote.request.CartQuantityRequest
import com.mtv.app.shopme.data.remote.request.ChangePasswordRequest
import com.mtv.app.shopme.data.remote.request.ChangePinRequest
import com.mtv.app.shopme.data.remote.request.CreateOrderRequest
import com.mtv.app.shopme.data.remote.request.CustomerUpdateRequest
import com.mtv.app.shopme.data.remote.request.FoodAddToCartRequest
import com.mtv.app.shopme.data.remote.request.FoodImageRequest
import com.mtv.app.shopme.data.remote.request.FoodOptionRequest
import com.mtv.app.shopme.data.remote.request.FoodUpsertRequest
import com.mtv.app.shopme.data.remote.request.FoodVariantRequest
import com.mtv.app.shopme.data.remote.request.FoodVariantAddToCartRequest
import com.mtv.app.shopme.data.remote.request.ForgotPasswordRequest
import com.mtv.app.shopme.data.remote.request.LoginRequest
import com.mtv.app.shopme.data.remote.request.NotificationPreferencesRequest
import com.mtv.app.shopme.data.remote.request.ResetPasswordRequest
import com.mtv.app.shopme.data.remote.request.RegisterRequest
import com.mtv.app.shopme.data.remote.request.SellerPaymentMethodRequest
import com.mtv.app.shopme.data.remote.request.VerifyOtpRequest
import com.mtv.app.shopme.data.remote.request.VerifyPinRequest
import com.mtv.app.shopme.domain.param.AddressAddParam
import com.mtv.app.shopme.domain.param.CafeAddParam
import com.mtv.app.shopme.domain.param.CafeAddressUpsertParam
import com.mtv.app.shopme.domain.param.CartAddParam
import com.mtv.app.shopme.domain.param.CartQuantityParam
import com.mtv.app.shopme.domain.param.ChangePasswordParam
import com.mtv.app.shopme.domain.param.ChangePinParam
import com.mtv.app.shopme.domain.param.CreateOrderParam
import com.mtv.app.shopme.domain.param.CustomerUpdateParam
import com.mtv.app.shopme.domain.param.ForgotPasswordParam
import com.mtv.app.shopme.domain.param.FoodUpsertParam
import com.mtv.app.shopme.domain.param.LoginParam
import com.mtv.app.shopme.domain.param.NotificationPreferencesParam
import com.mtv.app.shopme.domain.param.ResetPasswordParam
import com.mtv.app.shopme.domain.param.RegisterParam
import com.mtv.app.shopme.domain.param.SellerPaymentMethodParam
import com.mtv.app.shopme.domain.param.VerifyOtpParam
import com.mtv.app.shopme.domain.param.VerifyPinParam

fun CafeAddParam.toRequest() = CafeAddRequest(
    name = name,
    phone = phone,
    description = description,
    minimalOrder = minimalOrder,
    openTime = openTime,
    closeTime = closeTime,
    image = image,
    isActive = isActive
)

fun CafeAddressUpsertParam.toRequest() = CafeAddressUpsertRequest(
    cafeId = cafeId,
    villageId = villageId,
    block = block,
    rt = rt,
    rw = rw,
    number = number
)

fun FoodUpsertParam.toRequest() = FoodUpsertRequest(
    cafeId = cafeId,
    image = images.takeIf { it.isNotEmpty() }?.map { FoodImageRequest(image = it) },
    name = name,
    description = description,
    category = category,
    estimate = estimate,
    isActive = isActive,
    price = price,
    quantity = quantity,
    status = status,
    variant = variants.takeIf { it.isNotEmpty() }?.map { variant ->
        FoodVariantRequest(
            name = variant.name,
            option = variant.options.takeIf { it.isNotEmpty() }?.map { option ->
                FoodOptionRequest(
                    name = option.name,
                    price = option.price
                )
            }
        )
    }
)

fun NotificationPreferencesParam.toRequest() = NotificationPreferencesRequest(
    orderNotification = orderNotification,
    promoNotification = promoNotification,
    chatNotification = chatNotification,
    pushEnabled = pushEnabled,
    emailEnabled = emailEnabled
)

fun SellerPaymentMethodParam.toRequest() = SellerPaymentMethodRequest(
    cashEnabled = cashEnabled,
    bankEnabled = bankEnabled,
    bankNumber = bankNumber,
    ovoEnabled = ovoEnabled,
    ovoNumber = ovoNumber,
    danaEnabled = danaEnabled,
    danaNumber = danaNumber,
    gopayEnabled = gopayEnabled,
    gopayNumber = gopayNumber
)

fun VerifyOtpParam.toRequest() = VerifyOtpRequest(
    email = email,
    otp = otp
)

fun ResetPasswordParam.toRequest() = ResetPasswordRequest(
    email = email,
    newPassword = newPassword
)

fun VerifyPinParam.toRequest() = VerifyPinRequest(
    token = token,
    pin = pin
)

fun ChangePinParam.toRequest() = ChangePinRequest(
    oldPin = oldPin,
    newPin = newPin
)

fun ChangePasswordParam.toRequest() = ChangePasswordRequest(
    currentPassword = currentPassword,
    newPassword = newPassword
)

fun ForgotPasswordParam.toRequest() = ForgotPasswordRequest(
    email = email
)

fun CreateOrderParam.toRequest() = CreateOrderRequest(
    cartId = cartId,
    payment = payment,
    token = token
)

fun CartQuantityParam.toRequest() = CartQuantityRequest(
    quantity = quantity
)

fun CartAddParam.toRequest() = FoodAddToCartRequest(
    foodId = foodId,
    variants = variants?.map {
        FoodVariantAddToCartRequest(
            variantId = it.variantId,
            optionId = it.optionId
        )
    },
    quantity = quantity,
    note = note
)

fun CustomerUpdateParam.toRequest() = CustomerUpdateRequest(
    name = name,
    phone = phone,
    photo = photo,
    fcmToken = fcmToken
)

fun AddressAddParam.toRequest() = AddressAddRequest(
    villageId = villageId,
    block = block,
    number = number,
    rt = rt,
    rw = rw,
    isDefault = isDefault
)

fun LoginParam.toRequest() = LoginRequest(
    email = email,
    password = password
)

fun RegisterParam.toRequest() = RegisterRequest(
    name = name,
    email = email,
    password = password
)
