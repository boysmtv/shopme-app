/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FoodCategory.kt
 *
 * Last modified by Dedy Wijaya on 14/03/26 23.24
 */

package com.mtv.app.shopme.data.dto

import kotlinx.serialization.Serializable

@Serializable
enum class FoodCategory(val label: String) {
    FOOD("Makanan"),
    DRINK("Minuman"),
    SNACK("Cemilan"),
    PRODUCT("Barang"),
    SERVICE("Jasa/Layanan"),
    OTHER("Lainnya");

    override fun toString(): String = label
}