package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.CartRepository
import javax.inject.Inject

class DeleteCartItemUseCase @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke(cartId: String) = repository.deleteCartItem(cartId)
}
