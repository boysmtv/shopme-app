/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: EditAddressContract.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 14.02
 */

package com.mtv.app.shopme.feature.customer.contract

class EditAddressStateListener {

}

class EditAddressDataListener(
    val village: String = "Puri Lestari",
    val block: String = "H12",
    val number: String = "23",
    val rt: String = "004",
    val rw: String = "005",
    val map: String = "-6.2682185, 106.8296376"
)

class EditAddressEventListener(
    val onSaveClicked: (
        village: String,
        block: String,
        number: String,
        rt: String,
        rw: String,
        map: String
    ) -> Unit = { _, _ , _ , _ , _ , _ -> }
)

class EditAddressNavigationListener(
    val onBack: () -> Unit = {}
)