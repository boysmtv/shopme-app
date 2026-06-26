package com.mtv.app.shopme.common

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

fun BigDecimal.toRupiah(): String {
    val formatter = NumberFormat.getNumberInstance(Locale.Builder().setLanguage("id").setRegion("ID").build())
    formatter.maximumFractionDigits = 0
    formatter.minimumFractionDigits = 0
    return "Rp. ${formatter.format(this)}"
}

fun Double.toRupiah(): String = BigDecimal.valueOf(this).toRupiah()

fun Long.toRupiah(): String = BigDecimal.valueOf(this).toRupiah()

fun Int.toRupiah(): String = toLong().toRupiah()
