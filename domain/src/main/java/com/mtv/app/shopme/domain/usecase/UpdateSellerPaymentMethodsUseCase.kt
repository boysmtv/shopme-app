package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.SellerPaymentMethodParam
import com.mtv.app.shopme.domain.repository.SellerRepository
import javax.inject.Inject

class UpdateSellerPaymentMethodsUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    operator fun invoke(param: SellerPaymentMethodParam) = repository.updatePaymentMethods(param)
}
