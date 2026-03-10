/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: PageResponse.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 13.47
 */

package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class PageResponse<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int,
    val last: Boolean
)