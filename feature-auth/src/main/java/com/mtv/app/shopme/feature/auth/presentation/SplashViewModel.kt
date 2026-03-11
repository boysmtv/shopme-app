package com.mtv.app.shopme.feature.auth.presentation

import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SecurePrefs
import com.mtv.app.core.provider.utils.device.DeviceInfoProvider
import com.mtv.app.core.provider.utils.device.InstallationIdProvider
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.common.config.AppInfoProvider
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.AppConfig
import com.mtv.app.shopme.data.remote.response.SplashResponse
import com.mtv.app.shopme.data.remote.response.User
import com.mtv.app.shopme.domain.usecase.SplashUseCase
import com.mtv.app.shopme.feature.auth.contract.SplashStateListener
import com.mtv.based.core.network.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val securePrefs: SecurePrefs,
    private val installationIdProvider: InstallationIdProvider,
    private val deviceInfoProvider: DeviceInfoProvider,
    private val appInfoProvider: AppInfoProvider,
    private val splashUseCase: SplashUseCase
) : BaseViewModel(), UiOwner<SplashStateListener, Unit> {

    /** UI STATE : LOADING / ERROR / SUCCESS (API Response) */
    override val uiState = MutableStateFlow(SplashStateListener())

    /** UI DATA : DATA PERSIST (Prefs) */
    override val uiData = MutableStateFlow(Unit)

    fun doSplash() {
//        launchUseCase(
//            loading = false,
//            target = uiState.valueFlowOf(
//                get = { it.splashState },
//                set = { state -> copy(splashState = state) }
//            ),
//            block = {
//                splashUseCase(
//                    SplashRequest(
//                        deviceId = installationIdProvider.getInstallationId(),
//                        fcmToken = securePrefs.getString(FCM_TOKEN),
//                        appVersionName = appInfoProvider.versionName,
//                        appVersionCode = appInfoProvider.versionCode,
//                        deviceInfo = deviceInfoProvider.getAllDeviceInfo(),
//                        platform = PLATFORM,
//                        createdAt = System.currentTimeMillis().toString()
//                    )
//                )
//            },
//            onSuccess = { data ->
//                securePrefs.putObject(SPLASH_RESPONSE, data.data)
//            }
//        )

        uiState.update {
            it.copy(
                splashState = Resource.Success(mockApi)
            )
        }
    }

    val mockApi = ApiResponse(
        timestamp = "2026-03-08T10:15:30Z",
        status = 200,
        code = "SUCCESS",
        message = "Request successful",
        traceId = "trace-abc123xyz",
        data = SplashResponse(
            isAuthenticated = true,
            versionStatus = "UP_TO_DATE",
            user = User(
                id = "cust_001",
                name = "Dedy Wijaya",
                email = "dedy@example.com",
                phone = "081234567890",
                photo = "https://cdn.example.com/users/dedy.png"
            ),
            config = AppConfig(
                minVersion = 1,
                latestVersion = 5,
                forceUpdate = false,
                maintenanceMode = false,
                maintenanceMessage = null
            )
        )
    )

}