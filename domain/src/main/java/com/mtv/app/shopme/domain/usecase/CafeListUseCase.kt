/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CafeListUseCase.kt
 *
 * Last modified by Dedy Wijaya on 01/06/26
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.CafeRepository
import javax.inject.Inject

class CafeListUseCase @Inject constructor(
    private val repository: CafeRepository
) {
    operator fun invoke() = repository.getCafeList()
}
