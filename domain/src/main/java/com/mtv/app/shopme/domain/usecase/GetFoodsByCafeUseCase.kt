/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: GetFoodsByCafeUseCase.kt
 *
 * Last modified by Dedy Wijaya on 18/03/26 01.29
 */

package com.mtv.app.shopme.domain.usecase

import javax.inject.Inject

class GetFoodsByCafeUseCase @Inject constructor(
    private val repository: CafeRepository
) {
    operator fun invoke(id: String) = repository.getFoodsByCafe(id)
}