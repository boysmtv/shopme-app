/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: EditProfileContract.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 14.02
 */

package com.mtv.app.shopme.feature.customer.contract

class EditProfileStateListener {

}

class EditProfileDataListener(
    val name: String = "Dedy Wijaya",
    val phone: String = "08158844424",
    val email: String = "Boys.mtv@gmail.com",
    val village: String = "Puri Lestari",
    val block: String = "H12",
    val number: String = "23",
    val rt: String = "004",
    val rw: String = "005",
    val map: String = "-6.2682185,106.8296376"
)

class EditProfileEventListener(
    val onSaveClicked: (
        name: String,
        phone: String,
        email: String,
    ) -> Unit = { _, _, _ -> }
)

class EditProfileNavigationListener(
    val onBack: () -> Unit = {}
)