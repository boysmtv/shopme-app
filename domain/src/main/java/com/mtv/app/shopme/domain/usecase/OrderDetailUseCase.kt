/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderDetailUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 13.50
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.core.provider.based.BaseUseCase
import com.mtv.based.core.network.di.IoDispatcher
import com.mtv.based.core.network.model.NetworkResponse
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class OrderDetailUseCase @Inject constructor(
    private val repository: NetworkRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : BaseUseCase<Unit, String>(dispatcher) {

    override suspend fun execute(param: Unit): NetworkResponse<String> {
        TODO("Not yet implemented")
    }

}