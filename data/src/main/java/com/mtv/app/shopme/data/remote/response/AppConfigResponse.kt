/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AppConfigResponse.kt
 *
 * Last modified by Dedy Wijaya on 16/04/26 11.15
 */

package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class AppConfigResponse(
    val minVersion: Int,
    val latestVersion: Int,
    val forceUpdate: Boolean,
    val maintenanceMode: Boolean,
    val maintenanceMessage: String? = null
)