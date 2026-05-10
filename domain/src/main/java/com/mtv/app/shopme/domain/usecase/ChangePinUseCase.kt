package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.ChangePinParam
import com.mtv.app.shopme.domain.repository.AuthRepository
import javax.inject.Inject

class ChangePinUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(param: ChangePinParam) = repository.changePin(param)
}
