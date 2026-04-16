/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AuthRemoteDataSource.kt
 *
 * Last modified by Dedy Wijaya on 16/04/26 11.35
 */

package com.mtv.app.shopme.data.remote.datasource

import com.mtv.app.shopme.data.base.BaseRemoteDataSource
import com.mtv.app.shopme.data.mapper.toRequest
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.LoginResponse
import com.mtv.app.shopme.data.utils.requireData
import com.mtv.app.shopme.domain.param.LoginParam
import com.mtv.app.shopme.domain.param.RegisterParam
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    network: NetworkRepository
) : BaseRemoteDataSource(network) {

    suspend fun login(param: LoginParam) =
        request<ApiResponse<LoginResponse>>(
            endpoint = ApiEndPoint.Auth.Login,
            body = param.toRequest()
        ).requireData()


    suspend fun register(param: RegisterParam) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Auth.Register,
            body = param.toRequest()
        ).requireData()

}