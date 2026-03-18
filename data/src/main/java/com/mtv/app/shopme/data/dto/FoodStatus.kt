/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FoodStatus.kt
 *
 * Last modified by Dedy Wijaya on 14/03/26 23.24
 */

package com.mtv.app.shopme.data.dto

import kotlinx.serialization.Serializable

@Serializable
enum class FoodStatus {
    READY, JASTIP, PREORDER, UNKNOWN
}