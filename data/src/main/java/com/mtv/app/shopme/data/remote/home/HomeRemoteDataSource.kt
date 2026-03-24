/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeRemoteDataSource.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 19.44
 */

package com.mtv.app.shopme.data.remote.home

import com.mtv.app.shopme.data.base.BaseRemoteDataSource
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.CustomerResponse
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.app.shopme.data.utils.requireData
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
    network: NetworkRepository
) : BaseRemoteDataSource(network) {

    suspend fun getCustomer() = request<ApiResponse<CustomerResponse>>(
        endpoint = ApiEndPoint.Customer.Get
    ).requireData()

    suspend fun getFoods() = request<ApiResponse<List<FoodResponse>>>(
        endpoint = ApiEndPoint.Foods.GetAll
    ).requireData()
}