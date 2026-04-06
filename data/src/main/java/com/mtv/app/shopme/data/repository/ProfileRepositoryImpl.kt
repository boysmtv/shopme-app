/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ProfileRepositoryImpl.kt
 *
 * Last modified by Dedy Wijaya on 01/04/26 09.29
 */

package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.remote.datasource.ProfileRemoteDataSource
import com.mtv.app.shopme.domain.param.AddressAddParam
import com.mtv.app.shopme.domain.param.AddressDefaultParam
import com.mtv.app.shopme.domain.param.AddressDeleteParam
import com.mtv.app.shopme.domain.param.CustomerUpdateParam
import com.mtv.app.shopme.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val remote: ProfileRemoteDataSource,
    private val resultFlow: ResultFlowFactory
) : ProfileRepository {

    override fun getCustomer() =
        resultFlow.create {
            remote.getCustomer().toDomain()
        }

    override fun updateProfile(param: CustomerUpdateParam) =
        resultFlow.create {
            remote.updateProfile(param)
        }

    override fun getAddresses() =
        resultFlow.create {
            remote.getAddresses().map { it.toDomain() }
        }

    override fun getVillages() =
        resultFlow.create {
            remote.getVillages().map { it.toDomain() }
        }

    override fun addAddress(param: AddressAddParam) =
        resultFlow.create {
            remote.addAddress(param)
        }

    override fun deleteAddress(param: AddressDeleteParam) =
        resultFlow.create {
            remote.deleteAddress(param)
        }

    override fun setDefaultAddress(param: AddressDefaultParam) =
        resultFlow.create {
            remote.setDefaultAddress(param)
        }
}