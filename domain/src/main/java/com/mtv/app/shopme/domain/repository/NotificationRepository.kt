package com.mtv.app.shopme.domain.repository

import com.mtv.app.shopme.domain.model.NotificationItem
import com.mtv.app.shopme.domain.model.SellerNotifItem
import com.mtv.based.core.network.utils.Resource
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getCustomerNotifications(): Flow<Resource<List<NotificationItem>>>
    fun getSellerNotifications(): Flow<Resource<List<SellerNotifItem>>>
    fun clearNotifications(): Flow<Resource<Unit>>
}
