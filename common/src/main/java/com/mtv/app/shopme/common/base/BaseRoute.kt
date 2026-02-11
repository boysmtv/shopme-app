/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: BaseRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.47
 */

package com.mtv.app.shopme.common.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.mtv.app.core.provider.based.BaseUiState
import com.mtv.app.core.provider.based.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/** BASE ROUTE */
@Composable
inline fun <reified VM, UI_STATE, UI_DATA> BaseRoute(
    content: @Composable (
        vm: VM,
        baseUiState: BaseUiState,
        uiState: UI_STATE,
        uiData: UI_DATA
    ) -> Unit
) where VM : BaseViewModel, VM : UiOwner<UI_STATE, UI_DATA> {

    val vm: VM = hiltViewModel()

    val baseUiState by vm.baseUiState.collectAsState()
    val uiState by vm.uiState.collectAsState()
    val uiData by vm.uiData.collectAsState()

    content(vm, baseUiState, uiState, uiData)
}

/** UiStateOwner */
interface UiStateOwner<UI_STATE> {
    val uiState: StateFlow<UI_STATE>
}

/** UiDataOwner */
interface UiDataOwner<UI_DATA> {
    val uiData: StateFlow<UI_DATA>
}

/** UiOwner */
interface UiOwner<UI_STATE, UI_DATA> :
    UiStateOwner<UI_STATE>,
    UiDataOwner<UI_DATA>