/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AppConfigUpsertRequest.kt
 *
 * Last modified by Dedy Wijaya on 01/06/26
 */

package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class AppConfigUpsertRequest(
    val platform: String,
    val minVersion: Long,
    val latestVersion: Long,
    val forceUpdate: Boolean,
    val maintenanceMode: Boolean,
    val maintenanceMessage: String? = null
)
