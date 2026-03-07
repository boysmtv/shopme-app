/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AppInfoModule.kt
 *
 * Last modified by Dedy Wijaya on 04/03/26 19.37
 */

package com.mtv.app.shopme.di

import com.mtv.app.shopme.common.config.AppInfoProvider
import com.mtv.app.shopme.config.AppInfoProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class AppInfoModule {

    @Binds
    abstract fun bindAppInfoProvider(
        impl: AppInfoProviderImpl
    ): AppInfoProvider

}