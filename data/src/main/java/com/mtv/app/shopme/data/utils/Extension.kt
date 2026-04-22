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

inline fun <reified T : Any> ApiResponse<T>.requireData(): T {
    data?.let { return it }

    if (T::class == Unit::class) {
        @Suppress("UNCHECKED_CAST")
        return Unit as T
    }

    throw ApiException.EmptyBody()
}
