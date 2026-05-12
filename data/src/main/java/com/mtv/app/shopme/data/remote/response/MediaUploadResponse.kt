package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class MediaUploadResponse(
    val key: String,
    val originalUrl: String,
    val mediumUrl: String,
    val thumbnailUrl: String
)
