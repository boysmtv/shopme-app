package com.mtv.app.shopme.domain.repository

import com.mtv.app.shopme.domain.model.UploadedMedia
import com.mtv.based.core.network.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun uploadImage(localUri: String, scope: String): Flow<Resource<UploadedMedia>>
}
