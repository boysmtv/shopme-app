/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FoodResponse.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 13.48
 */

package com.mtv.app.shopme.data.remote.response

import com.mtv.app.shopme.common.serializer.BigDecimalSerializer
import com.mtv.app.shopme.common.serializer.LocalDateTimeSerializer
import com.mtv.app.shopme.domain.model.FoodCategory
import com.mtv.app.shopme.domain.model.FoodStatus
import java.math.BigDecimal
import kotlinx.serialization.Serializable
import org.threeten.bp.LocalDateTime


@Serializable
data class FoodResponse(
    val id: String,
    val cafeId: String,
    val name: String,
    val cafeName: String?,
    val cafeAddress: String?,
    val description: String,

    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal,

    val category: FoodCategory,
    val status: FoodStatus,
    val quantity: Long,
    val estimate: String,
    val isActive: Boolean,

    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,

    val images: List<String>,
    val variants: List<FoodVariantResponse>
)

@Serializable
data class FoodVariantResponse(
    val id: String,
    val name: String,
    val options: List<FoodOptionResponse>
)

@Serializable
data class FoodOptionResponse(
    val id: String,
    val name: String,

    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal
)