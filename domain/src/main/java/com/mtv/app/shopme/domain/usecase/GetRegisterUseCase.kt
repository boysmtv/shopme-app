/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SplashUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 14.12
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.RegisterParam
import com.mtv.app.shopme.domain.repository.AuthRepository
import javax.inject.Inject

class GetRegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(param: RegisterParam) = repository.register(param)
}