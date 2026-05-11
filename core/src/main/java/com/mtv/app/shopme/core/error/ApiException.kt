/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ApiException.kt
 *
 * Last modified by Dedy Wijaya on 24/03/26 18.48
 */

package com.mtv.app.shopme.core.error

sealed class ApiException(
    open val statusCode: Int? = null,
    open val errorCode: String? = null,
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause) {

    class Unauthorized(
        override val statusCode: Int? = 401,
        override val errorCode: String? = null,
        message: String = "Unauthorized"
    ) : ApiException(statusCode, errorCode, message)

    class Forbidden(
        override val statusCode: Int? = 403,
        override val errorCode: String? = null,
        message: String = "Forbidden"
    ) : ApiException(statusCode, errorCode, message)

    class ServerError(
        override val statusCode: Int? = 500,
        override val errorCode: String? = null,
        message: String = "Server error"
    ) : ApiException(statusCode, errorCode, message)

    class Validation(
        message: String,
        override val statusCode: Int? = null,
        override val errorCode: String? = null
    ) : ApiException(statusCode, errorCode, message)

    class EmptyBody : ApiException(message = "Response body is empty")

    class Unknown(
        override val statusCode: Int? = null,
        override val errorCode: String? = null,
        message: String = "Unknown error",
        cause: Throwable? = null
    ) : ApiException(statusCode, errorCode, message, cause)
}
