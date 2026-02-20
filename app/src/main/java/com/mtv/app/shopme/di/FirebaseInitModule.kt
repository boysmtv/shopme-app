/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FirebaseInitModule.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.di

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseInitModule {

    @Provides
    @Singleton
    fun provideFirebaseApp(
        @ApplicationContext context: Context
    ): FirebaseApp {
        return FirebaseApp.initializeApp(context)
            ?: throw IllegalStateException("FirebaseApp init failed")
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

}
