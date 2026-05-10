package com.mtv.app.shopme.domain.param

data class CafeAddressUpsertParam(
    val cafeId: String,
    val villageId: String,
    val block: String,
    val number: String,
    val rt: String,
    val rw: String
)
