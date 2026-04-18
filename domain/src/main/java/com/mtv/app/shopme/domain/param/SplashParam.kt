/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SplashParam.kt
 *
 * Last modified by Dedy Wijaya on 16/04/26 10.14
 */

package com.mtv.app.shopme.domain.param

import com.mtv.based.core.provider.utils.device.DeviceInfo

data class SplashParam(
    val deviceId: String,
    val platform: String,
    val fcmToken: String?,
    val appVersionName: String,
    val appVersionCode: Long,
    val deviceInfo: DeviceInfo,
    val createdAt: String
)