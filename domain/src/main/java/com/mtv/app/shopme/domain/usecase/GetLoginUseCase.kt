/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: GetLoginUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 14.12
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.LoginParam
import com.mtv.app.shopme.domain.repository.AuthRepository
import javax.inject.Inject

class GetLoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(param: LoginParam) = repository.login(param)
}