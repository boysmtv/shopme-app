/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeLocalMapper.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 19.48
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
import org.threeten.bp.LocalDateTime

fun CustomerEntity.toDomain(): Customer {
    return Customer(
        name = name,
        phone = EMPTY_STRING,
        email = EMPTY_STRING,
        address = null,
        photo = photo,
        verified = true,
        stats = null,
        menuSummary = null,
        updatedAt = 0L
    )
}

fun FoodEntity.toDomain(): Food {
    return Food(
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
        variants = emptyList(),
        updatedAt = 0L
    )
}

fun Customer.toEntity(): CustomerEntity {
    return CustomerEntity(
        id = phone.ifEmpty { name },
        name = name,
        address = address?.let {
            "${it.village} Blok ${it.block} No ${it.number}"
        }.orEmpty(),
        photo = photo,
        updatedAt = 0L
    )
}

fun Food.toEntity(): FoodEntity {
    return FoodEntity(
        id = id,
        name = name,
        price = price.toDouble(),
        image = images.firstOrNull().orEmpty(),
        isActive = isActive
    )
}