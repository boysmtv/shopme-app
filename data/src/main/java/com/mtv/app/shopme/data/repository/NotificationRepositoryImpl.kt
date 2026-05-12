package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.error.ErrorMapper
import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toCustomerNotification
import com.mtv.app.shopme.data.mapper.toEntity
import com.mtv.app.shopme.data.mapper.toSellerNotification
import com.mtv.app.shopme.data.remote.datasource.NotificationRemoteDataSource
import com.mtv.app.shopme.domain.repository.NotificationRepository
import com.mtv.based.core.network.utils.Resource
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class NotificationRepositoryImpl @Inject constructor(
    private val remote: NotificationRemoteDataSource,
    private val resultFlow: ResultFlowFactory,
    private val homeDao: HomeDao,
    private val errorMapper: ErrorMapper
) : NotificationRepository {

    override fun getCustomerNotifications() =
        flow {
            emit(Resource.Loading)
            val cached = homeDao.getNotificationsOnce(CUSTOMER_SCOPE).map { it.toCustomerNotification() }
            if (cached.isNotEmpty()) {
                emit(Resource.Success(cached))
            }

            try {
                val remoteNotifications = remote.getNotifications()
                val mapped = remoteNotifications.map { it.toCustomerNotification() }
                homeDao.clearNotifications(CUSTOMER_SCOPE)
                homeDao.insertNotifications(
                    mapped.mapIndexed { index, item ->
                        item.toEntity(CUSTOMER_SCOPE, remoteNotifications[index].id)
                    }
                )
                emit(Resource.Success(mapped))
            } catch (throwable: Throwable) {
                if (cached.isEmpty()) {
                    emit(Resource.Error(errorMapper.map(throwable)))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun getSellerNotifications() =
        flow {
            emit(Resource.Loading)
            val cached = homeDao.getNotificationsOnce(SELLER_SCOPE).map { it.toSellerNotification() }
            if (cached.isNotEmpty()) {
                emit(Resource.Success(cached))
            }

            try {
                val remoteNotifications = remote.getNotifications()
                val mapped = remoteNotifications.map { it.toSellerNotification() }
                homeDao.clearNotifications(SELLER_SCOPE)
                homeDao.insertNotifications(mapped.map { it.toEntity(SELLER_SCOPE) })
                emit(Resource.Success(mapped))
            } catch (throwable: Throwable) {
                if (cached.isEmpty()) {
                    emit(Resource.Error(errorMapper.map(throwable)))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun clearNotifications() =
        resultFlow.create {
            remote.clearNotifications()
            homeDao.clearNotifications(CUSTOMER_SCOPE)
            homeDao.clearNotifications(SELLER_SCOPE)
        }

    companion object {
        private const val CUSTOMER_SCOPE = "customer"
        private const val SELLER_SCOPE = "seller"
    }
}
