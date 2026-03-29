/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartRepository.kt
 *
 * Last modified by Dedy Wijaya on 25/03/26 12.23
 */

package com.mtv.app.shopme.domain.repository

import com.mtv.app.shopme.domain.model.Cart
import com.mtv.app.shopme.domain.model.SessionToken
import com.mtv.app.shopme.domain.param.CartClearByCafeParam
import com.mtv.app.shopme.domain.param.CartQuantityParam
import com.mtv.app.shopme.domain.param.CreateOrderParam
import com.mtv.app.shopme.domain.param.VerifyPinParam
import com.mtv.based.core.network.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun getCart(): Flow<Resource<List<Cart>>>

    fun updateQuantity(param: CartQuantityParam): Flow<Resource<Unit>>

    fun clearCart(): Flow<Resource<Unit>>

    fun clearCartByCafe(param: CartClearByCafeParam): Flow<Resource<Unit>>

    fun createOrder(param: CreateOrderParam): Flow<Resource<Unit>>

    fun verifyPin(param: VerifyPinParam): Flow<Resource<Unit>>

    fun getSessionToken(): Flow<Resource<SessionToken>>
}