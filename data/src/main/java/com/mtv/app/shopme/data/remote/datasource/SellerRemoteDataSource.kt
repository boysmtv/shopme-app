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
import com.mtv.app.shopme.data.remote.response.SellerDashboardResponse
import com.mtv.app.shopme.data.remote.response.DiscountResponse
import com.mtv.app.shopme.data.remote.response.ReviewResponse
import com.mtv.app.shopme.data.remote.response.SellerCategoryResponse
import com.mtv.app.shopme.data.remote.response.SellerPaymentMethodResponse
import com.mtv.app.shopme.data.remote.response.SellerOrderSummaryResponse
import com.mtv.app.shopme.data.remote.response.SellerProfileResponse
import com.mtv.app.shopme.data.remote.request.DiscountRequest
import com.mtv.app.shopme.data.remote.request.SellerCategoryRequest
import com.mtv.app.shopme.data.utils.requireData
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.domain.param.SellerPaymentMethodParam
import com.mtv.based.core.network.model.RequestOptions
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject

class SellerRemoteDataSource @Inject constructor(
    network: NetworkRepository
) : BaseRemoteDataSource(network) {

    suspend fun getDashboard() =
        request<ApiResponse<SellerDashboardResponse>>(
            endpoint = ApiEndPoint.Seller.Dashboard
        ).requireData()

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

    suspend fun getOrders(page: Int, size: Int, status: OrderStatus? = null) =
        request<ApiResponse<PageResponse<SellerOrderSummaryResponse>>>(
            endpoint = ApiEndPoint.Seller.OrdersPage,
            options = RequestOptions(
                query = buildMap {
                    put("page", page.toString())
                    put("size", size.toString())
                    status?.let { put("status", it.name) }
                }
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

    suspend fun getDiscounts() =
        request<ApiResponse<List<DiscountResponse>>>(
            endpoint = ApiEndPoint.Seller.Discounts
        ).requireData()

    suspend fun createDiscount(request: DiscountRequest) =
        request<ApiResponse<DiscountResponse>>(
            endpoint = ApiEndPoint.Seller.CreateDiscount,
            body = request
        ).requireData()

    suspend fun updateDiscount(discountId: String, request: DiscountRequest) =
        request<ApiResponse<DiscountResponse>>(
            endpoint = ApiEndPoint.Seller.UpdateDiscount(discountId),
            body = request
        ).requireData()

    suspend fun deleteDiscount(discountId: String) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Seller.DeleteDiscount(discountId)
        ).requireData()

    suspend fun getReviews() =
        request<ApiResponse<List<ReviewResponse>>>(
            endpoint = ApiEndPoint.Seller.Reviews
        ).requireData()

    suspend fun replyToReview(reviewId: String, request: ReviewReplyRequest) =
        request<ApiResponse<ReviewResponse>>(
            endpoint = ApiEndPoint.Seller.ReplyReview(reviewId),
            body = request
        ).requireData()

    suspend fun getCategories() =
        request<ApiResponse<List<SellerCategoryResponse>>>(
            endpoint = ApiEndPoint.Seller.Categories
        ).requireData()

    suspend fun createCategory(request: SellerCategoryRequest) =
        request<ApiResponse<SellerCategoryResponse>>(
            endpoint = ApiEndPoint.Seller.CreateCategory,
            body = request
        ).requireData()

    suspend fun updateCategory(categoryId: String, request: SellerCategoryRequest) =
        request<ApiResponse<SellerCategoryResponse>>(
            endpoint = ApiEndPoint.Seller.UpdateCategory(categoryId),
            body = request
        ).requireData()

    suspend fun deleteCategory(categoryId: String) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Seller.DeleteCategory(categoryId)
        ).requireData()
}
