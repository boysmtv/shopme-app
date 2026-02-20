/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AppNetworkConfigProvider.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.config

import com.mtv.app.shopme.common.BuildConfig
import com.mtv.based.core.network.config.AppNetworkConfig
import com.mtv.based.core.network.config.NetworkConfig
import com.mtv.based.core.network.config.NetworkConfigProvider

class AppNetworkConfigProvider : NetworkConfigProvider {
    override fun provide(): NetworkConfig =
        AppNetworkConfig(
            baseUrl = BuildConfig.TMDB_URL,
            useKtor = BuildConfig.USE_KTOR,
            isDebug = BuildConfig.DEBUG
        )
}
