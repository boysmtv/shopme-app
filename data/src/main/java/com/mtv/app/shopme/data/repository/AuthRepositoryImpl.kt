package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.remote.datasource.AuthRemoteDataSource
import com.mtv.app.shopme.domain.param.ChangePasswordParam
import com.mtv.app.shopme.domain.param.ChangePinParam
import com.mtv.app.shopme.domain.param.ForgotPasswordParam
import com.mtv.app.shopme.domain.param.LoginParam
import com.mtv.app.shopme.domain.param.ResetPasswordParam
import com.mtv.app.shopme.domain.param.RegisterParam
import com.mtv.app.shopme.domain.param.VerifyOtpParam
import com.mtv.app.shopme.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remote: AuthRemoteDataSource,
    private val resultFlow: ResultFlowFactory
) : AuthRepository {

    override fun login(param: LoginParam) =
        resultFlow.create {
            remote.login(param).toDomain()
        }

    override fun register(param: RegisterParam) =
        resultFlow.create {
            remote.register(param).toDomain()
        }

    override fun forgotPassword(param: ForgotPasswordParam) =
        resultFlow.create {
            remote.forgotPassword(param)
        }

    override fun verifyOtp(param: VerifyOtpParam) =
        resultFlow.create {
            remote.verifyOtp(param)
        }

    override fun resetPassword(param: ResetPasswordParam) =
        resultFlow.create {
            remote.resetPassword(param)
        }

    override fun changePassword(param: ChangePasswordParam) =
        resultFlow.create {
            remote.changePassword(param)
        }

    override fun changePin(param: ChangePinParam) =
        resultFlow.create {
            remote.changePin(param)
        }
}
