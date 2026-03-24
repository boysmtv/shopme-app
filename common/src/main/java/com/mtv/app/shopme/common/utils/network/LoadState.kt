/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: LoadState.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 22.14
 */

package com.mtv.app.shopme.common.utils.network

import com.mtv.based.core.network.utils.UiError

sealed class LoadState<out T> {
    object Idle : LoadState<Nothing>()

    object Loading : LoadState<Nothing>()

    data class Success<T>(
        val data: T,
        val isFresh: Boolean
    ) : LoadState<T>()

    data class Error<T>(
        val error: UiError,
        val data: T? = null
    ) : LoadState<T>()
}