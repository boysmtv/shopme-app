/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AuthRepository.kt
 *
 * Last modified by Dedy Wijaya on 16/04/26 11.30
 */

package com.mtv.app.shopme.domain.repository

import com.mtv.app.shopme.domain.model.Login
import com.mtv.app.shopme.domain.model.Register
import com.mtv.app.shopme.domain.param.LoginParam
import com.mtv.app.shopme.domain.param.RegisterParam
import com.mtv.based.core.network.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(param: LoginParam): Flow<Resource<Login>>
    fun register(param: RegisterParam): Flow<Resource<Register>>
}