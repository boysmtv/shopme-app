/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CafeExtension.kt
 *
 * Last modified by Dedy Wijaya on 28/03/26 22.46
 */

package com.mtv.app.shopme.feature.customer.utils

import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.domain.model.FoodStatus
import java.math.BigDecimal

fun Food.displayImage(): String? {
    return images.firstOrNull()
}

fun Food.displayPrice(): BigDecimal {
    return variants
        .firstOrNull()
        ?.options
        ?.firstOrNull()
        ?.price
        ?: price
}

fun Food.isAvailable(): Boolean {
    return isActive && status == FoodStatus.READY
}