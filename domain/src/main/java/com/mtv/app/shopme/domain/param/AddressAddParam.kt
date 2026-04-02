/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AddressAddParam.kt
 *
 * Last modified by Dedy Wijaya on 01/04/26 09.41
 */

package com.mtv.app.shopme.domain.param

data class AddressAddParam(
    val villageId: String,
    val block: String,
    val number: String,
    val rt: String,
    val rw: String,
    val isDefault: Boolean
)