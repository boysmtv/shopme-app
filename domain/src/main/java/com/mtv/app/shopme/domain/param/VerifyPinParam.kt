/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: VerifyPinParam.kt
 *
 * Last modified by Dedy Wijaya on 25/03/26 15.34
 */

package com.mtv.app.shopme.domain.param

data class VerifyPinParam(
    var token: String,
    var pin: String?
)