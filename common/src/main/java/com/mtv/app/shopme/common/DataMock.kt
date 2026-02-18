/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: DataMock.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 15.30
 */

package com.mtv.app.shopme.common

import com.mtv.app.shopme.data.CustomerModel
import com.mtv.app.shopme.data.FoodItemModel
import com.mtv.app.shopme.data.OrderItemModel
import com.mtv.app.shopme.data.OrderModel
import com.mtv.app.shopme.data.OrderStatus
import com.mtv.app.shopme.data.OwnerCafeModel

val mockFoodList = listOf(
    FoodItemModel(
        id = 1,
        name = "Grilled Cheeseburger",
        desc = "Juicy beef patty, cheddar cheese, lettuce, tomato, and our special sauce.",
        price = 9.99,
        imageUrl = "https://example.com/images/cheeseburger.png",
        cafeId = "cafe1",
        isActive = true
    ),
    FoodItemModel(
        id = 2,
        name = "Fried Chicken Bucket",
        desc = "Crispy golden fried chicken, seasoned with herbs and spices, serves 6 pieces.",
        price = 12.49,
        imageUrl = "https://example.com/images/fried_chicken.png",
        cafeId = "cafe1",
        isActive = false
    ),
    FoodItemModel(
        id = 3,
        name = "Double Beef Burger",
        desc = "Two juicy beef patties, melted cheese, pickles, onions, and a special sauce in a soft bun.",
        price = 10.99,
        imageUrl = "https://example.com/images/double_beef_burger.png",
        cafeId = "cafe1",
        isActive = true
    ),
    FoodItemModel(
        id = 4,
        name = "Hotdog Original",
        desc = "Classic hotdog with ketchup, mustard, and a soft warm bun, perfect snack on the go.",
        price = 7.49,
        imageUrl = "https://example.com/images/hotdog.png",
        cafeId = "cafe1",
        isActive = false
    ),
    FoodItemModel(
        id = 5,
        name = "Pepperoni Pizza Slice",
        desc = "Slice of pizza with mozzarella cheese, tangy tomato sauce, and spicy pepperoni toppings.",
        price = 4.99,
        imageUrl = "https://example.com/images/pepperoni_pizza.png",
        cafeId = "cafe1",
        isActive = true
    ),
    FoodItemModel(
        id = 6,
        name = "BBQ Chicken Pizza",
        desc = "Delicious pizza with BBQ chicken, melted cheese, onions, and a smoky BBQ sauce.",
        price = 11.49,
        imageUrl = "https://example.com/images/bbq_chicken_pizza.png",
        cafeId = "cafe1",
        isActive = false
    ),
    FoodItemModel(
        id = 7,
        name = "Crispy Fries",
        desc = "Golden crispy fries, lightly salted, served hot for the perfect side dish.",
        price = 3.29,
        imageUrl = "https://example.com/images/fries.png",
        cafeId = "cafe1",
        isActive = true
    ),
    FoodItemModel(
        id = 8,
        name = "Spaghetti Bolognese",
        desc = "Classic spaghetti with rich meat sauce, parmesan cheese, and fresh herbs.",
        price = 8.79,
        imageUrl = "https://example.com/images/spaghetti.png",
        cafeId = "cafe1",
        isActive = false
    )
)

// MOCK OWNER + CAFE (1 owner = 1 cafe)
val mockOwnerCafe = OwnerCafeModel(
    ownerId = "owner123",
    ownerName = "John Doe",
    ownerEmail = "owner@example.com",
    ownerPhone = "08129998877",
    ownerProfileImage = "https://example.com/images/owner.png",
    cafeId = "cafe1",
    cafeName = "Fried Chicken Bucket",
    cafeAddress = "Jl. Puri Lestari 12",
    cafePhone = "081233344455",
    cafeOpenTime = "08:00",
    cafeCloseTime = "21:00",
    cafeImageUrl = "https://example.com/images/cafe.png",
    cafeRating = 4.5f
)

// MOCK CUSTOMERS
val mockCustomers = listOf(
    CustomerModel(
        id = "cust001",
        name = "Boy",
        email = "boy@example.com",
        phone = "08158844424",
        defaultAddress = "Puri Lestari - Blok G06/01",
        addresses = listOf("Puri Lestari - Blok G06/01", "Jl. Sudirman 12"),
        profileImageUrl = "https://example.com/images/boy.png"
    ),
    CustomerModel(
        id = "cust002",
        name = "Alice",
        email = "alice@example.com",
        phone = "08129997766",
        defaultAddress = "Jl. Merdeka 10",
        addresses = listOf("Jl. Merdeka 10"),
        profileImageUrl = "https://example.com/images/alice.png"
    )
)