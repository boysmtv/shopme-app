package com.mtv.app.shopme.feature.firebase.di

import com.google.firebase.firestore.FirebaseFirestore
import com.mtv.app.shopme.feature.firebase.firestore.FirestoreChatSync
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirestoreModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirestoreChatSync(
        firestoreChatSync: FirestoreChatSync
    ): FirestoreChatSync = firestoreChatSync
}
