package com.mtv.app.shopme.data.remote.datasource

import com.mtv.app.shopme.data.base.BaseRemoteDataSource
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.OrderResponse
import com.mtv.app.shopme.data.utils.requireData
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject

class OrderRemoteDataSource @Inject constructor(
    network: NetworkRepository
) : BaseRemoteDataSource(network) {

    suspend fun getOrders() =
        request<ApiResponse<List<OrderResponse>>>(
            endpoint = ApiEndPoint.Order.GetList
        ).requireData()

    suspend fun getOrderDetail(orderId: String) =
        request<ApiResponse<OrderResponse>>(
            endpoint = ApiEndPoint.Order.Detail(orderId)
        ).requireData()

    suspend fun confirmTransfer(orderId: String) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Order.ConfirmTransfer(orderId)
        ).requireData()
}
