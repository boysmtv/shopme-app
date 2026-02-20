/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AppAdditionalHeaderModule.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.di

import com.mtv.app.shopme.config.AppAdditionalHeaderProvider
import com.mtv.based.core.network.header.AdditionalHeaderProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppAdditionalHeaderModule {

    @Binds
    abstract fun bindAppAdditionalHeaderProvider(
        impl: AppAdditionalHeaderProvider
    ): AdditionalHeaderProvider
}
