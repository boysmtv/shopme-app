/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatMessageMarkAsReadUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 13.45
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.ChatRepository
import javax.inject.Inject

class ChatMessageMarkAsReadUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(id: String, asSeller: Boolean = false) =
        repository.readAllMessage(id, asSeller)
}
