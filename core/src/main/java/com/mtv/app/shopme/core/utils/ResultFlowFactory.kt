package com.mtv.app.shopme.core.utils

import com.mtv.app.shopme.domain.model.Resource
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ResultFlowFactory @Inject constructor() {

    fun <T> create(
        block: suspend () -> T
    ): Flow<Resource<T>> = flow {
        emit(Resource.Loading)
        val result = block()
        emit(Resource.Success(result))
    }.catch { throwable ->
        emit(Resource.Error(throwable))
    }.flowOn(Dispatchers.IO)

    fun createUnit(
        block: suspend () -> Unit
    ): Flow<Resource<Unit>> = create {
        block()
        Unit
    }

}
