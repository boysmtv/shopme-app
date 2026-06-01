package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.AddressUpdateParam
import com.mtv.app.shopme.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateAddressUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(id: String, param: AddressUpdateParam) = repository.updateAddress(id, param)
}
