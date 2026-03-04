/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeFoodUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 11.36
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.core.provider.based.BaseUseCase
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.response.HomeFoodResponse
import com.mtv.based.core.network.di.IoDispatcher
import com.mtv.based.core.network.repository.NetworkRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class HomeFoodUseCase @Inject constructor(
    private val repository: NetworkRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : BaseUseCase<Int, HomeFoodResponse>(dispatcher) {

    override suspend fun execute(param: Int) = repository.request<HomeFoodResponse>(
        endpoint = ApiEndPoint.GetHomeFood
    )
}
