/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AuthRepositoryImpl.kt
 *
 * Last modified by Dedy Wijaya on 16/04/26 11.35
 */

package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.remote.datasource.AuthRemoteDataSource
import com.mtv.app.shopme.domain.param.LoginParam
import com.mtv.app.shopme.domain.param.RegisterParam
import com.mtv.app.shopme.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remote: AuthRemoteDataSource,
    private val resultFlow: ResultFlowFactory
) : AuthRepository {

    override fun login(param: LoginParam) =
        resultFlow.create {
            remote.login(param).toDomain()
        }

    override fun register(param: RegisterParam) =
        resultFlow.create {
            remote.register(param).toDomain()
        }
}