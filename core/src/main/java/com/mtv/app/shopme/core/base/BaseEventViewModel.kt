/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: BaseEventViewModel.kt
 *
 * Last modified by Dedy Wijaya on 25/03/26 10.28
 */

package com.mtv.app.shopme.core.base

import androidx.lifecycle.viewModelScope
import com.mtv.based.core.provider.based.BaseViewModel
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SessionManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseEventViewModel<EVENT, EFFECT> : BaseViewModel() {

    private val _effect = MutableSharedFlow<EFFECT>(extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    /**
     * 🔥 entry point semua event
     */
    abstract fun onEvent(event: EVENT)

    /**
     * 🔥 emit effect (navigation, one-time action)
     */
    protected fun emitEffect(effect: EFFECT) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    protected fun handleSessionError(
        error: UiError,
        sessionManager: SessionManager,
        beforeLogout: (() -> Unit)? = null,
        onOtherError: (UiError) -> Unit
    ) {
        if (error is UiError.Unauthorized) {
            beforeLogout?.invoke()
            viewModelScope.launch {
                sessionManager.forceLogout()
            }
            return
        }
        onOtherError(error)
    }
}
