/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: PaymentMethod.kt
 *
 * Last modified by Dedy Wijaya on 25/03/26 15.42
 */

package com.mtv.app.shopme.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class PaymentMethod {
    CASH,
    TRANSFER
}
