/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CafeViewModel.kt
 *
 * Last modified by Dedy Wijaya on 14/02/26 22.34
 */

package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.common.mockFoodList
import com.mtv.app.shopme.common.mockOwnerCafe
import com.mtv.app.shopme.data.FoodItemModel
import com.mtv.app.shopme.data.OwnerCafeModel
import com.mtv.app.shopme.feature.customer.contract.CafeDataListener
import com.mtv.app.shopme.feature.customer.contract.CafeStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CafeViewModel @Inject constructor() :
    BaseViewModel(),
    UiOwner<CafeStateListener, CafeDataListener> {

    /** UI STATE : LOADING / ERROR / SUCCESS (API Response) */
    override val uiState = MutableStateFlow(CafeStateListener())

    /** UI DATA : DATA PERSIST (Prefs) */
    override val uiData = MutableStateFlow(CafeDataListener())

    init {
        loadDummyData()

        uiData.value = CafeDataListener(
            cafe = mockOwnerCafe,
            foods = mockFoodList
        )
    }


    private fun loadDummyData() {
        viewModelScope.launch {
            uiData.emit(
                CafeDataListener(
                    cafe = OwnerCafeModel(
                        cafeId = "1",
                        cafeName = "Mamah Al Cafe",
                        cafeAddress = "Puri Lestari Blok H12/01",
                        cafePhone = "08123456789",
                        cafeOpenTime = "08:00",
                        cafeCloseTime = "22:00",
                        cafeRating = 4.6f
                    ),
                    foods = listOf(
                        FoodItemModel(
                            id = 1,
                            name = "Double Beef Burger",
                            desc = "Burger daging sapi double",
                            price = 9.99
                        ),
                        FoodItemModel(
                            id = 2,
                            name = "Cheese Pizza",
                            desc = "Pizza keju mozzarella",
                            price = 11.49
                        ),
                        FoodItemModel(
                            id = 3,
                            name = "French Fries",
                            desc = "Kentang goreng crispy",
                            price = 4.99
                        )
                    )
                )
            )
        }
    }
}
