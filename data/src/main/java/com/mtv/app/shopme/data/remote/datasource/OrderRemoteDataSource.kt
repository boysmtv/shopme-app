package com.mtv.app.shopme.data.remote.datasource

import com.mtv.app.shopme.data.base.BaseRemoteDataSource
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.request.CancelOrderRequest
import com.mtv.app.shopme.data.remote.response.OrderResponse
import com.mtv.app.shopme.data.remote.response.OrderSummaryResponse
import com.mtv.app.shopme.data.utils.requireData
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject

class OrderRemoteDataSource @Inject constructor(
    network: NetworkRepository
) : BaseRemoteDataSource(network) {

    suspend fun getOrders() =
        request<ApiResponse<List<OrderSummaryResponse>>>(
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

    suspend fun cancelOrder(orderId: String, reason: String?) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Order.Cancel(orderId),
            body = CancelOrderRequest(reason = reason?.takeIf { it.isNotBlank() })
        ).requireData()
}
