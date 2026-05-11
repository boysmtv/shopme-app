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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.based.core.provider.based.BaseUiState
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogCenterV1

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
                Column(
                    modifier = Modifier
                        .width(220.dp)
                        .background(Color.White, RoundedCornerShape(24.dp))
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(28.dp),
                        strokeWidth = 3.dp,
                        color = AppColor.Green
                    )
                    Text(
                        text = "Processing...",
                        fontFamily = PoppinsFont,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
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
