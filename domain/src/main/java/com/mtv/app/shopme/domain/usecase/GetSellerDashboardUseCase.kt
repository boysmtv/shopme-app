package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.SellerRepository
import javax.inject.Inject

class GetSellerDashboardUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    operator fun invoke() = repository.getDashboard()
}
