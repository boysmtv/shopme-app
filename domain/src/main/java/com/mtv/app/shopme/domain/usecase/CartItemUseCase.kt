/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 11.55
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.CartRepository
import javax.inject.Inject

class CartItemUseCase @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke() = repository.getCart()
}