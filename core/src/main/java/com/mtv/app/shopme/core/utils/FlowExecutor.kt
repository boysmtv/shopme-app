package com.mtv.app.shopme.core.utils

import com.mtv.app.shopme.domain.model.Resource
import com.mtv.based.core.network.dispatcher.CoroutineDispatcherProvider
import com.mtv.based.core.network.dispatcher.DefaultDispatcherProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

inline fun <T> executeFlow(
    dispatcher: CoroutineDispatcherProvider = DefaultDispatcherProvider(),
    crossinline block: suspend () -> T
): Flow<Resource<T>> = flow {
    emit(Resource.Loading)
    try {
        val result = block()
        emit(Resource.Success(result))
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        emit(Resource.Error(e))
    }
}.flowOn(dispatcher.io())
