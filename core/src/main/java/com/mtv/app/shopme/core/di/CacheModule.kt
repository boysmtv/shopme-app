/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CacheModule.kt
 *
 * Last modified by Dedy Wijaya on 24/03/26 18.53
 */

package com.mtv.app.shopme.core.di

import com.mtv.app.shopme.core.cache.CachePolicy
import com.mtv.app.shopme.core.cache.DefaultCachePolicy
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CacheModule {

    @Binds
    abstract fun bindCachePolicy(
        impl: DefaultCachePolicy
    ): CachePolicy

}