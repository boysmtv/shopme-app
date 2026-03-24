/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeMapper.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 00.55
 */

package com.mtv.app.shopme.feature.customer.mapper

import com.mtv.app.shopme.data.remote.response.CustomerResponse
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.app.shopme.domain.model.Customer
import com.mtv.app.shopme.domain.model.Food

fun CustomerResponse.toUi() = Customer(
    name = name.orEmpty(),
    email = email.orEmpty()
)

fun FoodResponse.toUi() = Food(
    id = id,
    name = name,
    price = price.toString(),
    image = images.firstOrNull().orEmpty()
)