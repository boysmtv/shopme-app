package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.ChatRepository
import javax.inject.Inject

class EnsureSellerChatConversationUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(orderId: String) = repository.ensureSellerConversation(orderId)
}
