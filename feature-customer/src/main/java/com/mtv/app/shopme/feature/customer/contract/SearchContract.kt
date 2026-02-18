/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SearchContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.based.core.network.utils.ResourceFirebase

data class SearchStateListener(val loading: Boolean = false)
data class SearchDataListener(val query: String = "", val results: List<String> = emptyList())
data class SearchEventListener(val onQueryChanged: (String) -> Unit = {})
data class SearchNavigationListener(
    val onDetailClick: () -> Unit = {},
    val onBack: () -> Unit = {},
)

