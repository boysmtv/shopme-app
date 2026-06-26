/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotificationModule.kt
 */

package com.mtv.app.shopme.di

import android.content.Context
import com.mtv.app.shopme.feature.firebase.NotificationRepository
import com.mtv.based.core.provider.utils.SecurePrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    fun provideNotificationRepository(@ApplicationContext context: Context): NotificationRepository {
        return NotificationRepository(SecurePrefs(context))
    }
}
