/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SearchParam.kt
 *
 * Last modified by Dedy Wijaya on 26/03/26 14.13
 */

package com.mtv.app.shopme.domain.param

data class SearchParam(
    val name: String,
    val page: Int = 0,
    val size: Int = 10,
    val sort: String = "name,asc"
)