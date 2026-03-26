/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: BaseScreen.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.49
 */

package com.mtv.app.shopme.common.base

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mtv.based.core.provider.based.BaseUiState
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogCenterV1
import com.mtv.based.uicomponent.core.component.loading.LoadingV2

@Composable
fun BaseScreen(
    baseUiState: BaseUiState,
    dismissDialog: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        content()
        if (baseUiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f))
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingV2()
            }
        }
        baseUiState.dialog?.let { dialog ->
            when (dialog) {
                is UiDialog.Center -> {
                    DialogCenterV1(
                        state = dialog.state,
                        onPrimaryClick = dialog.onPrimary,
                        onSecondaryClick = dialog.onSecondary,
                        onDismiss = {
                            dialog.onDismiss?.invoke()
                            dismissDialog()
                        }
                    )
                }
            }
        }
    }
}
