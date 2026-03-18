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
import java.math.BigDecimal

fun checkName(customerData: CustomerResponse?): String {
    return customerData?.name ?: "Update your name profile"
}

fun checkPhone(customerData: CustomerResponse?): String {
    return customerData?.phone ?: "Update your phone profile"
}

fun checkPrice(foodResponse: FoodResponse?): String {
    return foodResponse?.price?.toRupiah() ?: BigDecimal.ZERO.toRupiah()
}

fun checkAddress(customerData: CustomerResponse?): String {
    return customerData?.address?.let {
        "${it.village} - Blok ${it.block}/${it.number}"
    } ?: "Update your address profile"
}