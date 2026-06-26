package com.mtv.app.shopme.core.base

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.error.ApiException
import com.mtv.app.shopme.domain.model.Resource
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.based.BaseViewModel
import com.mtv.based.core.provider.utils.SessionManager
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseEventViewModel<EVENT, EFFECT> : BaseViewModel() {

    private val _effect = MutableSharedFlow<EFFECT>(extraBufferCapacity = 5)
    val effect = _effect.asSharedFlow()

    private var observationJob: Job? = null

    abstract fun onEvent(event: EVENT)

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
            viewModelScope.launch(Dispatchers.Main + NonCancellable) {
                sessionManager.forceLogout()
            }
            return
        }
        onOtherError(error)
    }

    @JvmName("observeDataFlowDomain")
    protected fun <T> observeDataFlow(
        flow: Flow<Resource<T>>,
        onLoad: (() -> Unit)? = null,
        onSuccess: ((T) -> Unit)? = null,
        onError: ((UiError) -> Unit)? = null,
        onState: ((LoadState<T>) -> Unit)? = null
    ) {
        observeIndependentDataFlow(flow, onLoad, onError, onSuccess, onState)
    }

    protected fun <T> observeIndependentDataFlow(
        flow: Flow<Resource<T>>,
        onLoad: (() -> Unit)? = null,
        onError: ((UiError) -> Unit)? = null,
        onSuccess: ((T) -> Unit)? = null,
        onState: ((LoadState<T>) -> Unit)? = null
    ) {
        observationJob?.cancel()
        observationJob = viewModelScope.launch {
            flow.collect { result ->
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
                        val uiError = mapToUiError(result.throwable)
                        onError?.invoke(uiError)
                        onState?.invoke(LoadState.Error(uiError))
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun mapToUiError(throwable: Throwable?): UiError {
        return when (throwable) {
            is com.mtv.app.shopme.core.error.ApiException.Unauthorized ->
                UiError.Unauthorized(message = throwable.message ?: "")
            is com.mtv.app.shopme.core.error.ApiException.Forbidden ->
                UiError.Forbidden(message = throwable.message ?: "")
            is com.mtv.app.shopme.core.error.ApiException.Validation ->
                UiError.Validation(message = throwable.message ?: "")
            is com.mtv.app.shopme.core.error.ApiException.ServerError ->
                UiError.Server(message = throwable.message ?: "")
            is java.io.IOException ->
                UiError.Network(message = throwable.message ?: "")
            else -> UiError.Unknown(
                message = throwable?.message ?: com.mtv.based.core.network.utils.ErrorMessages.GENERIC_ERROR
            )
        }
    }
}
