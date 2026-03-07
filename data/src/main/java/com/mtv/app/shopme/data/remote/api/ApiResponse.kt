package com.mtv.app.shopme.data.remote.api

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val timestamp: String,
    val status: Int,
    val code: String,
    val message: String,
    val traceId: String,
    val data: T? = null
)