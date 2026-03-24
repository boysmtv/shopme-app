/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeMapper.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 01.07
 */

package com.mtv.app.shopme.data.mapper

import com.mtv.app.shopme.core.database.entity.CustomerEntity
import com.mtv.app.shopme.core.database.entity.FoodEntity
import com.mtv.app.shopme.data.remote.response.AddressResponse
import com.mtv.app.shopme.data.remote.response.CustomerResponse
import com.mtv.app.shopme.data.remote.response.FoodOptionResponse
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.app.shopme.data.remote.response.FoodVariantResponse
import com.mtv.app.shopme.data.remote.response.MenuSummaryResponse
import com.mtv.app.shopme.data.remote.response.StatsResponse
import com.mtv.app.shopme.domain.model.Address
import com.mtv.app.shopme.domain.model.Customer
import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.domain.model.FoodOption
import com.mtv.app.shopme.domain.model.FoodVariant
import com.mtv.app.shopme.domain.model.MenuSummary
import com.mtv.app.shopme.domain.model.Stats

fun CustomerResponse.toDomain(): Customer {
    return Customer(
        name = name.orEmpty(),
        phone = phone.orEmpty(),
        email = email.orEmpty(),
        address = address?.toDomain(),
        photo = photo.orEmpty(),
        verified = verified ?: false,
        stats = stats?.toDomain(),
        menuSummary = menuSummary?.toDomain(),
        updatedAt = updatedAt
    )
}

fun AddressResponse.toDomain(): Address {
    return Address(
        id = id,
        village = village,
        block = block,
        number = number,
        rt = rt,
        rw = rw,
        isDefault = isDefault
    )
}

fun StatsResponse.toDomain(): Stats {
    return Stats(
        totalOrders = totalOrders,
        activeOrders = activeOrders,
        membership = membership
    )
}

fun MenuSummaryResponse.toDomain(): MenuSummary {
    return MenuSummary(
        ordered = ordered,
        cooking = cooking,
        shipping = shipping,
        completed = completed,
        cancelled = cancelled
    )
}

fun FoodResponse.toDomain(): Food {
    return Food(
        id = id,
        cafeId = cafeId,
        name = name,
        cafeName = cafeName,
        cafeAddress = cafeAddress,
        description = description,
        price = price,
        category = category,
        status = status,
        quantity = quantity,
        estimate = estimate,
        isActive = isActive,
        createdAt = createdAt,
        images = images,
        variants = variants.map { it.toDomain() },
        updatedAt = updatedAt
    )
}

fun FoodVariantResponse.toDomain(): FoodVariant {
    return FoodVariant(
        id = id,
        name = name,
        options = options.map { it.toDomain() }
    )
}

fun FoodOptionResponse.toDomain(): FoodOption {
    return FoodOption(
        id = id,
        name = name,
        price = price
    )
}