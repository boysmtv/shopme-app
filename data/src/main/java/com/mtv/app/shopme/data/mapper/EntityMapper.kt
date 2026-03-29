/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: EntityMapper.kt
 *
 * Last modified by Dedy Wijaya on 28/03/26 23.55
 */

package com.mtv.app.shopme.data.mapper

import com.mtv.app.shopme.core.database.entity.CustomerEntity
import com.mtv.app.shopme.core.database.entity.FoodEntity
import com.mtv.app.shopme.domain.model.Customer
import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.domain.model.FoodCategory
import com.mtv.app.shopme.domain.model.FoodStatus
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING
import java.math.BigDecimal
import kotlin.text.ifEmpty
import org.threeten.bp.LocalDateTime

/* =========================================================
 * ENTITY → DOMAIN
 * ========================================================= */

fun CustomerEntity.toDomain(): Customer = Customer(
    name = name,
    phone = EMPTY_STRING,
    email = EMPTY_STRING,
    address = null,
    photo = photo,
    verified = true,
    stats = null,
    menuSummary = null
)

fun FoodEntity.toDomain(): Food = Food(
    id = id,
    cafeId = EMPTY_STRING,
    name = name,
    cafeName = EMPTY_STRING,
    cafeAddress = EMPTY_STRING,
    description = EMPTY_STRING,
    price = BigDecimal.valueOf(price),
    category = FoodCategory.FOOD,
    status = FoodStatus.UNKNOWN,
    quantity = 0,
    estimate = EMPTY_STRING,
    isActive = isActive,
    createdAt = LocalDateTime.now(),
    images = listOf(image),
    variants = emptyList()
)

/* =========================================================
 * DOMAIN → ENTITY
 * ========================================================= */

fun Customer.toEntity(): CustomerEntity = CustomerEntity(
    id = phone.ifEmpty { name },
    name = name,
    address = address?.let {
        "${it.village} Blok ${it.block} No ${it.number}"
    }.orEmpty(),
    photo = photo,
    updatedAt = 0L
)

fun Food.toEntity(): FoodEntity = FoodEntity(
    id = id,
    name = name,
    price = price.toDouble(),
    image = images.firstOrNull().orEmpty(),
    isActive = isActive
)