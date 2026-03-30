package com.mtv.app.shopme.data.di

import com.mtv.app.shopme.data.repository.CafeRepositoryImpl
import com.mtv.app.shopme.data.repository.CartRepositoryImpl
import com.mtv.app.shopme.data.repository.ChatRepositoryImpl
import com.mtv.app.shopme.data.repository.FoodDetailRepositoryImpl
import com.mtv.app.shopme.data.repository.HomeRepositoryImpl
import com.mtv.app.shopme.data.repository.SearchRepositoryImpl
import com.mtv.app.shopme.domain.repository.CafeRepository
import com.mtv.app.shopme.domain.repository.CartRepository
import com.mtv.app.shopme.domain.repository.ChatRepository
import com.mtv.app.shopme.domain.repository.FoodDetailRepository
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

    @Binds
    abstract fun bindCafeRepository(
        impl: CafeRepositoryImpl
    ): CafeRepository

    @Binds
    abstract fun bindFoodDetailRepository(
        impl: FoodDetailRepositoryImpl
    ): FoodDetailRepository

    @Binds
    abstract fun bindChatRepository(
        impl: ChatRepositoryImpl
    ): ChatRepository

}