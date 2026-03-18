/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: DataMock.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 15.30
 */

package com.mtv.app.shopme.data.dto
import java.math.BigDecimal

val mockFoodList = listOf(
    FoodItemModel(
        id = "1",
        name = "Grilled Cheeseburger",
        description = "Juicy beef patty with cheddar cheese.",
        price = BigDecimal("45000"),
        imageUrl = "", // kosong → pakai preview drawable
        cafeName = "Warung Kopi Nusantara",
        cafeAddress = "Puri Lestari - Blok H7/21",
        isActive = true
    ),
    FoodItemModel(
        id = "2",
        name = "Fried Chicken Bucket",
        description = "Crispy fried chicken 6 pcs.",
        price = BigDecimal("75000"),
        imageUrl = "",
        cafeName = "Warung Kopi Nusantara",
        cafeAddress = "Puri Lestari - Blok H7/21",
        isActive = false
    ),
    FoodItemModel(
        id = "3",
        name = "Double Beef Burger",
        description = "Double patty burger with cheese.",
        price = BigDecimal("55000"),
        imageUrl = "",
        cafeName = "Warung Kopi Nusantara",
        cafeAddress = "Puri Lestari - Blok H7/21",
        isActive = true
    )
)

val mockOwnerCafe = OwnerCafeModel(
    id = "62ca866d-49fa-4660-bcda-8cded490dd3e",
    name = "Warung Kopi Nusantara",
    address = "Puri Lestari - Blok H7/21",
    phone = "0813-4455-6677",
    openTime = "06:00",
    closeTime = "23:00",
    minimalOrder = BigDecimal(20000),
    description = "Menyediakan kopi tradisional dan makanan ringan",
    imageUrl = "",
    isActive = true
)