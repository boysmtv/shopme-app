/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: EditProfileContract.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 14.02
 */

package com.mtv.app.shopme.feature.contract

class EditProfileStateListener {

}

class EditProfileDataListener(
    val name: String = "Dedy Wijaya",
    val phone: String = "08158844424",
    val email: String = "Boys.mtv@gmail.com",
    val address: String = "Puri Lestari - Blok G06/01",
    val map: String = "-6.2682185,106.8296376"
)

class EditProfileEventListener(
    val onSaveClicked: (name: String, phone: String, address: String) -> Unit = { _, _, _ -> }
)

class EditProfileNavigationListener(
    val onBack: () -> Unit = {}
)