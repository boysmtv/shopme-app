/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: RepositoryModule.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 01.11
 */

package com.mtv.app.shopme.di

import com.mtv.app.shopme.data.repository.CartRepositoryImpl
import com.mtv.app.shopme.data.repository.HomeRepositoryImpl
import com.mtv.app.shopme.data.repository.SearchRepositoryImpl
import com.mtv.app.shopme.domain.repository.CartRepository
import com.mtv.app.shopme.domain.repository.HomeRepository
import com.mtv.app.shopme.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindHomeRepository(
        impl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    abstract fun bindCartRepository(
        impl: CartRepositoryImpl
    ): CartRepository

    @Binds
    abstract fun bindSearchRepository(
        impl: SearchRepositoryImpl
    ): SearchRepository

}