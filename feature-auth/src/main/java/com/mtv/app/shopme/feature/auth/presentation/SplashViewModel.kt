/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SplashViewModel.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 14.12
 */

package com.mtv.app.shopme.feature.auth.presentation

import com.mtv.app.shopme.common.Constant.PLATFORM
import com.mtv.app.shopme.common.ConstantPreferences.FCM_TOKEN
import com.mtv.app.shopme.common.ConstantPreferences.SPLASH_RESPONSE
import com.mtv.app.shopme.common.config.AppInfoProvider
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.model.Splash
import com.mtv.app.shopme.domain.param.SplashParam
import com.mtv.app.shopme.domain.usecase.GetSplashUseCase
import com.mtv.app.shopme.feature.auth.contract.SplashBlockingState
import com.mtv.app.shopme.feature.auth.contract.SplashEffect
import com.mtv.app.shopme.feature.auth.contract.SplashEvent
import com.mtv.app.shopme.feature.auth.contract.SplashUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SecurePrefs
import com.mtv.based.core.provider.utils.device.DeviceInfoProvider
import com.mtv.based.core.provider.utils.device.InstallationIdProvider
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val securePrefs: SecurePrefs,
    private val installationIdProvider: InstallationIdProvider,
    private val deviceInfoProvider: DeviceInfoProvider,
    private val appInfoProvider: AppInfoProvider,
    private val splashUseCase: GetSplashUseCase
) : BaseEventViewModel<SplashEvent, SplashEffect>() {

    private val _state = MutableStateFlow(SplashUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SplashEvent) {
        when (event) {
            SplashEvent.Load -> doSplash()
            SplashEvent.CloseApp -> emitEffect(SplashEffect.ExitApp)
        }
    }

    private fun doSplash() {
        observeDataFlow(
            flow = splashUseCase(
                SplashParam(
                    deviceId = installationIdProvider.getInstallationId(),
                    fcmToken = securePrefs.getString(FCM_TOKEN),
                    appVersionName = appInfoProvider.versionName,
                    appVersionCode = appInfoProvider.versionCode,
                    deviceInfo = deviceInfoProvider.getAllDeviceInfo(),
                    platform = PLATFORM,
                    createdAt = System.currentTimeMillis().toString()
                )
            ),
            onState = { state ->
                _state.update {
                    it.copy(
                        splash = state,
                        blockingState = if (state is LoadState.Loading) null else it.blockingState
                    )
                }

                if (state is LoadState.Success) {
                    securePrefs.putObject(SPLASH_RESPONSE, state.data)
                    handleNavigation(state.data)
                }
            },
            onError = {
                showError(it)
            }
        )
    }

    private fun handleNavigation(data: Splash) {
        when {
            data.config.maintenanceMode -> {
                _state.update {
                    it.copy(
                        blockingState = SplashBlockingState.Maintenance(
                            message = data.config.maintenanceMessage
                        )
                    )
                }
            }

            data.versionStatus == "FORCE_UPDATE" || data.config.forceUpdate -> {
                _state.update { it.copy(blockingState = SplashBlockingState.ForceUpdate) }
            }

            data.isAuthenticated -> {
                emitEffect(SplashEffect.NavigateToHome)
            }

            else -> {
                emitEffect(SplashEffect.NavigateToLogin)
            }
        }
    }

    private fun showError(error: UiError) {
        _state.update {
            it.copy(
                splash = LoadState.Error(error),
                blockingState = SplashBlockingState.Fatal(
                    message = error.message.ifBlank { ErrorMessages.GENERIC_ERROR }
                )
            )
        }
    }

}
