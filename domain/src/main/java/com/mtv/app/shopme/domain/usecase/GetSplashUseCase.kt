/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SplashUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 14.12
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.SplashParam
import com.mtv.app.shopme.domain.repository.AppRepository
import javax.inject.Inject

class GetSplashUseCase @Inject constructor(
    private val repository: AppRepository
) {
    operator fun invoke(param: SplashParam) = repository.getSplash(param)
}