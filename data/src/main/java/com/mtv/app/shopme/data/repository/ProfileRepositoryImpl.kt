/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ProfileRepositoryImpl.kt
 *
 * Last modified by Dedy Wijaya on 01/04/26 09.29
 */

package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.database.entity.CustomerEntity
import com.mtv.app.shopme.core.error.ErrorMapper
import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toRequest
import com.mtv.app.shopme.data.mapper.toSearchDomain
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.mapper.toEntity
import com.mtv.app.shopme.data.remote.datasource.ProfileRemoteDataSource
import com.mtv.app.shopme.data.sync.OfflineMutationSyncManager
import com.mtv.app.shopme.data.sync.PendingMutationAction
import com.mtv.app.shopme.domain.param.AddressAddParam
import com.mtv.app.shopme.domain.param.AddressDefaultParam
import com.mtv.app.shopme.domain.param.AddressDeleteParam
import com.mtv.app.shopme.domain.param.CustomerUpdateParam
import com.mtv.app.shopme.domain.param.NotificationPreferencesParam
import com.mtv.app.shopme.domain.repository.ProfileRepository
import com.mtv.based.core.network.utils.Resource
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.TimeoutCancellationException

class ProfileRepositoryImpl @Inject constructor(
    private val remote: ProfileRemoteDataSource,
    private val resultFlow: ResultFlowFactory,
    private val homeDao: HomeDao,
    private val errorMapper: ErrorMapper,
    private val syncManager: OfflineMutationSyncManager
) : ProfileRepository {

    override fun getCustomer() =
        flow {
            emit(Resource.Loading)
            val cached = homeDao.getCustomerOnce()?.toDomain()
            if (cached != null) {
                emit(Resource.Success(cached))
            }

            try {
                val remoteCustomer = remote.getCustomer().toDomain()
                homeDao.insertCustomer(remoteCustomer.toEntity())
                emit(Resource.Success(remoteCustomer))
            } catch (throwable: Throwable) {
                if (cached == null) {
                    emit(Resource.Error(errorMapper.map(throwable)))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun updateProfile(param: CustomerUpdateParam) =
        flow {
            emit(Resource.Loading)
            try {
                remote.updateProfile(param)
                patchCustomerCache(param)
                emit(Resource.Success(Unit))
            } catch (throwable: Throwable) {
                if (throwable.isRetryableOfflineWrite()) {
                    syncManager.enqueue(
                        PendingMutationAction.ProfileUpdate(param.toRequest())
                    )
                    patchCustomerCache(param)
                    emit(Resource.Success(Unit))
                } else {
                    emit(Resource.Error(errorMapper.map(throwable)))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun deleteAccount() =
        resultFlow.create {
            remote.deleteAccount()
        }

    override fun getNotificationPreferences() =
        resultFlow.create {
            remote.getNotificationPreferences().toDomain()
        }

    override fun updateNotificationPreferences(param: NotificationPreferencesParam) =
        resultFlow.create {
            remote.updateNotificationPreferences(param).toDomain()
        }

    override fun getAddresses() =
        resultFlow.create {
            remote.getAddresses().map { it.toDomain() }
        }

    override fun getVillages() =
        resultFlow.create {
            remote.getVillages().map { it.toDomain() }
        }

    override fun addAddress(param: AddressAddParam) =
        resultFlow.create {
            remote.addAddress(param)
        }

    override fun deleteAddress(param: AddressDeleteParam) =
        resultFlow.create {
            remote.deleteAddress(param)
        }

    override fun setDefaultAddress(param: AddressDefaultParam) =
        resultFlow.create {
            remote.setDefaultAddress(param)
        }

    override fun getFavoriteFoodIds() =
        resultFlow.create {
            remote.getFavoriteFoodIds()
        }

    override fun getFavoriteFoods() =
        resultFlow.create {
            remote.getFavoriteFoods().map { it.toSearchDomain() }
        }

    override fun addFavoriteFood(foodId: String) =
        resultFlow.create {
            remote.addFavoriteFood(foodId)
        }

    override fun removeFavoriteFood(foodId: String) =
        resultFlow.create {
            remote.removeFavoriteFood(foodId)
        }

    private suspend fun patchCustomerCache(param: CustomerUpdateParam) {
        homeDao.getCustomerOnce()?.let { cached ->
            homeDao.insertCustomer(
                cached.copy(
                    name = param.name,
                    phone = param.phone,
                    photo = param.photo ?: cached.photo,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    private fun Throwable.isRetryableOfflineWrite(): Boolean =
        this is IOException || this is TimeoutCancellationException
}
