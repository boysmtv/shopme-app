/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SearchFoodRequest.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 13.56
 */

package com.mtv.app.shopme.data.remote.request

data class SearchFoodRequest(
    val name: String,
    val page: Int = 0,
    val size: Int = 10,
    val sort: String = "name,asc"
)