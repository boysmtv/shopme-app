/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: User.kt
 *
 * Last modified by Dedy Wijaya on 16/04/26 10.15
 */

package com.mtv.app.shopme.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val photo: String?
)