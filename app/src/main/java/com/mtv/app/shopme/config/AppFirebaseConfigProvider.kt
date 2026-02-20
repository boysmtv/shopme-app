/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AppFirebaseConfigProvider.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.config

import com.mtv.app.shopme.common.BuildConfig
import com.mtv.based.core.network.config.FirebaseConfig
import com.mtv.based.core.network.config.FirebaseConfigProvider

class AppFirebaseConfigProvider : FirebaseConfigProvider {

    override fun provide(): FirebaseConfig =
        FirebaseConfig(
            projectId = BuildConfig.FIREBASE_PROJECT_ID,
            defaultCollection = BuildConfig.FIREBASE_USERS_COLLECTION
        )

}