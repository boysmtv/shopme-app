/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeData.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 19.19
 */

package com.mtv.app.shopme.domain.model

data class HomeData(
    val customer: Customer,
    val foods: List<Food>
)