/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: BaseEventViewModel.kt
 *
 * Last modified by Dedy Wijaya on 25/03/26 10.28
 */

package com.mtv.app.shopme.core.base

import androidx.lifecycle.viewModelScope
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.based.BaseViewModel
import com.mtv.based.core.provider.utils.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
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
        shouldForceLogout: (UiError) -> Boolean = { it is UiError.Unauthorized },
        onOtherError: (UiError) -> Unit
    ) {
        if (shouldForceLogout(error)) {
            beforeLogout?.invoke()
            viewModelScope.launch {
                sessionManager.forceLogout()
            }
            return
        }
        onOtherError(error)
    }

    protected fun <T> observeIndependentDataFlow(
        flow: Flow<Resource<T>>,
        onLoad: (() -> Unit)? = null,
        onError: ((UiError) -> Unit)? = null,
        onSuccess: ((T) -> Unit)? = null,
        onState: ((LoadState<T>) -> Unit)? = null
    ) {
        viewModelScope.launch {
            flow.collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        onLoad?.invoke()
                        onState?.invoke(LoadState.Loading)
                    }

                    is Resource.Success -> {
                        onSuccess?.invoke(result.data)
                        onState?.invoke(LoadState.Success(result.data))
                    }

                    is Resource.Error -> {
                        onError?.invoke(result.error)
                        onState?.invoke(LoadState.Error(result.error))
                    }

                    else -> Unit
                }
            }
        }
    }
}
