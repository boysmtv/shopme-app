package com.mtv.app.shopme.data.remote.datasource

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.mtv.app.shopme.data.base.BaseRemoteDataSource
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.MediaUploadResponse
import com.mtv.app.shopme.data.utils.requireData
import com.mtv.based.core.network.repository.NetworkRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.IOException
import javax.inject.Inject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody

class MediaRemoteDataSource @Inject constructor(
    network: NetworkRepository,
    @ApplicationContext context: Context
) : BaseRemoteDataSource(network) {

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(
            ChuckerInterceptor.Builder(context)
                .redactHeaders("Authorization", "Cookie")
                .build()
        )
        .build()

    suspend fun createUploadTicket(scope: String, contentType: String) =
        request<ApiResponse<MediaUploadResponse>>(
            endpoint = ApiEndPoint.Media.PresignUpload(
                scope = scope,
                contentType = java.net.URLEncoder.encode(contentType, Charsets.UTF_8.name())
            )
        ).requireData()

    suspend fun uploadToPresignedUrl(
        uploadUrl: String,
        file: File,
        contentType: String
    ) {
        val request = Request.Builder()
            .url(uploadUrl)
            .put(file.asRequestBody(contentType.toMediaType()))
            .build()

        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Presigned media upload failed with ${response.code}")
            }
        }
    }

    suspend fun uploadImage(file: File, scope: String) =
        request<ApiResponse<MediaUploadResponse>>(
            endpoint = ApiEndPoint.Media.Upload,
            body = mapOf(
                "file" to file,
                "scope" to scope
            )
        ).requireData()
}
