/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeRepositoryImpl.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 00.59
 */

package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.remote.datasource.HomeRemoteDataSource
import com.mtv.app.shopme.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val remote: HomeRemoteDataSource,
    private val resultFlow: ResultFlowFactory
) : HomeRepository {

    override fun getCustomer() =
        resultFlow.create {
            remote.getCustomer().toDomain()
        }

    override fun getFoods() =
        resultFlow.create {
            remote.getFoods().map { it.toDomain() }
        }

}