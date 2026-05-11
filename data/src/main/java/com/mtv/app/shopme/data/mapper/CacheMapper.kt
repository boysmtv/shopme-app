package com.mtv.app.shopme.data.mapper

import com.mtv.app.shopme.core.database.entity.FoodEntity
import com.mtv.app.shopme.domain.model.SearchFood
import java.math.BigDecimal

fun SearchFood.toCacheEntity() = FoodEntity(
    id = id,
    name = name,
    price = price.toDouble(),
    image = image,
    cafeName = cafeName,
    isActive = true
)

fun FoodEntity.toSearchDomain() = SearchFood(
    id = id,
    name = name,
    price = BigDecimal.valueOf(price),
    image = image,
    cafeName = cafeName
)
