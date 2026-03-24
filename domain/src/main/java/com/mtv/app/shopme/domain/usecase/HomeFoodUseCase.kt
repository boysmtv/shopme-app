/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeFoodUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 11.36
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.HomeRepository
import javax.inject.Inject

class HomeFoodUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke() = repository.getFoods()
}