package com.mtv.app.shopme.data.remote.datasource

import com.mtv.app.shopme.data.base.BaseRemoteDataSource
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.MediaUploadResponse
import com.mtv.app.shopme.data.utils.requireData
import com.mtv.based.core.network.repository.NetworkRepository
import java.io.File
import javax.inject.Inject

class MediaRemoteDataSource @Inject constructor(
    network: NetworkRepository
) : BaseRemoteDataSource(network) {

    suspend fun uploadImage(file: File, scope: String) =
        request<ApiResponse<MediaUploadResponse>>(
            endpoint = ApiEndPoint.Media.Upload,
            body = mapOf(
                "file" to file,
                "scope" to scope
            )
        ).requireData()
}
