/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: GetCafeUseCase.kt
 *
 * Last modified by Dedy Wijaya on 18/03/26 01.29
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.CafeRepository
import javax.inject.Inject

class GetCafeUseCase @Inject constructor(
    private val repository: CafeRepository
) {
    operator fun invoke(id: String) = repository.getCafe(id)
}