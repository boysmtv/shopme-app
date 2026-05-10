package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.VerifyOtpParam
import com.mtv.app.shopme.domain.repository.AuthRepository
import javax.inject.Inject

class VerifyOtpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(param: VerifyOtpParam) = repository.verifyOtp(param)
}
