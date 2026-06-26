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
import com.mtv.based.core.provider.utils.device.DeviceInfoProvider
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val remote: AppRemoteDataSource,
    private val resultFlow: ResultFlowFactory,
    private val deviceInfoProvider: DeviceInfoProvider
) : AppRepository {

    override fun getSplash(param: SplashParam) =
        resultFlow.create {
            remote.getSplash(
                SplashRequest(
                    deviceId = param.deviceId,
                    fcmToken = param.fcmToken,
                    appVersionName = param.appVersionName,
                    appVersionCode = param.appVersionCode,
                    deviceInfo = deviceInfoProvider.getAllDeviceInfo(),
                    platform = param.platform,
                    createdAt = param.createdAt
                )
            ).toDomain()
        }

    override fun getSupportCenter() =
        resultFlow.create {
            remote.getSupportCenter().toDomain()
        }

    override fun getSupportChat() =
        resultFlow.create {
            remote.getSupportChat().toDomain()
        }

    override fun sendSupportChatMessage(message: String) =
        resultFlow.create {
            remote.sendSupportChatMessage(message).toDomain()
        }

}
