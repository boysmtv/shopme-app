/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AppFirebaseConfigModule.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.di

import com.mtv.app.shopme.config.AppFirebaseConfigProvider
import com.mtv.based.core.network.config.FirebaseConfigProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppFirebaseConfigModule {

    @Provides
    fun provideFirebaseConfigProvider(): FirebaseConfigProvider = AppFirebaseConfigProvider()
}