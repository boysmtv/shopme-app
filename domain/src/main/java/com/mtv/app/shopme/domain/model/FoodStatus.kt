package com.mtv.app.shopme.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class FoodStatus {
    READY, JASTIP, PREORDER, UNKNOWN
}