/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: DataExtension.kt
 *
 * Last modified by Dedy Wijaya on 24/03/26 18.48
 */

package com.mtv.app.shopme.core.data

import kotlinx.coroutines.delay
import java.io.IOException

suspend fun <T> defaultRetryPolicy(
    times: Int = 3,
    initialDelay: Long = 300,
    maxDelay: Long = 2000,
    factor: Double = 2.0,
    block: suspend () -> T
): T {
    var currentDelay = initialDelay
    repeat(times - 1) {
        try {
            return block()
        } catch (e: IOException) {
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        }
    }
    return block()
}