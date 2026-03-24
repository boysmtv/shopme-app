/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: BaseRemoteDataSource.kt
 *
 * Last modified by Dedy Wijaya on 24/03/26 19.59
 */

package com.mtv.app.shopme.data.base

import com.mtv.app.shopme.core.error.ApiException
import com.mtv.based.core.network.endpoint.IApiEndPoint
import com.mtv.based.core.network.model.NetworkResponse
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject

abstract class BaseRemoteDataSource @Inject constructor(
    val network: NetworkRepository
) {

    protected suspend inline fun <reified R : Any> request(
        endpoint: IApiEndPoint,
        body: Any? = null
    ): R {
        val response: NetworkResponse<R> = network.request(
            endpoint = endpoint,
            body = body
        )

        return response.data
            ?: throw ApiException.EmptyBody()
    }
}