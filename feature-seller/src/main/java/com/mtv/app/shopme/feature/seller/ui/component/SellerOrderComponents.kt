package com.mtv.app.shopme.feature.seller.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mtv.app.shopme.common.AppColor

@Composable
fun OrderFilterChips(
    selected: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = listOf("All", "ORDERED", "COOKING", "DELIVERING", "COMPLETED", "CANCELLED")

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(filters.size) { index ->
            val filter = filters[index]
            val isSelected = filter.equals(selected, ignoreCase = true)

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (isSelected) AppColor.Blue
                        else Color.LightGray.copy(alpha = 0.3f)
                    )
                    .clickable { onSelected(filter) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = filter.toOrderFilterLabel(),
                    color = if (isSelected) AppColor.White else Color.Black,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

fun String.toOrderFilterLabel(): String = when (this) {
    "ORDERED" -> "Ordered"
    "COOKING" -> "Cooking"
    "DELIVERING" -> "Delivering"
    "COMPLETED" -> "Completed"
    "CANCELLED" -> "Cancelled"
    else -> this
}

fun matchesOrderFilter(selectedFilter: String, status: String): Boolean {
    if (selectedFilter == "All") return true
    return status.equals(selectedFilter, ignoreCase = true)
}
