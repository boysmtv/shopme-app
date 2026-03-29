/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 11.55
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.SearchParam
import com.mtv.app.shopme.domain.repository.SearchRepository
import javax.inject.Inject

class SearchFoodUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    operator fun invoke(request: SearchParam) = repository.searchFoods(request)
}