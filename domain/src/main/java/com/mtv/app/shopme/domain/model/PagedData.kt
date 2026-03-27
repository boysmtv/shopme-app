/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: PagedData.kt
 *
 * Last modified by Dedy Wijaya on 26/03/26 13.49
 */

package com.mtv.app.shopme.domain.model

data class PagedData<T>(
    val content: List<T>,
    val page: Int,
    val last: Boolean
)