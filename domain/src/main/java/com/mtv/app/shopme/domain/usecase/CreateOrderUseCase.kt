/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: VerifyPinUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 13.33
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.CreateOrderParam
import com.mtv.app.shopme.domain.repository.CartRepository
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke(param: CreateOrderParam) = repository.createOrder(param)
}