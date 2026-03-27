/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SearchMapper.kt
 *
 * Last modified by Dedy Wijaya on 26/03/26 14.35
 */

package com.mtv.app.shopme.data.mapper.param

import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.app.shopme.domain.model.SearchFood

fun FoodResponse.toSearchDomain(): SearchFood {
    return SearchFood(
        id = id,
        name = name,
        price = price,
        image = images.firstOrNull().orEmpty(),
        cafeName = cafeName
    )
}