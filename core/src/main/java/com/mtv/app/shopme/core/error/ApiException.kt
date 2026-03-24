/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ApiException.kt
 *
 * Last modified by Dedy Wijaya on 24/03/26 18.48
 */

package com.mtv.app.shopme.core.error

sealed class ApiException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause) {

    class Unauthorized : ApiException("Unauthorized")

    class Forbidden : ApiException("Forbidden")

    class ServerError : ApiException("Server error")

    class Validation(message: String) : ApiException(message)

    class EmptyBody : ApiException("Response body is empty")

    class Unknown(cause: Throwable? = null) : ApiException("Unknown error", cause)
}