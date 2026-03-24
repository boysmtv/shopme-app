/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ErrorMapper.kt
 *
 * Last modified by Dedy Wijaya on 24/03/26 18.48
 */

package com.mtv.app.shopme.core.error

import com.mtv.based.core.network.utils.UiError

interface ErrorMapper {
    fun map(throwable: Throwable): UiError
}