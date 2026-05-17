/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SplashContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.auth.contract

import com.mtv.app.shopme.domain.model.Splash
import com.mtv.based.core.network.utils.LoadState

data class SplashUiState(
    val splash: LoadState<Splash> = LoadState.Loading,
    val blockingState: SplashBlockingState? = null,
)

sealed class SplashEvent {
    data object Load : SplashEvent()
    data object CloseApp : SplashEvent()
}

sealed class SplashBlockingState {
    data class Maintenance(val message: String?) : SplashBlockingState()
    data object ForceUpdate : SplashBlockingState()
    data class Fatal(val message: String) : SplashBlockingState()
}

sealed class SplashEffect {
    data object NavigateToHome : SplashEffect()
    data object NavigateToSellerDashboard : SplashEffect()
    data object NavigateToLogin : SplashEffect()
    data object ExitApp : SplashEffect()
}
