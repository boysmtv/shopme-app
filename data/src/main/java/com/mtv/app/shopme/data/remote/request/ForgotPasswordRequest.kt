package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordRequest(
    val email: String
)
