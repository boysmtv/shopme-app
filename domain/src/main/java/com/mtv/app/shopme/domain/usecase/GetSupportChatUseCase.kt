package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.AppRepository
import javax.inject.Inject

class GetSupportChatUseCase @Inject constructor(
    private val repository: AppRepository
) {
    operator fun invoke() = repository.getSupportChat()
}
