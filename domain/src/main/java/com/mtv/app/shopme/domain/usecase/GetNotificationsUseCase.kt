package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke() = repository.getCustomerNotifications()
}
