/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: RegisterParam.kt
 *
 * Last modified by Dedy Wijaya on 16/04/26 11.41
 */

package com.mtv.app.shopme.domain.param

data class RegisterParam(
    val name: String,
    val email: String,
    val password: String
)