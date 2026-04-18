/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: Splash.kt
 *
 * Last modified by Dedy Wijaya on 16/04/26 10.14
 */

package com.mtv.app.shopme.domain.model

data class Splash(
    val isAuthenticated: Boolean,
    val versionStatus: String,
    val user: User?,
    val config: AppConfig
)