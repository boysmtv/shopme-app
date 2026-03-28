package com.mtv.app.shopme.data.remote.datasource

import com.mtv.app.shopme.data.base.BaseRemoteDataSource
import com.mtv.app.shopme.data.mapper.param.toRequest
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.CartItemResponse
import com.mtv.app.shopme.data.remote.response.SessionTokenResponse
import com.mtv.app.shopme.data.remote.response.VerifyPinResponse
import com.mtv.app.shopme.data.utils.requireData
import com.mtv.app.shopme.domain.model.param.CartClearByCafeParam
import com.mtv.app.shopme.domain.model.param.CartQuantityParam
import com.mtv.app.shopme.domain.model.param.CreateOrderParam
import com.mtv.app.shopme.domain.model.param.VerifyPinParam
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject

class CartRemoteDataSource @Inject constructor(
    network: NetworkRepository
) : BaseRemoteDataSource(network) {

    suspend fun getCart() =
        request<ApiResponse<List<CartItemResponse>>>(
            endpoint = ApiEndPoint.Cart.Get
        ).requireData()

    suspend fun updateQuantity(param: CartQuantityParam) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Cart.Quantity(param.cartId),
            body = param.toRequest()
        )

    suspend fun clearCart() =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Cart.Clear
        )

    suspend fun clearCartByCafe(param: CartClearByCafeParam) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Cart.DeleteByCafeId(param.cafeId)
        )

    suspend fun createOrder(param: CreateOrderParam) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Order.Create,
            body = param.toRequest()
        )

    suspend fun verifyPin(param: VerifyPinParam) =
        request<ApiResponse<VerifyPinResponse>>(
            endpoint = ApiEndPoint.Customer.VerifyPin,
            body = param.toRequest()
        ).requireData()

    suspend fun getSessionToken() =
        request<ApiResponse<SessionTokenResponse>>(
            endpoint = ApiEndPoint.Order.GetSession
        ).requireData()
}