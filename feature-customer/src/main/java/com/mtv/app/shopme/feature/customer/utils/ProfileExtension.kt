/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ProfileExtension.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 23.45
 */

package com.mtv.app.shopme.feature.customer.utils

import com.mtv.app.shopme.common.toRupiah
import com.mtv.app.shopme.data.remote.response.CustomerResponse
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.app.shopme.domain.model.Customer
import com.mtv.app.shopme.domain.model.Food
import java.math.BigDecimal

fun checkName(customerData: Customer?): String {
    return customerData?.name ?: "Update your name profile"
}

fun checkPhone(customerData: Customer?): String {
    return customerData?.phone ?: "Update your phone profile"
}

fun checkPrice(foodResponse: Food?): String {
    return foodResponse?.price?.toRupiah() ?: BigDecimal.ZERO.toRupiah()
}

fun checkAddress(customerData: Customer?): String {
    return customerData?.address?.let {
        "${it.village} - Blok ${it.block}/${it.number}"
    } ?: "Update your address profile"
}