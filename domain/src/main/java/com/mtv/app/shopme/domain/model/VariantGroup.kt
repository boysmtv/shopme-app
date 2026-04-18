/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: VariantGroup.kt
 *
 * Last modified by Dedy Wijaya on 15/04/26 16.08
 */

package com.mtv.app.shopme.domain.model

data class VariantGroup(
    val name: String = "",
    val options: List<VariantOption> = emptyList()
)