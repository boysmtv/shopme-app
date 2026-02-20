/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: MainActivity.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.di

import com.mtv.app.shopme.config.AppNetworkConfigProvider
import com.mtv.based.core.network.config.NetworkConfig
import com.mtv.based.core.network.config.NetworkConfigProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppNetworkConfigModule {

    @Provides
    @Singleton
    fun provideNetworkConfigProvider(): NetworkConfigProvider = AppNetworkConfigProvider()

    @Provides
    @Singleton
    fun provideNetworkConfig(
        provider: NetworkConfigProvider
    ): NetworkConfig =
        provider.provide()

}
