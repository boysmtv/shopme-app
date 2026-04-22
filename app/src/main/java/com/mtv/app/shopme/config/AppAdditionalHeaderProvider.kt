/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AppAdditionalHeaderProvider.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.config

import com.mtv.app.shopme.common.ConstantPreferences.ACCESS_TOKEN
import com.mtv.based.core.network.header.AdditionalHeaderProvider
import com.mtv.based.core.provider.utils.SecurePrefs
import com.mtv.based.core.provider.utils.device.InstallationIdProvider
import javax.inject.Inject

class AppAdditionalHeaderProvider @Inject constructor(
    private val installationIdProvider: InstallationIdProvider,
    private val securePrefs: SecurePrefs
) : AdditionalHeaderProvider {

    override fun provide(): Map<String, String> =
        mapOf(
            "Authorization" to "Bearer ${securePrefs.getString(ACCESS_TOKEN)}",
            "X-Device-Id" to installationIdProvider.getInstallationId(),
            "X-App-Name" to "Shopme",
            "X-Version" to "1.0.0",
            "Accept" to "application/json",
            "Accept-Language" to "id-ID"
        )
}
