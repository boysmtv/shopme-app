/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: CafeContract.kt
 *
 * Last modified by Dedy Wijaya on 14/02/26 22.34
 */

package com.mtv.app.shopme.feature.contract

import com.mtv.app.shopme.data.FoodItemModel
import com.mtv.app.shopme.data.OwnerCafeModel
import com.mtv.based.core.network.utils.ResourceFirebase

data class CafeStateListener(
    val loadingState: ResourceFirebase<Unit> = ResourceFirebase.Loading,
    val activeDialog: CafeDialog? = null
)

data class CafeDataListener(
    val cafe: OwnerCafeModel? = null,
    val foods: List<FoodItemModel> = emptyList()
)

data class CafeEventListener(
    val onFoodClick: (FoodItemModel) -> Unit = {},
    val onDismissActiveDialog: () -> Unit = {}
)

data class CafeNavigationListener(
    val onBack: () -> Unit = {},
    val onFoodDetail: (Int) -> Unit = {}
)

sealed class CafeDialog {
    object Error : CafeDialog()
}
