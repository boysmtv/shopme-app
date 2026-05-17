package com.mtv.app.shopme.data.remote.datasource

import com.mtv.app.shopme.data.base.BaseRemoteDataSource
import com.mtv.app.shopme.data.mapper.toRequest
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.request.CancelOrderRequest
import com.mtv.app.shopme.data.remote.request.SellerAvailabilityRequest
import com.mtv.app.shopme.data.remote.request.SellerPaymentMethodRequest
import com.mtv.app.shopme.data.remote.response.OrderResponse
import com.mtv.app.shopme.data.remote.response.PageResponse
import com.mtv.app.shopme.data.remote.response.SellerPaymentMethodResponse
import com.mtv.app.shopme.data.remote.response.SellerOrderSummaryResponse
import com.mtv.app.shopme.data.remote.response.SellerProfileResponse
import com.mtv.app.shopme.data.utils.requireData
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.domain.param.SellerPaymentMethodParam
import com.mtv.based.core.network.model.RequestOptions
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject

class SellerRemoteDataSource @Inject constructor(
    network: NetworkRepository
) : BaseRemoteDataSource(network) {

    suspend fun getProfile() =
        request<ApiResponse<SellerProfileResponse>>(
            endpoint = ApiEndPoint.Seller.Profile
        ).requireData()

    suspend fun getPaymentMethods() =
        request<ApiResponse<SellerPaymentMethodResponse>>(
            endpoint = ApiEndPoint.Seller.PaymentMethods
        ).requireData()

    suspend fun getOrders() =
        request<ApiResponse<List<SellerOrderSummaryResponse>>>(
            endpoint = ApiEndPoint.Seller.Orders
        ).requireData()

    suspend fun getOrders(page: Int, size: Int) =
        request<ApiResponse<PageResponse<SellerOrderSummaryResponse>>>(
            endpoint = ApiEndPoint.Seller.OrdersPage,
            options = RequestOptions(
                query = mapOf(
                    "page" to page.toString(),
                    "size" to size.toString()
                )
            )
        ).requireData()

    suspend fun getOrderDetail(orderId: String) =
        request<ApiResponse<OrderResponse>>(
            endpoint = ApiEndPoint.Seller.OrderDetail(orderId)
        ).requireData()

    suspend fun updateOrderStatus(orderId: String, status: OrderStatus) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Order.UpdateStatus(orderId, status)
        ).requireData()

    suspend fun cancelOrder(orderId: String, reason: String?) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Order.Cancel(orderId),
            body = CancelOrderRequest(reason = reason?.takeIf { it.isNotBlank() })
        ).requireData()

    suspend fun updateAvailability(isOnline: Boolean) =
        request<ApiResponse<SellerProfileResponse>>(
            endpoint = ApiEndPoint.Seller.Availability,
            body = SellerAvailabilityRequest(isOnline)
        ).requireData()

    suspend fun updateAvailability(body: SellerAvailabilityRequest) =
        request<ApiResponse<SellerProfileResponse>>(
            endpoint = ApiEndPoint.Seller.Availability,
            body = body
        ).requireData()

    suspend fun updatePaymentMethods(param: SellerPaymentMethodParam) =
        request<ApiResponse<SellerPaymentMethodResponse>>(
            endpoint = ApiEndPoint.Seller.UpdatePaymentMethods,
            body = param.toRequest()
        ).requireData()

    suspend fun updatePaymentMethods(body: SellerPaymentMethodRequest) =
        request<ApiResponse<SellerPaymentMethodResponse>>(
            endpoint = ApiEndPoint.Seller.UpdatePaymentMethods,
            body = body
        ).requireData()
}
