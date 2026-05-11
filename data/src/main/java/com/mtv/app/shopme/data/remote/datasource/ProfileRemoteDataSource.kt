/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ProfileRemoteDataSource.kt
 *
 * Last modified by Dedy Wijaya on 01/04/26 09.30
 */

package com.mtv.app.shopme.data.remote.datasource

import com.mtv.app.shopme.data.base.BaseRemoteDataSource
import com.mtv.app.shopme.data.mapper.toRequest
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.AddressResponse
import com.mtv.app.shopme.data.remote.response.CustomerResponse
import com.mtv.app.shopme.data.remote.response.NotificationPreferencesResponse
import com.mtv.app.shopme.data.remote.response.VillageResponse
import com.mtv.app.shopme.data.utils.requireData
import com.mtv.app.shopme.domain.param.AddressAddParam
import com.mtv.app.shopme.domain.param.AddressDefaultParam
import com.mtv.app.shopme.domain.param.AddressDeleteParam
import com.mtv.app.shopme.domain.param.CustomerUpdateParam
import com.mtv.app.shopme.domain.param.NotificationPreferencesParam
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject

class ProfileRemoteDataSource @Inject constructor(
    network: NetworkRepository
) : BaseRemoteDataSource(network) {

    suspend fun getCustomer() = request<ApiResponse<CustomerResponse>>(
        endpoint = ApiEndPoint.Customer.Get
    ).requireData()

    suspend fun updateProfile(param: CustomerUpdateParam) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Customer.Update,
            body = param.toRequest()
        ).requireData()

    suspend fun deleteAccount() =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Customer.Delete
        ).requireData()

    suspend fun getNotificationPreferences() = request<ApiResponse<NotificationPreferencesResponse>>(
        endpoint = ApiEndPoint.Customer.GetNotificationPreferences
    ).requireData()

    suspend fun updateNotificationPreferences(param: NotificationPreferencesParam) =
        request<ApiResponse<NotificationPreferencesResponse>>(
            endpoint = ApiEndPoint.Customer.UpdateNotificationPreferences,
            body = param.toRequest()
        ).requireData()

    suspend fun getAddresses() =
        request<ApiResponse<List<AddressResponse>>>(
            endpoint = ApiEndPoint.Address.Get
        ).requireData()

    suspend fun getVillages() =
        request<ApiResponse<List<VillageResponse>>>(
            endpoint = ApiEndPoint.Village.Get
        ).requireData()

    suspend fun addAddress(param: AddressAddParam) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Address.Create,
            body = param.toRequest()
        ).requireData()

    suspend fun deleteAddress(param: AddressDeleteParam) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Address.Delete(param.id)
        ).requireData()

    suspend fun setDefaultAddress(param: AddressDefaultParam) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Address.SetDefault(param.id)
        ).requireData()

    suspend fun getFavoriteFoodIds() =
        request<ApiResponse<List<String>>>(
            endpoint = ApiEndPoint.Customer.Favorites
        ).requireData()

    suspend fun addFavoriteFood(foodId: String) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Customer.AddFavorite(foodId)
        ).requireData()

    suspend fun removeFavoriteFood(foodId: String) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Customer.RemoveFavorite(foodId)
        ).requireData()

}
