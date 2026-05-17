package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.ChatRepository
import javax.inject.Inject

class EnsureOrderChatConversationUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(orderId: String) = repository.ensureOrderConversation(orderId)
}
