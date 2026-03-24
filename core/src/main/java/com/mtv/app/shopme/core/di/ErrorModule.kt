/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ErrorModule.kt
 *
 * Last modified by Dedy Wijaya on 24/03/26 18.56
 */

package com.mtv.app.shopme.core.di

import com.mtv.app.shopme.core.error.DefaultErrorMapper
import com.mtv.app.shopme.core.error.ErrorMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorModule {

    @Binds
    abstract fun bindErrorMapper(
        impl: DefaultErrorMapper
    ): ErrorMapper

}