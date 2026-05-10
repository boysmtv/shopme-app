package com.mtv.app.shopme.feature.auth

import com.mtv.app.shopme.common.config.AppInfoProvider
import com.mtv.app.shopme.domain.model.AppConfig
import com.mtv.app.shopme.domain.model.Splash
import com.mtv.app.shopme.domain.param.SplashParam
import com.mtv.app.shopme.domain.usecase.GetSplashUseCase
import com.mtv.app.shopme.feature.auth.contract.SplashBlockingState
import com.mtv.app.shopme.feature.auth.contract.SplashEffect
import com.mtv.app.shopme.feature.auth.contract.SplashEvent
import com.mtv.app.shopme.feature.auth.presentation.SplashViewModel
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.provider.utils.SecurePrefs
import com.mtv.based.core.provider.utils.device.DeviceInfo
import com.mtv.based.core.provider.utils.device.DeviceInfoProvider
import com.mtv.based.core.provider.utils.device.InstallationIdProvider
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SplashViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val securePrefs: SecurePrefs = mockk(relaxed = true)
    private val installationIdProvider: InstallationIdProvider = mockk()
    private val deviceInfoProvider: DeviceInfoProvider = mockk()
    private val appInfoProvider: AppInfoProvider = mockk()
    private val splashUseCase: GetSplashUseCase = mockk()

    @Test
    fun `load should block navigation when maintenance mode active`() = runTest {
        val vm = viewModel(
            splash = Splash(
                isAuthenticated = true,
                versionStatus = "OK",
                user = null,
                config = AppConfig(
                    minVersion = 1,
                    latestVersion = 1,
                    forceUpdate = false,
                    maintenanceMode = true,
                    maintenanceMessage = "Balik lagi jam 10"
                )
            )
        )

        vm.onEvent(SplashEvent.Load)
        advanceUntilIdle()

        val blockingState = vm.uiState.value.blockingState
        assertTrue(blockingState is SplashBlockingState.Maintenance)
        assertEquals("Balik lagi jam 10", (blockingState as SplashBlockingState.Maintenance).message)
    }

    @Test
    fun `load should block navigation when backend requires force update`() = runTest {
        val vm = viewModel(
            splash = Splash(
                isAuthenticated = true,
                versionStatus = "FORCE_UPDATE",
                user = null,
                config = AppConfig(
                    minVersion = 10,
                    latestVersion = 11,
                    forceUpdate = false,
                    maintenanceMode = false,
                    maintenanceMessage = null
                )
            )
        )

        vm.onEvent(SplashEvent.Load)
        advanceUntilIdle()

        assertTrue(vm.uiState.value.blockingState is SplashBlockingState.ForceUpdate)
    }

    @Test
    fun `load should navigate home when authenticated and not blocked`() = runTest {
        val vm = viewModel(
            splash = Splash(
                isAuthenticated = true,
                versionStatus = "OK",
                user = null,
                config = AppConfig(
                    minVersion = 1,
                    latestVersion = 1,
                    forceUpdate = false,
                    maintenanceMode = false,
                    maintenanceMessage = null
                )
            )
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(SplashEvent.Load)
        advanceUntilIdle()

        assertEquals(SplashEffect.NavigateToHome, effect.await())
    }

    private fun viewModel(splash: Splash): SplashViewModel {
        every { installationIdProvider.getInstallationId() } returns "install-1"
        every { securePrefs.getString(any()) } returns ""
        every { appInfoProvider.versionName } returns "1.0.0"
        every { appInfoProvider.versionCode } returns 1
        every { deviceInfoProvider.getAllDeviceInfo() } returns DeviceInfo(
            androidId = "android-id",
            brand = "brand",
            manufacturer = "manufacturer",
            model = "model",
            device = "device",
            hardware = "hardware",
            supportedAbis = "arm64-v8a",
            sdkVersion = 34,
            androidVersion = "14",
            screenWidth = 1080,
            screenHeight = 2400,
            densityDpi = 440,
            locale = "id_ID",
            timezone = "Asia/Jakarta",
            batteryLevel = 80,
            networkType = "wifi",
            freeStorageMb = 1024,
            freeRamMb = 2048,
            appVersionName = "1.0.0",
            appVersionCode = 1,
            firstInstallTime = 1,
            lastUpdateTime = 1
        )
        every {
            splashUseCase.invoke(any<SplashParam>())
        } returns flowOf(Resource.Success(splash))

        return SplashViewModel(
            securePrefs = securePrefs,
            installationIdProvider = installationIdProvider,
            deviceInfoProvider = deviceInfoProvider,
            appInfoProvider = appInfoProvider,
            splashUseCase = splashUseCase
        )
    }
}
