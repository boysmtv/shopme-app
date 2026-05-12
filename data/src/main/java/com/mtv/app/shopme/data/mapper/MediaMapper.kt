package com.mtv.app.shopme.data.mapper

import com.mtv.app.shopme.data.remote.response.MediaUploadResponse
import com.mtv.app.shopme.domain.model.UploadedMedia

fun MediaUploadResponse.toDomain() = UploadedMedia(
    key = key,
    originalUrl = originalUrl,
    mediumUrl = mediumUrl,
    thumbnailUrl = thumbnailUrl
)
