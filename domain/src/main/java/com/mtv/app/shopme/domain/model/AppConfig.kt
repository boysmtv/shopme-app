/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AppConfig.kt
 *
 * Last modified by Dedy Wijaya on 16/04/26 10.14
 */

package com.mtv.app.shopme.domain.model

data class AppConfig(
    val minVersion: Int,
    val latestVersion: Int,
    val forceUpdate: Boolean,
    val maintenanceMode: Boolean,
    val maintenanceMessage: String?
)