/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProduct.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 12.02
 */

package com.mtv.app.shopme.feature.seller.model

data class SellerProduct(
    val id: String,
    val name: String,
    val price: String,
    val stock: Int,
    val imageUrl: String? = null
)
