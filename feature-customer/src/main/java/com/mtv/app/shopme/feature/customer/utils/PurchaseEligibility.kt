package com.mtv.app.shopme.feature.customer.utils

import com.mtv.app.shopme.domain.model.Customer

private const val PURCHASE_REQUIREMENTS_MESSAGE =
    "Lengkapi nama, nomor telepon, email, dan alamat default sebelum berbelanja."

fun Customer?.purchaseRequirementsMessage(): String? {
    val customer = this ?: return PURCHASE_REQUIREMENTS_MESSAGE
    val address = customer.address
    val hasCompleteProfile = customer.name.isNotBlank() &&
        customer.phone.isNotBlank() &&
        customer.email.isNotBlank()
    val hasCompleteAddress = address != null &&
        address.village.isNotBlank() &&
        address.block.isNotBlank() &&
        address.number.isNotBlank() &&
        address.rt.isNotBlank() &&
        address.rw.isNotBlank()

    return if (hasCompleteProfile && hasCompleteAddress) {
        null
    } else {
        PURCHASE_REQUIREMENTS_MESSAGE
    }
}
