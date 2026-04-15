/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ProfileMenuItem.kt
 *
 * Last modified by Dedy Wijaya on 15/04/26 22.02
 */

package com.mtv.app.shopme.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector


@Immutable
data class ProfileMenuItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)