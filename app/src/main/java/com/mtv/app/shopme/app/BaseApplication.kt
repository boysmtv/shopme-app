/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AppNavGraph.kt
 *
 * Last modified by Dedy Wijaya on 31/12/2025 11.08
 */

package com.mtv.app.shopme.app

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mtv.app.shopme.data.sync.OfflineMutationSyncManager
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application(), ImageLoaderFactory {

    @Inject
    lateinit var offlineMutationSyncManager: OfflineMutationSyncManager

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        applicationScope.launch(Dispatchers.IO) {
            offlineMutationSyncManager.start()
        }
    }

    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(this)
        .crossfade(true)
        .memoryCache {
            MemoryCache.Builder(this)
                .maxSizePercent(0.25)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(cacheDir.resolve("shopme_image_cache"))
                .maxSizePercent(0.03)
                .build()
        }
        .respectCacheHeaders(false)
        .build()
}
