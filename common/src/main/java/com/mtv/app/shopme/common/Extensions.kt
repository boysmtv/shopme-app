package com.mtv.app.shopme.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.based.BaseUiState
import com.mtv.based.core.provider.based.BaseViewModel
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map


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

fun BigDecimal.toRupiah(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.Builder().setLanguage("id").setRegion("ID").build())
    formatter.maximumFractionDigits = 0
    return formatter.format(this).replace("Rp", "Rp ")
}

fun <T : Any> Flow<T?>.requireNotNull(): Flow<T> =
    map { it ?: throw IllegalStateException("Required value was null") }

fun isExpired(updatedAt: Long, ttl: Long): Boolean {
    return System.currentTimeMillis() - updatedAt > ttl
}

inline fun <T> Resource<T>.onSuccess(
    action: (data: T, isFresh: Boolean) -> Unit
): Resource<T> {
    if (this is Resource.Success) action(data, isFresh)
    return this
}

inline fun <T> Resource<T>.onError(action: (UiError, T?) -> Unit): Resource<T> {
    if (this is Resource.Error) action(error, data)
    return this
}

inline fun <T> Resource<T>.onLoading(action: (T?) -> Unit): Resource<T> {
    if (this is Resource.Loading) action(data)
    return this
}

inline fun <T> Resource<T>.onIdle(action: () -> Unit): Resource<T> {
    if (this is Resource.Idle) action()
    return this
}

inline fun <T> Resource<T>.handle(
    onLoading: (T?) -> Unit = {},
    onSuccess: (T, Boolean) -> Unit = { _, _ -> },
    onError: (T?, UiError) -> Unit = { _, _ -> }
) {
    when (this) {
        is Resource.Loading -> onLoading(data)
        is Resource.Success -> onSuccess(data, isFresh)
        is Resource.Error -> onError(data, error)
        else -> {}
    }
}