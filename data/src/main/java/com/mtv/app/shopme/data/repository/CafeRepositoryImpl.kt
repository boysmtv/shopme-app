/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CafeRepositoryImpl.kt
 *
 * Last modified by Dedy Wijaya on 28/03/26 22.32
 */

package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.remote.datasource.CafeRemoteDataSource
import com.mtv.app.shopme.domain.repository.CafeRepository
import javax.inject.Inject

class CafeRepositoryImpl @Inject constructor(
    private val remote: CafeRemoteDataSource,
    private val resultFlow: ResultFlowFactory
) : CafeRepository {

    override fun getCafe(id: String) =
        resultFlow.create {
            remote.getCafe(id).toDomain()
        }

    override fun getFoodsByCafe(id: String) =
        resultFlow.create {
            remote.getFoodsByCafe(id).map { it.toDomain() }
        }
}