package com.mtv.app.shopme.data.di

import android.content.Context
import androidx.room.Room
import com.mtv.app.shopme.core.database.AppDatabase
import com.mtv.app.shopme.core.database.dao.HomeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "shopme.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideHomeDao(database: AppDatabase): HomeDao = database.homeDao()
}
