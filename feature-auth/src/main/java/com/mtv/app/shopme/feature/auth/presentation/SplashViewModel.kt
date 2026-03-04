package com.mtv.app.shopme.feature.auth.presentation

import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SecurePrefs
import com.mtv.app.core.provider.utils.SessionManager
import com.mtv.app.core.provider.utils.device.DeviceInfoProvider
import com.mtv.app.core.provider.utils.device.InstallationIdProvider
import com.mtv.app.shopme.common.ConstantPreferences.ACCESS_TOKEN
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.common.valueFlowOf
import com.mtv.app.shopme.data.remote.request.SplashRequest
import com.mtv.app.shopme.domain.usecase.SplashUseCase
import com.mtv.app.shopme.feature.auth.contract.SplashDataListener
import com.mtv.app.shopme.feature.auth.contract.SplashStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val securePrefs: SecurePrefs,
    private val installationIdProvider: InstallationIdProvider,
    private val deviceInfoProvider: DeviceInfoProvider,
    private val splashUseCase: SplashUseCase
) : BaseViewModel(), UiOwner<SplashStateListener, Unit> {

    /** UI STATE : LOADING / ERROR / SUCCESS (API Response) */
    override val uiState = MutableStateFlow(SplashStateListener())

    /** UI DATA : DATA PERSIST (Prefs) */
    override val uiData = MutableStateFlow(Unit)

    fun doSplash() {
        launchUseCase(
            loading = false,
            target = uiState.valueFlowOf(
                get = { it.splashState },
                set = { state -> copy(splashState = state) }
            ),
            block = {
                splashUseCase(
                    SplashRequest(
                        deviceId = installationIdProvider.getInstallationId(),
                        deviceInfo = deviceInfoProvider.getAllDeviceInfo(),
                        createdAt = System.currentTimeMillis().toString()
                    )
                )
            },
            onSuccess = { data ->
                securePrefs.putString(ACCESS_TOKEN, data)
            }
        )
    }

}