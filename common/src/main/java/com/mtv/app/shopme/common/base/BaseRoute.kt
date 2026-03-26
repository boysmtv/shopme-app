/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: BaseRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.47
 */

package com.mtv.app.shopme.common.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.mtv.app.shopme.core.base.BaseEventViewModel
import kotlinx.coroutines.flow.collectLatest

/** BASE ROUTE */
@Composable
fun <EVENT, EFFECT> BaseRoute(
    viewModel: BaseEventViewModel<EVENT, EFFECT>,
    onLoad: EVENT? = null,
    onEffect: (EFFECT) -> Unit,
    onEvent: (EVENT) -> Unit,
    content: @Composable () -> Unit
) {
    LaunchedEffect(true) {
        onLoad?.let { onEvent(it) }
    }
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            onEffect(effect)
        }
    }
    content.invoke()
}