/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartRepositoryImpl.kt
 *
 * Last modified by Dedy Wijaya on 25/03/26 12.24
 */

package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.remote.datasource.CartRemoteDataSource
import com.mtv.app.shopme.domain.param.CartAddParam
import com.mtv.app.shopme.domain.param.CartClearByCafeParam
import com.mtv.app.shopme.domain.param.CartQuantityParam
import com.mtv.app.shopme.domain.param.CreateOrderParam
import com.mtv.app.shopme.domain.param.VerifyPinParam
import com.mtv.app.shopme.domain.repository.CartRepository
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val remote: CartRemoteDataSource,
    private val resultFlow: ResultFlowFactory
) : CartRepository {

    override fun getCart() =
        resultFlow.create {
            remote.getCart().map { it.toDomain() }
        }

    override fun addCart(param: CartAddParam) =
        resultFlow.createUnit {
            remote.addCart(param)
        }

    override fun updateQuantity(param: CartQuantityParam) =
        resultFlow.createUnit {
            remote.updateQuantity(param)
        }

    override fun clearCart() =
        resultFlow.createUnit {
            remote.clearCart()
        }

    override fun clearCartByCafe(param: CartClearByCafeParam) =
        resultFlow.createUnit {
            remote.clearCartByCafe(param)
        }

    override fun createOrder(param: CreateOrderParam) =
        resultFlow.createUnit {
            remote.createOrder(param)
        }

    override fun verifyPin(param: VerifyPinParam) =
        resultFlow.createUnit {
            remote.verifyPin(param)
        }

    override fun getSessionToken() =
        resultFlow.create {
            remote.getSessionToken().toDomain()
        }

}