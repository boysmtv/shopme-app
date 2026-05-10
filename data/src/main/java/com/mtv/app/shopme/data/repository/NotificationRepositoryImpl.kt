package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toCustomerNotification
import com.mtv.app.shopme.data.mapper.toSellerNotification
import com.mtv.app.shopme.data.remote.datasource.NotificationRemoteDataSource
import com.mtv.app.shopme.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val remote: NotificationRemoteDataSource,
    private val resultFlow: ResultFlowFactory
) : NotificationRepository {

    override fun getCustomerNotifications() =
        resultFlow.create {
            remote.getNotifications().map { it.toCustomerNotification() }
        }

    override fun getSellerNotifications() =
        resultFlow.create {
            remote.getNotifications().map { it.toSellerNotification() }
        }

    override fun clearNotifications() =
        resultFlow.create {
            remote.clearNotifications()
        }
}
