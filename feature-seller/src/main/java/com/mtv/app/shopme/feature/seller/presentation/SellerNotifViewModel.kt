/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerNotifViewModel.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.27
 */

package com.mtv.app.shopme.feature.seller.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeEventType
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeGateway
import com.mtv.app.shopme.domain.model.SellerNotifItem
import com.mtv.app.shopme.domain.usecase.ClearNotificationsUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerNotificationsUseCase
import com.mtv.app.shopme.feature.seller.contract.*
import com.mtv.based.core.network.utils.ResourceFirebase
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SellerNotifViewModel @Inject constructor(
    private val getSellerNotificationsUseCase: GetSellerNotificationsUseCase,
    private val clearNotificationsUseCase: ClearNotificationsUseCase,
    private val realtimeGateway: ShopmeRealtimeGateway,
    private val sessionManager: SessionManager,
) : BaseEventViewModel<SellerNotifEvent, SellerNotifEffect>() {

    private val _state = MutableStateFlow(SellerNotifUiState())
    val uiState = _state.asStateFlow()
    private var realtimeRetained = false

    init {
        viewModelScope.launch {
            realtimeGateway.events.collectLatest { event ->
                if (event.type == ShopmeRealtimeEventType.NOTIFICATION_CREATED) {
                    if (!applyNotificationDelta(event.title, event.message, event.notificationId, event.occurredAt)) {
                        getNotifications()
                    }
                }
            }
        }
    }

    override fun onEvent(event: SellerNotifEvent) {
        when (event) {
            SellerNotifEvent.Load, SellerNotifEvent.GetNotification -> getNotifications()
            SellerNotifEvent.LoadMore -> loadMore()
            SellerNotifEvent.DismissDialog -> dismissDialog()
            SellerNotifEvent.ClearNotification -> clearNotifications()
            is SellerNotifEvent.ClickNotification -> {
                if (event.item.orderId.isNotBlank()) {
                    emitEffect(SellerNotifEffect.NavigateToOrderDetail(event.item.orderId))
                }
            }
            SellerNotifEvent.ClickBack -> emitEffect(SellerNotifEffect.NavigateBack)
        }
    }

    private fun getNotifications() {
        loadNotifications(page = 0, append = false)
    }

    private fun loadMore() {
        val state = _state.value
        if (state.isLoadingMore || state.isLastPage || state.notificationState is ResourceFirebase.Loading) return
        loadNotifications(page = state.page + 1, append = true)
    }

    private fun loadNotifications(page: Int, append: Boolean) {
        retainRealtime()
        observeDataFlow(
            flow = getSellerNotificationsUseCase(page, NOTIFICATION_PAGE_SIZE),
            onState = { state ->
                _state.update {
                    when (state) {
                        is com.mtv.based.core.network.utils.LoadState.Loading -> it.copy(
                            notificationState = if (append) it.notificationState else ResourceFirebase.Loading,
                            isLoadingMore = append
                        )
                        is com.mtv.based.core.network.utils.LoadState.Success -> {
                            val merged = if (append) {
                                (it.notifications + state.data.content).distinctBy { item ->
                                    listOf(item.orderId, item.title, item.date, item.time, item.message).joinToString("|")
                                }
                            } else {
                                state.data.content
                            }
                            it.copy(
                                notifications = merged,
                                notificationState = ResourceFirebase.Success(""),
                                page = state.data.page,
                                isLastPage = state.data.last,
                                isLoadingMore = false
                            )
                        }
                        is com.mtv.based.core.network.utils.LoadState.Error -> it.copy(
                            notificationState = ResourceFirebase.Success(""),
                            isLoadingMore = false
                        )
                        else -> it.copy(notificationState = ResourceFirebase.Success(""), isLoadingMore = false)
                    }
                }
            },
            onError = ::showError
        )
    }

    private fun clearNotifications() {
        observeDataFlow(
            flow = clearNotificationsUseCase(),
            onState = { state ->
                _state.update {
                    it.copy(
                        notifications = if (state is com.mtv.based.core.network.utils.LoadState.Success) emptyList() else it.notifications,
                        activeDialog = if (state is com.mtv.based.core.network.utils.LoadState.Success) SellerNotifDialog.Success else it.activeDialog,
                        page = if (state is com.mtv.based.core.network.utils.LoadState.Success) 0 else it.page,
                        isLastPage = if (state is com.mtv.based.core.network.utils.LoadState.Success) true else it.isLastPage,
                        isLoadingMore = false,
                        notificationState = when (state) {
                            is com.mtv.based.core.network.utils.LoadState.Loading -> ResourceFirebase.Loading
                            is com.mtv.based.core.network.utils.LoadState.Success -> ResourceFirebase.Success("")
                            is com.mtv.based.core.network.utils.LoadState.Error -> ResourceFirebase.Success("")
                            else -> ResourceFirebase.Success("")
                        }
                    )
                }
            },
            onError = ::showError
        )
    }

    private fun applyNotificationDelta(
        title: String?,
        message: String?,
        notificationId: String?,
        occurredAt: String?
    ): Boolean {
        val current = _state.value.notifications
        if (current.isEmpty()) return false

        val (date, time) = occurredAt.toDateTimeParts()
        _state.update {
            it.copy(
                notifications = listOf(
                    SellerNotifItem(
                        title = title.orEmpty().ifBlank { "Notifikasi Baru" },
                        message = message.orEmpty(),
                        orderId = notificationId.orEmpty(),
                        buyerName = title.orEmpty().ifBlank { "Notifikasi Baru" },
                        date = date,
                        time = time,
                        isRead = false
                    )
                ) + current,
                notificationState = ResourceFirebase.Success("")
            )
        }
        return true
    }

    private fun String?.toDateTimeParts(): Pair<String, String> {
        val raw = this.orEmpty()
        val date = raw.substringBefore("T", "").ifBlank { "now" }
        val time = raw.substringAfter("T", "").take(8).ifBlank { "now" }
        return date to time
    }

    private fun showError(error: UiError) {
        handleSessionError(
            error = error,
            sessionManager = sessionManager,
            beforeLogout = {
                _state.update {
                    it.copy(notificationState = ResourceFirebase.Success(""), activeDialog = null)
                }
            }
        ) { uiError ->
            _state.update {
                it.copy(
                    activeDialog = SellerNotifDialog.Error(uiError.message),
                    notificationState = ResourceFirebase.Success(""),
                    isLoadingMore = false
                )
            }
        }
    }

    private fun retainRealtime() {
        if (realtimeRetained) return
        realtimeRetained = true
        realtimeGateway.retain()
    }

    override fun onCleared() {
        if (realtimeRetained) {
            realtimeGateway.release()
        }
        super.onCleared()
    }

    private companion object {
        const val NOTIFICATION_PAGE_SIZE = 20
    }
}
