package com.mtv.app.shopme.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.mtv.app.core.provider.based.BaseUiState
import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SecurePrefs
import com.mtv.based.core.network.utils.ErrorMessages
import kotlin.collections.toMutableList
import kotlin.getOrElse
import kotlin.runCatching
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import kotlinx.serialization.serializer

/** HELPER MAPPING FLOW */
@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
fun <S, T> MutableStateFlow<S>.valueFlowOf(
    get: (S) -> T,
    set: S.(T) -> S
): MutableStateFlow<T> {
    val parent = this
    return object : MutableStateFlow<T> by MutableStateFlow(get(parent.value)) {
        override var value: T
            get() = get(parent.value)
            set(v) {
                parent.value = parent.value.set(v)
            }
    }
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

/** GENERIC UPDATE DATA */
fun <T> updateUiDataListener(stateFlow: MutableStateFlow<T>, block: T.() -> T) {
    stateFlow.update { it.block() }
}