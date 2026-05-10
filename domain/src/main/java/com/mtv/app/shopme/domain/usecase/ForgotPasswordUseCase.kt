package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.ForgotPasswordParam
import com.mtv.app.shopme.domain.repository.AuthRepository
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(param: ForgotPasswordParam) = repository.forgotPassword(param)
}
