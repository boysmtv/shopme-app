/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: Extension.kt
 *
 * Last modified by Dedy Wijaya on 24/03/26 20.10
 */

package com.mtv.app.shopme.data.utils

import com.mtv.app.shopme.core.error.ApiException
import com.mtv.app.shopme.data.remote.api.ApiResponse

fun <T : Any> ApiResponse<T>.requireData(): T {
    return data ?: throw ApiException.EmptyBody()
}