/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SearchRepository.kt
 *
 * Last modified by Dedy Wijaya on 26/03/26 13.48
 */

package com.mtv.app.shopme.domain.repository

import com.mtv.app.shopme.domain.model.PagedData
import com.mtv.app.shopme.domain.model.SearchFood
import com.mtv.app.shopme.domain.param.SearchParam
import com.mtv.based.core.network.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchFoods(request: SearchParam): Flow<Resource<PagedData<SearchFood>>>

}