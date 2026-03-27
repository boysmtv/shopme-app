/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: RemoteResource.kt
 *
 * Last modified by Dedy Wijaya on 25/03/26 09.53
 */

package com.mtv.app.shopme.common.utils.network

import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.network.utils.UiError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> remoteResource(
    fetch: suspend () -> T,
    errorMapper: (Throwable) -> UiError
): Flow<Resource<T>> = flow {

    emit(Resource.Loading())

    try {
        val data = fetch()
        emit(Resource.Success(data, isFresh = true))
    } catch (e: Throwable) {
        emit(Resource.Error(errorMapper(e)))
    }

}