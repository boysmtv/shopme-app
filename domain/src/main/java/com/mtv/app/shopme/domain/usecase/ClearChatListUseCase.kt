/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ClearChatListUseCase.kt
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.ChatRepository
import javax.inject.Inject

class ClearChatListUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(asSeller: Boolean = false) = repository.clearAllMessages(asSeller)
}
