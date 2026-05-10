package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class CafeAddressUpsertRequest(
    val cafeId: String,
    val villageId: String,
    val block: String,
    val rt: String,
    val rw: String,
    val number: String
)
