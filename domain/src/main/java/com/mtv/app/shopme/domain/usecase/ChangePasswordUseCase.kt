package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.ChangePasswordParam
import com.mtv.app.shopme.domain.repository.AuthRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(param: ChangePasswordParam) = repository.changePassword(param)
}
