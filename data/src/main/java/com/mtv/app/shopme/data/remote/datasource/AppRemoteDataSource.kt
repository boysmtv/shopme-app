/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AppRemoteDataSource.kt
 *
 * Last modified by Dedy Wijaya on 16/04/26 11.12
 */

package com.mtv.app.shopme.data.remote.datasource

import com.mtv.app.shopme.data.base.BaseRemoteDataSource
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.request.SplashRequest
import com.mtv.app.shopme.data.remote.response.SplashResponse
import com.mtv.app.shopme.data.remote.response.SupportCenterResponse
import com.mtv.app.shopme.data.utils.requireData
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject

class AppRemoteDataSource @Inject constructor(
    network: NetworkRepository
) : BaseRemoteDataSource(network) {

    suspend fun getSplash(param: SplashRequest) =
        request<ApiResponse<SplashResponse>>(
            endpoint = ApiEndPoint.Misc.Splash,
            body = param
        ).requireData()

    suspend fun getSupportCenter() =
        request<ApiResponse<SupportCenterResponse>>(
            endpoint = ApiEndPoint.Misc.Support
        ).requireData()
}
