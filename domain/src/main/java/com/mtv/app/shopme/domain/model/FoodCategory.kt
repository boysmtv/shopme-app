package com.mtv.app.shopme.domain.model

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