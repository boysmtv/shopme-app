package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.error.ErrorMapper
import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toCustomerNotification
import com.mtv.app.shopme.data.mapper.toEntity
import com.mtv.app.shopme.data.mapper.toSellerNotification
import com.mtv.app.shopme.data.remote.datasource.NotificationRemoteDataSource
import com.mtv.app.shopme.domain.model.PagedData
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

    override fun getCustomerNotifications(page: Int, size: Int) =
        flow {
            emit(Resource.Loading)
            val useCache = page == 0
            val cached = if (useCache) {
                homeDao.getNotificationsOnce(CUSTOMER_SCOPE).map { it.toCustomerNotification() }
            } else {
                emptyList()
            }
            if (cached.isNotEmpty()) {
                emit(Resource.Success(PagedData(content = cached, page = 0, last = false)))
            }

            try {
                val remoteNotifications = remote.getNotifications(page, size)
                val mapped = remoteNotifications.content.map { it.toCustomerNotification() }
                if (useCache) {
                    homeDao.clearNotifications(CUSTOMER_SCOPE)
                }
                homeDao.insertNotifications(
                    mapped.mapIndexed { index, item ->
                        item.toEntity(CUSTOMER_SCOPE, remoteNotifications.content[index].id)
                    }
                )
                emit(
                    Resource.Success(
                        PagedData(
                            content = mapped,
                            page = remoteNotifications.page,
                            last = remoteNotifications.last
                        )
                    )
                )
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

    override fun getSellerNotifications(page: Int, size: Int) =
        flow {
            emit(Resource.Loading)
            val useCache = page == 0
            val cached = if (useCache) {
                homeDao.getNotificationsOnce(SELLER_SCOPE).map { it.toSellerNotification() }
            } else {
                emptyList()
            }
            if (cached.isNotEmpty()) {
                emit(Resource.Success(PagedData(content = cached, page = 0, last = false)))
            }

            try {
                val remoteNotifications = remote.getNotifications(page, size)
                val mapped = remoteNotifications.content.map { it.toSellerNotification() }
                if (useCache) {
                    homeDao.clearNotifications(SELLER_SCOPE)
                }
                homeDao.insertNotifications(mapped.map { it.toEntity(SELLER_SCOPE) })
                emit(
                    Resource.Success(
                        PagedData(
                            content = mapped,
                            page = remoteNotifications.page,
                            last = remoteNotifications.last
                        )
                    )
                )
            } catch (throwable: Throwable) {
                if (cached.isEmpty()) {
                    emit(Resource.Error(errorMapper.map(throwable)))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun getUnreadCount() =
        resultFlow.create {
            remote.getUnreadCount()
        }

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
