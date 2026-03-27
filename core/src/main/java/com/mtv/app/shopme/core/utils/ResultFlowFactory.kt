/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ResultFlowFactory.kt
 *
 * Last modified by Dedy Wijaya on 25/03/26 13.17
 */

package com.mtv.app.shopme.core.utils

import com.mtv.app.shopme.core.error.ErrorMapper
import com.mtv.based.core.network.utils.Resource
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ResultFlowFactory @Inject constructor(
    private val errorMapper: ErrorMapper
) {

    fun <T> create(
        block: suspend () -> T
    ): Flow<Resource<T>> = flow {
        emit(Resource.Loading)
        val result = block()
        emit(Resource.Success(result))
    }.catch { throwable ->
        if (throwable is CancellationException) throw throwable
        emit(Resource.Error(errorMapper.map(throwable)))
    }.flowOn(Dispatchers.IO)

    fun createUnit(
        block: suspend () -> Unit
    ): Flow<Resource<Unit>> = create {
        block()
        Unit
    }

}