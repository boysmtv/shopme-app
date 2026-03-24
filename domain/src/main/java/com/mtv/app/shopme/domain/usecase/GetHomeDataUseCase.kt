/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: GetHomeDataUseCase.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 19.18
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.model.HomeData
import com.mtv.app.shopme.domain.repository.HomeRepository
import com.mtv.based.core.network.utils.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetHomeDataUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(): Flow<Resource<HomeData>> =
        repository.getHomeData()
}