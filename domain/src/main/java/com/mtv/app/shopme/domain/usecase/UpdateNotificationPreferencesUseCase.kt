package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.NotificationPreferencesParam
import com.mtv.app.shopme.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateNotificationPreferencesUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(param: NotificationPreferencesParam) = repository.updateNotificationPreferences(param)
}
