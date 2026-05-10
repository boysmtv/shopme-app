package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.SellerRepository
import javax.inject.Inject

class GetSellerPaymentMethodsUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    operator fun invoke() = repository.getPaymentMethods()
}
