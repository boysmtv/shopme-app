/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ItemExtension.kt
 *
 * Last modified by Dedy Wijaya on 19/03/26 03.12
 */

package com.mtv.app.shopme.feature.customer.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.data.dto.FoodStatus

@Composable
fun StatusStatItem(
    status: FoodStatus,
) {
    val (icon, color, text) = when (status) {
        FoodStatus.READY -> Triple(
            Icons.Default.CheckCircle,
            Color(0xFF4CAF50),
            "Ready"
        )

        FoodStatus.PREORDER -> Triple(
            Icons.Default.Schedule,
            Color(0xFFFF9800),
            "Pre-order"
        )

        FoodStatus.JASTIP -> Triple(
            Icons.Default.LocalShipping,
            Color(0xFF2196F3),
            "Jastip"
        )

        FoodStatus.UNKNOWN -> Triple(
            Icons.Default.Downloading,
            Color(0xFFA3A3A3),
            "Loading"
        )
    }

    Row(
        modifier = Modifier
            .background(
                color = color.copy(alpha = 0.12f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = color,
            fontFamily = PoppinsFont
        )
    }
}

@Composable
fun StatItem(
    icon: ImageVector,
    text: String
) {
    Row(
        modifier = Modifier
            .background(
                color = AppColor.Green.copy(alpha = 0.12f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppColor.Green,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = AppColor.Green,
            fontFamily = PoppinsFont
        )
    }
}
