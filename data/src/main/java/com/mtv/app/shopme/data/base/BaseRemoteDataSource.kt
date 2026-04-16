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
import com.mtv.based.core.network.model.RequestOptions
import com.mtv.based.core.network.repository.NetworkRepository

abstract class BaseRemoteDataSource(
    protected val network: NetworkRepository
) {

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
        return response.data ?: throw ApiException.EmptyBody()
    }

}