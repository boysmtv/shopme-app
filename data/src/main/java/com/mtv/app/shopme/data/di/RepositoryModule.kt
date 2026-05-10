package com.mtv.app.shopme.data.di

import com.mtv.app.shopme.data.repository.AppRepositoryImpl
import com.mtv.app.shopme.data.repository.AuthRepositoryImpl
import com.mtv.app.shopme.data.repository.CafeRepositoryImpl
import com.mtv.app.shopme.data.repository.CartRepositoryImpl
import com.mtv.app.shopme.data.repository.ChatRepositoryImpl
import com.mtv.app.shopme.data.repository.FoodRepositoryImpl
import com.mtv.app.shopme.data.repository.NotificationRepositoryImpl
import com.mtv.app.shopme.data.repository.OrderRepositoryImpl
import com.mtv.app.shopme.data.repository.ProfileRepositoryImpl
import com.mtv.app.shopme.data.repository.SellerRepositoryImpl
import com.mtv.app.shopme.domain.repository.AppRepository
import com.mtv.app.shopme.domain.repository.AuthRepository
import com.mtv.app.shopme.domain.repository.CafeRepository
import com.mtv.app.shopme.domain.repository.CartRepository
import com.mtv.app.shopme.domain.repository.ChatRepository
import com.mtv.app.shopme.domain.repository.FoodRepository
import com.mtv.app.shopme.domain.repository.NotificationRepository
import com.mtv.app.shopme.domain.repository.OrderRepository
import com.mtv.app.shopme.domain.repository.ProfileRepository
import com.mtv.app.shopme.domain.repository.SellerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository
    @Binds abstract fun bindFoodRepository(impl: FoodRepositoryImpl): FoodRepository
    @Binds abstract fun bindCafeRepository(impl: CafeRepositoryImpl): CafeRepository
    @Binds abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository
    @Binds abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
    @Binds abstract fun bindAppRepository(impl: AppRepositoryImpl): AppRepository
    @Binds abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
    @Binds abstract fun bindOrderRepository(impl: OrderRepositoryImpl): OrderRepository
    @Binds abstract fun bindSellerRepository(impl: SellerRepositoryImpl): SellerRepository
    @Binds abstract fun bindNotificationRepository(impl: NotificationRepositoryImpl): NotificationRepository
}
