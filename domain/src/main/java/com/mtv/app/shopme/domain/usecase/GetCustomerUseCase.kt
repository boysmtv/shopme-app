/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SplashUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 14.12
 */

package com.mtv.app.shopme.domain.usecase

import javax.inject.Inject

class GetCustomerUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke() = repository.getCustomer()
}