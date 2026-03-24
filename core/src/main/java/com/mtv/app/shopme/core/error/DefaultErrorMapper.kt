/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: DefaultErrorMapper.kt
 *
 * Last modified by Dedy Wijaya on 24/03/26 18.56
 */

package com.mtv.app.shopme.core.error

import com.mtv.based.core.network.utils.UiError
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.TimeoutCancellationException

class DefaultErrorMapper @Inject constructor() : ErrorMapper {

    override fun map(throwable: Throwable): UiError {
        return when (throwable) {

            is IOException -> UiError.Network()

            is TimeoutCancellationException -> UiError.Network(
                message = "Request timeout"
            )

            is ApiException.Unauthorized -> UiError.Unauthorized()

            is ApiException.Forbidden -> UiError.Forbidden()

            is ApiException.ServerError -> UiError.Server()

            is ApiException.Validation -> UiError.Validation(
                message = throwable.message ?: "Validation error"
            )

            is ApiException.EmptyBody -> UiError.Unknown(
                message = "Empty response from server"
            )

            else -> UiError.Unknown()
        }
    }
}