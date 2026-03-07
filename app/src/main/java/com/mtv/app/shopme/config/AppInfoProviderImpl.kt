/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AppInfoProviderImpl.kt
 *
 * Last modified by Dedy Wijaya on 04/03/26 19.36
 */

package com.mtv.app.shopme.config

import com.mtv.app.shopme.BuildConfig
import com.mtv.app.shopme.common.config.AppInfoProvider
import javax.inject.Inject

class AppInfoProviderImpl @Inject constructor() : AppInfoProvider {

    override val versionName: String
        get() = BuildConfig.VERSION_NAME

    override val versionCode: Long
        get() = BuildConfig.VERSION_CODE.toLong()

}