package com.mtv.app.shopme.data.remote.datasource

import com.mtv.app.shopme.data.base.BaseRemoteDataSource
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.AppNotificationResponse
import com.mtv.app.shopme.data.utils.requireData
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject

class NotificationRemoteDataSource @Inject constructor(
    network: NetworkRepository
) : BaseRemoteDataSource(network) {

    suspend fun getNotifications() =
        request<ApiResponse<List<AppNotificationResponse>>>(
            endpoint = ApiEndPoint.Notifications.Get
        ).requireData()

    suspend fun getUnreadCount() =
        request<ApiResponse<Long>>(
            endpoint = ApiEndPoint.Notifications.UnreadCount
        ).requireData().toInt()

    suspend fun clearNotifications() =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Notifications.ReadAll
        ).requireData()
}
