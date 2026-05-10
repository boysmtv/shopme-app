/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CafeRepository.kt
 *
 * Last modified by Dedy Wijaya on 28/03/26 22.18
 */

package com.mtv.app.shopme.domain.repository

import com.mtv.app.shopme.domain.model.Cafe
import com.mtv.app.shopme.domain.model.CafeAddress
import com.mtv.app.shopme.domain.param.CafeAddParam
import com.mtv.app.shopme.domain.param.CafeAddressUpsertParam
import com.mtv.based.core.network.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CafeRepository {

    fun getCafe(id: String): Flow<Resource<Cafe>>

    fun createCafe(param: CafeAddParam): Flow<Resource<Unit>>

    fun updateCafe(id: String, param: CafeAddParam): Flow<Resource<Unit>>

    fun getCafeAddress(id: String): Flow<Resource<CafeAddress>>

    fun upsertCafeAddress(param: CafeAddressUpsertParam): Flow<Resource<Unit>>

}
