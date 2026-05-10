package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    val email: String,
    val newPassword: String
)
