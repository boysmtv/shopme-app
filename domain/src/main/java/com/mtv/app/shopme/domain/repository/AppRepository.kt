/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AppRepository.kt
 *
 * Last modified by Dedy Wijaya on 16/04/26 10.13
 */

package com.mtv.app.shopme.domain.repository

import com.mtv.app.shopme.domain.param.SplashParam
import com.mtv.app.shopme.domain.model.Splash
import com.mtv.app.shopme.domain.model.SupportCenter
import com.mtv.based.core.network.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun getSplash(param: SplashParam): Flow<Resource<Splash>>
    fun getSupportCenter(): Flow<Resource<SupportCenter>>
}
