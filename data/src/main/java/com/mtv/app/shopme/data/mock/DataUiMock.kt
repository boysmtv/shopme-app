/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomePreview.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 21.14
 */

package com.mtv.app.shopme.data.mock

import com.mtv.app.shopme.domain.model.Address
import com.mtv.app.shopme.domain.model.Cafe
import com.mtv.app.shopme.domain.model.CafeAddress
import com.mtv.app.shopme.domain.model.Cart
import com.mtv.app.shopme.domain.model.CartVariant
import com.mtv.app.shopme.domain.model.Customer
import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.domain.model.FoodCategory
import com.mtv.app.shopme.domain.model.FoodOption
import com.mtv.app.shopme.domain.model.FoodStatus
import com.mtv.app.shopme.domain.model.FoodVariant
import com.mtv.app.shopme.domain.model.MenuSummary
import com.mtv.app.shopme.domain.model.Stats
import java.math.BigDecimal
import org.threeten.bp.LocalDateTime

object DataUiMock {
    fun customer() = Customer(
        name = "Dedy Wijaya",
        phone = "08123456789",
        email = "boys.mtv@gmail.com",
        address = Address(
            id = "1",
            village = "Puri Lestari",
            block = "H2",
            number = "21",
            rt = "012",
            rw = "002",
            isDefault = true
        ),
        photo = "https://picsum.photos/200",
        verified = true,
        stats = Stats(
            totalOrders = 120,
            activeOrders = 3,
            membership = "Gold"
        ),
        menuSummary = MenuSummary(
            ordered = 10,
            cooking = 2,
            shipping = 1,
            completed = 7,
            cancelled = 0
        )
    )

    fun foods(): List<Food> = List(6) { index ->
        Food(
            id = index.toString(),
            cafeId = "cafe_$index",
            name = "Burger Special $index",
            cafeName = "Cafe Mantap",
            cafeAddress = "Jakarta Selatan",
            description = "Burger enak dengan daging premium",
            price = BigDecimal(25000 + (index * 5000)),
            category = FoodCategory.FOOD,
            status = FoodStatus.READY,
            quantity = 10,
            estimate = "15-20 menit",
            isActive = index % 2 == 0,
            createdAt = LocalDateTime.now(),
            images = listOf("https://picsum.photos/200?random=$index"),

            variants = listOf(
                FoodVariant(
                    id = "variant_size",
                    name = "Size",
                    options = listOf(
                        FoodOption(
                            id = "opt_small",
                            name = "Small",
                            price = BigDecimal(0)
                        ),
                        FoodOption(
                            id = "opt_medium",
                            name = "Medium",
                            price = BigDecimal(3000)
                        ),
                        FoodOption(
                            id = "opt_large",
                            name = "Large",
                            price = BigDecimal(5000)
                        )
                    )
                ),
                FoodVariant(
                    id = "variant_extra",
                    name = "Extra",
                    options = listOf(
                        FoodOption(
                            id = "opt_cheese",
                            name = "Extra Cheese",
                            price = BigDecimal(4000)
                        ),
                        FoodOption(
                            id = "opt_sauce",
                            name = "Extra Sauce",
                            price = BigDecimal(2000)
                        )
                    )
                )
            )
        )
    }

    fun cafe() = Cafe(
        id = "1",
        customerId = "1",
        name = "Cafe Santuy",
        phone = "08123456789",
        description = "Cafe enak buat nongkrong santai",
        minimalOrder = BigDecimal(10000),
        openTime = "08:00",
        closeTime = "22:00",
        image = "",
        isActive = true,
        createdAt = "",
        address = CafeAddress(
            id = "1",
            name = "Bekasi Timur",
            block = "A",
            number = "12",
            rt = "01",
            rw = "05"
        )
    )

    fun cart() = listOf(
        Cart(
            id = "1",
            customerId = "cust_001",
            foodId = "food_001",
            quantity = 2,
            notes = "Extra cheese",
            cafeId = "cafe_1",
            cafeName = "Burger Queen",
            price = 12000.toBigDecimal(),
            image = "https://example.com/images/burger.png",
            name = "Classic Burger",
            variants = listOf(
                CartVariant(
                    variantId = "v1",
                    variantName = "Size",
                    optionId = "o1",
                    optionName = "Large",
                    price = 3000.toBigDecimal()
                ),
                CartVariant(
                    variantId = "v2",
                    variantName = "Cheese",
                    optionId = "o2",
                    optionName = "Extra Cheese",
                    price = 2000.toBigDecimal()
                )
            )
        ),

        Cart(
            id = "2",
            customerId = "cust_001",
            foodId = "food_002",
            quantity = 1,
            notes = "Less sauce",
            cafeId = "cafe_1",
            cafeName = "Burger Queen",
            price = 15000.toBigDecimal(),
            image = "https://example.com/images/cheese_burger.png",
            name = "Cheese Burger",
            variants = listOf(
                CartVariant(
                    variantId = "v1",
                    variantName = "Size",
                    optionId = "o1",
                    optionName = "Medium",
                    price = 0.toBigDecimal()
                )
            )
        ),

        Cart(
            id = "3",
            customerId = "cust_001",
            foodId = "food_003",
            quantity = 1,
            notes = "Medium spicy",
            cafeId = "cafe_2",
            cafeName = "Pizza Palace",
            price = 30000.toBigDecimal(),
            image = "https://example.com/images/pizza.png",
            name = "Pepperoni Pizza",
            variants = listOf(
                CartVariant(
                    variantId = "v1",
                    variantName = "Crust",
                    optionId = "o1",
                    optionName = "Thin Crust",
                    price = 2000.toBigDecimal()
                )
            )
        ),

        Cart(
            id = "4",
            customerId = "cust_001",
            foodId = "food_004",
            quantity = 2,
            notes = "Extra chili sauce",
            cafeId = "cafe_2",
            cafeName = "Pizza Palace",
            price = 28000.toBigDecimal(),
            image = "https://example.com/images/margherita.png",
            name = "Margherita Pizza",
            variants = listOf(
                CartVariant(
                    variantId = "v1",
                    variantName = "Size",
                    optionId = "o1",
                    optionName = "Large",
                    price = 5000.toBigDecimal()
                )
            )
        ),

        Cart(
            id = "5",
            customerId = "cust_001",
            foodId = "food_005",
            quantity = 2,
            notes = "No sugar",
            cafeId = "cafe_3",
            cafeName = "Coffee Corner",
            price = 18000.toBigDecimal(),
            image = "https://example.com/images/cappuccino.png",
            name = "Cappuccino",
            variants = listOf(
                CartVariant(
                    variantId = "v1",
                    variantName = "Sugar",
                    optionId = "o1",
                    optionName = "No Sugar",
                    price = 0.toBigDecimal()
                )
            )
        ),

        Cart(
            id = "6",
            customerId = "cust_001",
            foodId = "food_006",
            quantity = 1,
            notes = "",
            cafeId = "cafe_3",
            cafeName = "Coffee Corner",
            price = 20000.toBigDecimal(),
            image = "https://example.com/images/latte.png",
            name = "Latte",
            variants = emptyList()
        )
    )

}
