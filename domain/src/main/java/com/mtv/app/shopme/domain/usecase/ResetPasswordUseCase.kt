package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.ResetPasswordParam
import com.mtv.app.shopme.domain.repository.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(param: ResetPasswordParam) = repository.resetPassword(param)
}
