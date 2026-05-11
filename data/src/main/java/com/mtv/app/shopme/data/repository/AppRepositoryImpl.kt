/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AppRepositoryImpl.kt
 *
 * Last modified by Dedy Wijaya on 16/04/26 10.18
 */

package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.remote.datasource.AppRemoteDataSource
import com.mtv.app.shopme.data.remote.request.SplashRequest
import com.mtv.app.shopme.domain.param.SplashParam
import com.mtv.app.shopme.domain.repository.AppRepository
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val remote: AppRemoteDataSource,
    private val resultFlow: ResultFlowFactory
) : AppRepository {

    override fun getSplash(param: SplashParam) =
        resultFlow.create {
            remote.getSplash(
                SplashRequest(
                    deviceId = param.deviceId,
                    fcmToken = param.fcmToken,
                    appVersionName = param.appVersionName,
                    appVersionCode = param.appVersionCode,
                    deviceInfo = param.deviceInfo,
                    platform = param.platform,
                    createdAt = param.createdAt
                )
            ).toDomain()
        }

    override fun getSupportCenter() =
        resultFlow.create {
            remote.getSupportCenter().toDomain()
        }

}
