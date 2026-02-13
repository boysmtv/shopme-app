/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotifViewModel.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.45
 */

package com.mtv.app.shopme.feature.presentation

import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SecurePrefs
import com.mtv.app.core.provider.utils.SessionManager
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.contract.NotifDataListener
import com.mtv.app.shopme.feature.contract.NotifStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class NotifViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val securePrefs: SecurePrefs
) : BaseViewModel(), UiOwner<NotifStateListener, NotifDataListener> {

    /** UI STATE : LOADING / ERROR / SUCCESS (API Response) */
    override val uiState = MutableStateFlow(NotifStateListener())

    /** UI DATA : DATA PERSIST (Prefs) */
    override val uiData = MutableStateFlow(NotifDataListener())

}
