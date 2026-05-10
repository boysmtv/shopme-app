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
import com.mtv.app.shopme.domain.param.CafeAddParam
import com.mtv.app.shopme.domain.param.CafeAddressUpsertParam
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

    override fun createCafe(param: CafeAddParam) =
        resultFlow.create {
            remote.createCafe(param)
        }

    override fun updateCafe(id: String, param: CafeAddParam) =
        resultFlow.create {
            remote.updateCafe(id, param)
        }

    override fun getCafeAddress(id: String) =
        resultFlow.create {
            remote.getCafeAddress(id).toDomain()
        }

    override fun upsertCafeAddress(param: CafeAddressUpsertParam) =
        resultFlow.create {
            remote.upsertCafeAddress(param)
        }

}
