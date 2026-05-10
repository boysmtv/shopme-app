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
    val user: UserResponse? = null,
    val config: AppConfigResponse? = null,
    val versionStatus: String
)
