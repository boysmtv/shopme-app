package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.MediaRepository
import javax.inject.Inject

class UploadMediaUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    operator fun invoke(localUri: String, scope: String) = repository.uploadImage(localUri, scope)
}
