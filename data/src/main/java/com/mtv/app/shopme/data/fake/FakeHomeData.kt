/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FakeHomeData.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 21.12
 */

package com.mtv.app.shopme.data.fake


import com.mtv.app.shopme.data.remote.response.AddressResponse
import com.mtv.app.shopme.data.remote.response.CustomerResponse
import com.mtv.app.shopme.data.remote.response.MenuSummaryResponse
import com.mtv.app.shopme.data.remote.response.StatsResponse

fun fakeCustomerResponse(): CustomerResponse {
    return CustomerResponse(
        name = "Dedy Wijaya",
        phone = "08123456789",
        email = "boys.mtv@gmail.com",
        address = AddressResponse(
            id = "1",
            village = "Puri Lestari",
            block = "H2",
            number = "21",
            rt = "012",
            rw = "002",
            isDefault = true
        ),
        photo = "https://picsum.photos/200",
        verified = true,
        stats = StatsResponse(
            totalOrders = 120,
            activeOrders = 3,
            membership = "Gold"
        ),
        menuSummary = MenuSummaryResponse(
            ordered = 10,
            cooking = 2,
            shipping = 1,
            completed = 7,
            cancelled = 0
        )
    )
}