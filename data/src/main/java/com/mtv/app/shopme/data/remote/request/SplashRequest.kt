/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SplashRequest.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 14.25
 */

package com.mtv.app.shopme.data.remote.request

import com.mtv.app.core.provider.utils.device.DeviceInfo
import kotlinx.serialization.Serializable

@Serializable
data class SplashRequest(
    var deviceId: String,
    var platform: String,
    var fcmToken: String?,
    var appVersionName: String,
    var appVersionCode: Long,
    var deviceInfo: DeviceInfo,
    var createdAt: String,
)