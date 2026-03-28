/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FoodDetailRepositoryImpl.kt
 *
 * Last modified by Dedy Wijaya on 27/03/26 22.38
 */

package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.remote.datasource.FoodDetailRemoteDataSource
import com.mtv.app.shopme.domain.model.param.CartAddParam
import com.mtv.app.shopme.domain.repository.FoodDetailRepository
import javax.inject.Inject


class FoodDetailRepositoryImpl @Inject constructor(
    private val remote: FoodDetailRemoteDataSource,
    private val resultFlow: ResultFlowFactory
) : FoodDetailRepository {

    override fun getFoodDetail(id: String) =
        resultFlow.create {
            remote.getFoodDetail(id).toDomain()
        }

    override fun getSimilarFoods(cafeId: String) =
        resultFlow.create {
            remote.getSimilarFoods(cafeId).map { it.toDomain() }
        }

    override fun addToCart(param: CartAddParam) =
        resultFlow.createUnit {
            remote.addToCart(param)
        }
}