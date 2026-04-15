/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ProductItem.kt
 *
 * Last modified by Dedy Wijaya on 15/04/26 16.02
 */

package com.mtv.app.shopme.domain.model

data class ProductItem(
    val id: String = "",
    val name: String = "",
    val price: String = "",
    val stock: Int = 0,
    val category: String = "",
    val description: String = ""
)
