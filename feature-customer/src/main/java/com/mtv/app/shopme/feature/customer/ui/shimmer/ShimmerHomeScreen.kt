/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ShimmerHomeScreen.kt
 *
 * Last modified by Dedy Wijaya on 27/03/26 09.53
 */

package com.mtv.app.shopme.feature.customer.ui.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mtv.app.shopme.common.shimmerBrush

@Composable
fun ShimmerHomeHeaderSkeleton() {
    val brush = shimmerBrush()

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            // Avatar / Icon placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(brush)
            )

            Column(modifier = Modifier.padding(start = 12.dp)) {

                // Address skeleton
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )

                Spacer(Modifier.height(6.dp))

                // Name skeleton
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
            }
        }

        // Notification icon skeleton
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(brush)
        )
    }
}
