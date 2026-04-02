/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CafeRepository.kt
 *
 * Last modified by Dedy Wijaya on 28/03/26 22.18
 */

package com.mtv.app.shopme.domain.repository

import com.mtv.app.shopme.domain.model.Cafe
import com.mtv.app.shopme.domain.model.Food
import com.mtv.based.core.network.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CafeRepository {

    fun getCafe(id: String): Flow<Resource<Cafe>>

}