/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SimilarItem.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 22.22
 */

package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class SimilarItem(
    val image: Int,
    val title: String,
    val price: Double
)