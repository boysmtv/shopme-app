package com.mtv.app.shopme.common

import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.network.utils.UiError
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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

fun BigDecimal.toRupiah(): String {
    val formatter = NumberFormat.getNumberInstance(Locale.Builder().setLanguage("id").setRegion("ID").build())
    formatter.maximumFractionDigits = 0
    formatter.minimumFractionDigits = 0
    return "Rp. ${formatter.format(this)}"
}

fun Double.toRupiah(): String = BigDecimal.valueOf(this).toRupiah()

fun Long.toRupiah(): String = BigDecimal.valueOf(this).toRupiah()

fun Int.toRupiah(): String = toLong().toRupiah()
