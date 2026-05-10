package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class ChangePinRequest(
    val oldPin: String,
    val newPin: String
)
