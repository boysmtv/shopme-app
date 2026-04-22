/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CustomerUpdateParam.kt
 *
 * Last modified by Dedy Wijaya on 01/04/26 09.40
 */

package com.mtv.app.shopme.domain.param

data class CustomerUpdateParam(
    val name: String,
    val phone: String,
    val photo: String?,
    val fcmToken: String?
)