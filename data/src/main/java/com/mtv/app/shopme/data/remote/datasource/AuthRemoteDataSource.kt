package com.mtv.app.shopme.data.remote.datasource

import com.mtv.app.shopme.data.base.BaseRemoteDataSource
import com.mtv.app.shopme.data.mapper.toRequest
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.LoginResponse
import com.mtv.app.shopme.data.utils.requireData
import com.mtv.app.shopme.domain.param.ChangePasswordParam
import com.mtv.app.shopme.domain.param.ChangePinParam
import com.mtv.app.shopme.domain.param.ForgotPasswordParam
import com.mtv.app.shopme.domain.param.LoginParam
import com.mtv.app.shopme.domain.param.ResetPasswordParam
import com.mtv.app.shopme.domain.param.RegisterParam
import com.mtv.app.shopme.domain.param.VerifyOtpParam
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    network: NetworkRepository
) : BaseRemoteDataSource(network) {

    suspend fun login(param: LoginParam) =
        request<ApiResponse<LoginResponse>>(
            endpoint = ApiEndPoint.Auth.Login,
            body = param.toRequest()
        ).requireData()

    suspend fun register(param: RegisterParam) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Auth.Register,
            body = param.toRequest()
        ).requireData()

    suspend fun forgotPassword(param: ForgotPasswordParam) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Auth.ForgotPassword,
            body = param.toRequest()
        ).requireData()

    suspend fun verifyOtp(param: VerifyOtpParam) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Auth.VerifyOtp,
            body = param.toRequest()
        ).requireData()

    suspend fun resetPassword(param: ResetPasswordParam) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Auth.ResetPassword,
            body = param.toRequest()
        ).requireData()

    suspend fun changePassword(param: ChangePasswordParam) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Auth.ChangePassword,
            body = param.toRequest()
        ).requireData()

    suspend fun changePin(param: ChangePinParam) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Customer.ChangePin,
            body = param.toRequest()
        ).requireData()
}
