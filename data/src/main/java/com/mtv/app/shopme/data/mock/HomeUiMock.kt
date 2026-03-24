/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomePreview.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 21.14
 */

package com.mtv.app.shopme.data.mock

import com.mtv.app.shopme.domain.model.Address
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

object HomeUiMock {
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
}
