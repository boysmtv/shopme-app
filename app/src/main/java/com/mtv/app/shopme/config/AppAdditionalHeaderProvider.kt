/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: MainActivity.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.config

import com.mtv.app.core.provider.utils.device.InstallationIdProvider
import com.mtv.app.shopme.common.BuildConfig
import com.mtv.based.core.network.header.AdditionalHeaderProvider
import javax.inject.Inject

class AppAdditionalHeaderProvider @Inject constructor(
    private val installationIdProvider: InstallationIdProvider
) :
    AdditionalHeaderProvider {

    override fun provide(): Map<String, String> =
        mapOf(
            "Authorization" to "Bearer ${BuildConfig.TMDB_BEARER}",
            "X-Device-Id" to installationIdProvider.getInstallationId(),
            "X-App-Name" to "MovieApp",
            "X-Version" to "1.0.0",
            "Accept" to "application/json",
            "Accept-Language" to "id-ID"
        )
}