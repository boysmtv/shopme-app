/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SplashUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 14.12
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.CustomerUpdateParam
import com.mtv.app.shopme.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateCustomerUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(param: CustomerUpdateParam) = repository.updateProfile(param)
}