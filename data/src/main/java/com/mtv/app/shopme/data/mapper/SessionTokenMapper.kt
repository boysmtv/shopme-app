/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SessionTokenMapper.kt
 *
 * Last modified by Dedy Wijaya on 25/03/26 15.21
 */

package com.mtv.app.shopme.data.mapper

import com.mtv.app.shopme.data.remote.response.SessionTokenResponse
import com.mtv.app.shopme.domain.model.SessionToken

fun SessionTokenResponse.toDomain(): SessionToken =
    SessionToken(
        token = token
    )
