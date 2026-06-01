package com.mtv.app.shopme.domain.param

data class AddressUpdateParam(
    val villageId: String,
    val block: String,
    val number: String,
    val rt: String,
    val rw: String,
    val isDefault: Boolean = false
)
