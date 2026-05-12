package com.mtv.app.shopme.data.repository

import android.content.Context
import android.net.Uri
import com.mtv.app.shopme.common.uriToCompressedJpegFile
import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.remote.datasource.MediaRemoteDataSource
import com.mtv.app.shopme.domain.repository.MediaRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remote: MediaRemoteDataSource,
    private val resultFlow: ResultFlowFactory
) : MediaRepository {

    override fun uploadImage(localUri: String, scope: String) =
        resultFlow.create {
            val file = prepareUploadFile(localUri)
            try {
                val ticket = remote.createUploadTicket(
                    scope = scope,
                    contentType = JPEG_CONTENT_TYPE
                )
                val uploadUrl = ticket.uploadUrl
                    ?: throw IllegalStateException("Upload URL is missing")
                remote.uploadToPresignedUrl(
                    uploadUrl = uploadUrl,
                    file = file,
                    contentType = JPEG_CONTENT_TYPE
                )
                ticket.toDomain()
            } finally {
                file.delete()
            }
        }

    private fun prepareUploadFile(localUri: String): File {
        val uri = Uri.parse(localUri)
        return uriToCompressedJpegFile(context, uri)
            ?: throw IllegalArgumentException("Unable to prepare image upload")
    }

    private companion object {
        private const val JPEG_CONTENT_TYPE = "image/jpeg"
    }
}
