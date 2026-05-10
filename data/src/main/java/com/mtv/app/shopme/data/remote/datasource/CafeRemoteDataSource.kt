/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CafeRemoteDataSource.kt
 *
 * Last modified by Dedy Wijaya on 28/03/26 22.32
 */

package com.mtv.app.shopme.data.remote.datasource

import com.mtv.app.shopme.data.base.BaseRemoteDataSource
import com.mtv.app.shopme.data.mapper.toRequest
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.CafeAddressResponse
import com.mtv.app.shopme.data.remote.response.CafeResponse
import com.mtv.app.shopme.data.utils.requireData
import com.mtv.app.shopme.domain.param.CafeAddParam
import com.mtv.app.shopme.domain.param.CafeAddressUpsertParam
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject

class CafeRemoteDataSource @Inject constructor(
    network: NetworkRepository
) : BaseRemoteDataSource(network) {

    suspend fun getCafe(id: String) = request<ApiResponse<CafeResponse>>(
        endpoint = ApiEndPoint.Cafe.Detail(id)
    ).requireData()

    suspend fun createCafe(param: CafeAddParam) = request<ApiResponse<Unit>>(
        endpoint = ApiEndPoint.Cafe.Create,
        body = param.toRequest()
    ).requireData()

    suspend fun updateCafe(id: String, param: CafeAddParam) = request<ApiResponse<Unit>>(
        endpoint = ApiEndPoint.Cafe.Update(id),
        body = param.toRequest()
    ).requireData()

    suspend fun getCafeAddress(id: String) = request<ApiResponse<CafeAddressResponse>>(
        endpoint = ApiEndPoint.Cafe.Address(id)
    ).requireData()

    suspend fun upsertCafeAddress(param: CafeAddressUpsertParam) = request<ApiResponse<Unit>>(
        endpoint = ApiEndPoint.Cafe.UpsertAddress,
        body = param.toRequest()
    ).requireData()

}
