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
                remote.uploadImage(file, scope).toDomain()
            } finally {
                file.delete()
            }
        }

    private fun prepareUploadFile(localUri: String): File {
        val uri = Uri.parse(localUri)
        return uriToCompressedJpegFile(context, uri)
            ?: throw IllegalArgumentException("Unable to prepare image upload")
    }
}
