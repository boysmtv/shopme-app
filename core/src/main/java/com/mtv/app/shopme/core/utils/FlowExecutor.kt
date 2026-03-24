package com.mtv.app.shopme.core.utils

import com.mtv.based.core.network.dispatcher.CoroutineDispatcherProvider
import com.mtv.based.core.network.dispatcher.DefaultDispatcherProvider
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.network.utils.toUiError
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
        emit(Resource.Error(e.toUiError()))
    }
}.flowOn(dispatcher.io())