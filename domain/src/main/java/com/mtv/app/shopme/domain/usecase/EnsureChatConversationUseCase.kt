package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.ChatRepository
import javax.inject.Inject

class EnsureChatConversationUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(cafeId: String) = repository.ensureConversation(cafeId)
}
