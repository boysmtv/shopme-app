/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CustomerModel.kt
 *
 * Last modified by Dedy Wijaya on 24/02/26 07.46
 */

package com.mtv.app.shopme.domain.model

class CustomerAccount(
    val customerId: String = "ACC1234",
    val name: String = "Dedy Wijaya",
    val phone: String = "08158844424",
    val email: String = "Boys.mtv@gmail.com",
    val addressId: String = "ADD123",
    val photoId: String = "PT123",
)

class CustomerAddress(
    val addressId: String = "ADD123",
    val village: String = "Puri Lestari",
    val block: String = "H12",
    val number: String = "23",
    val rt: String = "004",
    val rw: String = "005",
)

class CustomerPhoto(
    val photoId: String = "PT123",
    val photo: String = "Base64"
)

class RegisterLog(
    regId: String = "LOG123",
    email: String = "Boys.mtv@gmail.com",
)

class LoginLog(
    logId: String = "LOG123",
    email: String = "Boys.mtv@gmail.com",
)