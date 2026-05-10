package com.mtv.app.shopme.domain.repository

import com.mtv.app.shopme.domain.model.Login
import com.mtv.app.shopme.domain.model.Register
import com.mtv.app.shopme.domain.param.ChangePasswordParam
import com.mtv.app.shopme.domain.param.ChangePinParam
import com.mtv.app.shopme.domain.param.ForgotPasswordParam
import com.mtv.app.shopme.domain.param.LoginParam
import com.mtv.app.shopme.domain.param.ResetPasswordParam
import com.mtv.app.shopme.domain.param.RegisterParam
import com.mtv.app.shopme.domain.param.VerifyOtpParam
import com.mtv.based.core.network.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(param: LoginParam): Flow<Resource<Login>>
    fun register(param: RegisterParam): Flow<Resource<Register>>
    fun forgotPassword(param: ForgotPasswordParam): Flow<Resource<Unit>>
    fun verifyOtp(param: VerifyOtpParam): Flow<Resource<Unit>>
    fun resetPassword(param: ResetPasswordParam): Flow<Resource<Unit>>
    fun changePassword(param: ChangePasswordParam): Flow<Resource<Unit>>
    fun changePin(param: ChangePinParam): Flow<Resource<Unit>>
}
