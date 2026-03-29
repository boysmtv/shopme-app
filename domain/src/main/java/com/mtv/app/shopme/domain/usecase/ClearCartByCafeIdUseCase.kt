/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartQuantityUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 13.05
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.CartClearByCafeParam
import com.mtv.app.shopme.domain.repository.CartRepository
import javax.inject.Inject

class ClearCartByCafeIdUseCase @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke(param: CartClearByCafeParam) = repository.clearCartByCafe(param)
}