package com.mtv.app.shopme.domain.repository

import com.mtv.app.shopme.domain.model.NotificationItem
import com.mtv.app.shopme.domain.model.PagedData
import com.mtv.app.shopme.domain.model.SellerNotifItem
import com.mtv.based.core.network.utils.Resource
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getCustomerNotifications(): Flow<Resource<List<NotificationItem>>>
    fun getCustomerNotifications(page: Int, size: Int): Flow<Resource<PagedData<NotificationItem>>>
    fun getSellerNotifications(): Flow<Resource<List<SellerNotifItem>>>
    fun getSellerNotifications(page: Int, size: Int): Flow<Resource<PagedData<SellerNotifItem>>>
    fun getUnreadCount(): Flow<Resource<Int>>
    fun clearNotifications(): Flow<Resource<Unit>>
    fun markNotificationRead(id: String): Flow<Resource<Unit>>
    fun deleteNotification(id: String): Flow<Resource<Unit>>
}
