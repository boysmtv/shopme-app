/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SplashResponse.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 00.59
 */

package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class SplashResponse(
    val isAuthenticated: Boolean,
    val user: User? = null,
    val config: AppConfig? = null,
    val versionStatus: String
)

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val photo: String
)

@Serializable
data class AppConfig(
    val minVersion: Int,
    val latestVersion: Int,
    val forceUpdate: Boolean,
    val maintenanceMode: Boolean,
    val maintenanceMessage: String? = null
)