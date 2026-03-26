/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NetworkBoundResource.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 19.43
 */

package com.mtv.app.shopme.common.utils.network

import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.network.utils.ResourceOffline
import com.mtv.based.core.network.utils.UiError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
inline fun <Result : Any, Request> networkBoundResource(
    crossinline query: () -> Flow<Result?>,
    crossinline fetch: suspend () -> Request,
    crossinline shouldFetch: (Result?) -> Boolean,
    crossinline saveFetchResult: suspend (Request) -> Unit,
    crossinline errorMapper: (Throwable) -> UiError,
    crossinline retryPolicy: suspend (suspend () -> Request) -> Request
): Flow<ResourceOffline<Result>> {

    return query()
        .distinctUntilChanged()
        .flatMapLatest { localData ->
            flow {
                currentCoroutineContext().ensureActive()

                emit(ResourceOffline.Loading(localData))

                if (shouldFetch(localData)) {
                    try {
                        val remote = retryPolicy { fetch() }
                        saveFetchResult(remote)
                    } catch (e: Throwable) {
                        if (e is CancellationException) throw e
                        emit(
                            ResourceOffline.Error(
                                error = errorMapper(e),
                                data = localData
                            )
                        )
                    }
                }

                var first = true

                emitAll(
                    query()
                        .filterNotNull()
                        .distinctUntilChanged()
                        .map { newData ->
                            ResourceOffline.Success(
                                data = newData,
                                isFresh = first.also { first = false }
                            )
                        }
                )
            }
        }
}