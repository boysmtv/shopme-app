/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: BaseRemoteDataSource.kt
 *
 * Last modified by Dedy Wijaya on 24/03/26 19.59
 */

package com.mtv.app.shopme.data.base

import com.mtv.app.shopme.core.error.ApiException
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.based.core.network.endpoint.IApiEndPoint
import com.mtv.based.core.network.model.NetworkResponse
import com.mtv.based.core.network.model.RequestOptions
import com.mtv.based.core.network.repository.NetworkRepository
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

abstract class BaseRemoteDataSource(
    protected val network: NetworkRepository
) {

    @PublishedApi
    internal val errorJson = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    protected suspend inline fun <reified R : Any> request(
        endpoint: IApiEndPoint,
        body: Any? = null,
        options: RequestOptions = RequestOptions()
    ): R {
        val response: NetworkResponse<R> = network.request(
            endpoint = endpoint,
            body = body,
            options = options
        )

        if (response.httpCode !in 200..299) {
            throw response.toApiException()
        }

        return response.data ?: throw ApiException.EmptyBody()
    }

    @PublishedApi
    internal fun <R : Any> NetworkResponse<R>.toApiException(): ApiException {
        val parsed = rawBody
            ?.takeIf { it.isNotBlank() }
            ?.let(::parseErrorEnvelope)
        val message = parsed?.message
            ?.takeIf { it.isNotBlank() }
            ?: defaultMessageForStatus(httpCode)
        val errorCode = parsed?.code

        return when (httpCode) {
            401 -> ApiException.Unauthorized(
                statusCode = httpCode,
                errorCode = errorCode,
                message = message
            )

            403 -> ApiException.Forbidden(
                statusCode = httpCode,
                errorCode = errorCode,
                message = message
            )

            409 -> ApiException.Conflict(
                statusCode = httpCode,
                errorCode = errorCode,
                message = message
            )

            in 400..499 -> ApiException.Validation(
                message = message,
                statusCode = httpCode,
                errorCode = errorCode
            )

            in 500..599 -> ApiException.ServerError(
                statusCode = httpCode,
                errorCode = errorCode,
                message = message
            )

            else -> ApiException.Unknown(
                statusCode = httpCode,
                errorCode = errorCode,
                message = message
            )
        }
    }

    @PublishedApi
    internal fun parseErrorEnvelope(rawBody: String): ErrorEnvelope? =
        runCatching {
            errorJson.decodeFromString<ApiResponse<JsonElement?>>(rawBody).let { envelope ->
                ErrorEnvelope(
                    status = envelope.status,
                    code = envelope.code,
                    message = envelope.message
                )
            }
        }.recoverCatching {
            errorJson.decodeFromString<ErrorEnvelope>(rawBody)
        }.getOrNull()

    @PublishedApi
    internal fun defaultMessageForStatus(statusCode: Int): String =
        when (statusCode) {
            401 -> "Unauthorized"
            403 -> "Forbidden"
            409 -> "Conflict"
            in 400..499 -> "Validation error"
            in 500..599 -> "Server error"
            else -> "Unknown error"
        }

    @Serializable
    internal data class ErrorEnvelope(
        val status: Int? = null,
        val code: String? = null,
        val message: String? = null
    )

}
