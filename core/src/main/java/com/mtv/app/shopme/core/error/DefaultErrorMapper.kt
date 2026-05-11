/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: DefaultErrorMapper.kt
 *
 * Last modified by Dedy Wijaya on 24/03/26 18.56
 */

package com.mtv.app.shopme.core.error

import com.mtv.based.core.network.utils.ErrorMessages
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

            is ApiException.Unauthorized -> UiError.Unauthorized(
                message = throwable.message ?: ErrorMessages.SESSION_EXPIRED
            )

            is ApiException.Forbidden -> UiError.Forbidden(
                message = throwable.message ?: ErrorMessages.ACCESS_DENIED
            )

            is ApiException.Conflict -> UiError.Validation(
                message = throwable.message ?: "Conflict"
            )

            is ApiException.ServerError -> UiError.Server(
                message = throwable.message ?: ErrorMessages.SERVER_ERROR
            )

            is ApiException.Validation -> UiError.Validation(
                message = throwable.message ?: "Validation error"
            )

            is ApiException.EmptyBody -> UiError.Unknown(
                message = "Empty response from server"
            )

            is ApiException.Unknown -> UiError.Unknown(
                message = throwable.message ?: ErrorMessages.GENERIC_ERROR
            )

            else -> UiError.Unknown(
                message = throwable.message ?: ErrorMessages.GENERIC_ERROR
            )
        }
    }
}
