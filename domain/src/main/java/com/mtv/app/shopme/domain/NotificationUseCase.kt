/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotificationUseCase.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 15.13
 */

package com.mtv.app.shopme.domain

import com.mtv.app.core.provider.based.BaseFirebaseUseCase
import com.mtv.app.shopme.data.response.NotificationData
import com.mtv.based.core.network.datasource.FirebaseDataSource
import com.mtv.based.core.network.di.IoDispatcher
import com.mtv.based.core.network.utils.ResourceFirebase
import com.mtv.based.core.network.utils.mapFirebaseExceptionToUiError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotificationUseCase @Inject constructor(
    private val dataSource: FirebaseDataSource,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : BaseFirebaseUseCase<NotificationData, String>(dispatcher) {

    override fun execute(param: NotificationData): Flow<ResourceFirebase<String>> = flow {
        emit(ResourceFirebase.Loading)
        try {
            val result = dataSource.addDocument(
                collection = "notifications",
                data = mapOf(
                    "title" to param.title,
                    "message" to param.message,
                    "date" to param.date,
                    "isRead" to param.isRead,
                    "uid" to param.uid
                )
            )
            emitAll(result)
        } catch (e: Throwable) {
            emit(ResourceFirebase.Error(mapFirebaseExceptionToUiError(e)))
        }
    }
}
