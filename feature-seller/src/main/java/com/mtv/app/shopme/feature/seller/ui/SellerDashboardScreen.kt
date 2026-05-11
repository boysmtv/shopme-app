/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerDashboardScreen.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 12.16
 */

package com.mtv.app.shopme.feature.seller.ui

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.navbar.seller.SellerBottomNavigationBar
import com.mtv.app.shopme.domain.model.SellerOrderItem
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardEvent
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardUiState
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.max

@Composable
fun SellerDashboardScreen(
    state: SellerDashboardUiState,
    event: (SellerDashboardEvent) -> Unit
) {
    val isOnline = state.isOnline
    val selectedFilter = state.selectedFilter
    val selectedSort = state.selectedSort

    val orders = state.orders

    val sortedOrders = remember(orders, selectedSort) {
        if (selectedSort == "Asc") orders.sortedBy { it.location }
        else orders.sortedByDescending { it.location }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(AppColor.Blue, AppColor.BlueSoft)
                )
            )
            .statusBarsPadding()
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColor.WhiteSoft),
        ) {
            item {
                DashboardHeader(
                    storeName = state.storeName,
                    storeAddress = state.storeAddress,
                    notificationCount = state.notificationCount,
                    isOnline = isOnline,
                    onToggle = { event(SellerDashboardEvent.ToggleOnline) },
                    onNotifClick = { event(SellerDashboardEvent.ClickNotif) }
                )
            }

            item { Spacer(Modifier.height(20.dp)) }

            if (state.isLoading && orders.isEmpty()) {
                item { SellerDashboardShimmer() }
            } else {
                item { WeeklySummaryCard(orders) }

                item { Spacer(Modifier.height(24.dp)) }

                item { WeeklyOrdersChart(orders) }

                item { Spacer(Modifier.height(20.dp)) }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OrderFilterChips(
                            selected = selectedFilter,
                            onSelected = { event(SellerDashboardEvent.ChangeFilter(it)) })

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Asc", "Desc").forEach { sortOption ->
                                val isSelected = sortOption == selectedSort
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(
                                            if (isSelected) AppColor.Blue
                                            else Color.LightGray.copy(alpha = 0.3f)
                                        )
                                        .clickable { event(SellerDashboardEvent.ChangeSort(sortOption)) }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        sortOption,
                                        color = if (isSelected) AppColor.White else Color.Black,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }

                items(sortedOrders.size) { index ->
                    val order = sortedOrders[index]
                    if (matchesOrderFilter(selectedFilter, order.status)) {
                        ModernOrderItemCompact(
                            invoice = order.invoice,
                            customer = order.customer,
                            total = order.total,
                            date = order.date,
                            time = order.time,
                            paymentMethod = order.paymentMethod,
                            status = order.status,
                            location = order.location,
                            onClick = { event(SellerDashboardEvent.ClickOrderDetail(order.id)) }
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }

}

@Composable
private fun SellerDashboardShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.LightGray.copy(alpha = 0.25f))
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.LightGray.copy(alpha = 0.2f))
        )
        repeat(2) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(104.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.LightGray.copy(alpha = 0.2f))
            )
        }
    }
}

@Composable
fun ModernOrderItemCompact(
    invoice: String,
    customer: String,
    total: String,
    date: String,
    time: String,
    paymentMethod: String,
    status: String,
    location: String,
    onClick: () -> Unit
) {
    val statusColor = when (status.lowercase()) {
        "completed" -> Color(0xFF4CAF50)
        "ordered", "pending", "unpaid" -> Color(0xFFFF5722)
        "cooking" -> Color(0xFFFF8A65)
        "delivering" -> Color(0xFF29B6F6)
        "cancelled", "canceled" -> Color(0xFFFF0D00)
        else -> AppColor.Blue
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(customer, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(location, fontSize = 12.sp, color = Color.Gray, maxLines = 1)
                    Spacer(Modifier.height(4.dp))
                    Text(invoice, fontSize = 11.sp, color = Color.Gray, maxLines = 1)
                }
                Text(total, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }

            Spacer(Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("$time - $date", fontSize = 11.sp, color = Color.Gray)
                    Spacer(Modifier.width(12.dp))
                    Icon(
                        Icons.Default.Payment,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(paymentMethod, fontSize = 11.sp, color = Color.Gray)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(statusColor.copy(alpha = 0.15f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(status, color = statusColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun DashboardHeader(
    storeName: String,
    storeAddress: String,
    notificationCount: Int,
    isOnline: Boolean,
    onToggle: () -> Unit,
    onNotifClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(AppColor.Blue, AppColor.BlueMedium)
                )
            )
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = storeName.ifBlank { "Seller Store" },
                        color = AppColor.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = storeAddress.ifBlank { "-" },
                        color = AppColor.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isOnline) Color.Green else Color.Red)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            if (isOnline) "Online" else "Offline",
                            color = AppColor.White,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(Modifier.weight(1f))
                NotificationBadge(
                    count = notificationCount,
                    onNotifClick = { onNotifClick() }
                )
            }

            Switch(
                checked = isOnline,
                onCheckedChange = { onToggle() }
            )
        }
    }
}

@Composable
fun NotificationBadge(
    count: Int,
    onNotifClick: () -> Unit
) {
    Box(
        modifier = Modifier.clickable { onNotifClick() }
    ) {
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = null,
            tint = AppColor.White,
            modifier = Modifier
                .size(38.dp)
        )

        if (count > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp, y = (-6).dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.Red),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = count.toString(),
                    color = AppColor.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun WeeklySummaryCard(orders: List<SellerOrderItem>) {
    val totalOrders = orders.size
    val totalRevenue = orders.sumOf { parseCurrencyAmount(it.total) }
    val completedOrders = orders.count { it.status.equals("COMPLETED", ignoreCase = true) }

    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(Modifier.padding(20.dp)) {

            Text("Weekly Summary", fontWeight = FontWeight.SemiBold)

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                SummaryItem("Orders", totalOrders.toString())
                SummaryItem("Revenue", formatRupiah(totalRevenue))
                SummaryItem("Completed", completedOrders.toString())
            }
        }
    }
}

@Composable
fun SummaryItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold)
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun WeeklyOrdersChart(orders: List<SellerOrderItem>) {
    val weeklyCounts = buildWeeklyOrderCounts(orders)
    val data = weeklyCounts.map { it.second.toFloat() }
    val maxValue = max(data.max(), 1f)
    val days = weeklyCounts.map { it.first }

    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(Modifier.padding(20.dp)) {

            Text("Orders This Week", fontWeight = FontWeight.SemiBold)

            Spacer(Modifier.height(20.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                val width = size.width
                val height = size.height - 20.dp.toPx()
                val space = width / (data.size - 1)

                val path = Path()

                data.forEachIndexed { index, value ->
                    val x = space * index
                    val y = height - (value / maxValue) * height

                    if (index == 0) {
                        path.moveTo(x, y)
                    } else {
                        path.lineTo(x, y)
                    }

                    drawCircle(
                        color = AppColor.Blue,
                        radius = 6f,
                        center = Offset(x, y)
                    )
                }

                drawPath(
                    path = path,
                    color = AppColor.Blue,
                    style = Stroke(width = 4f)
                )

                data.forEachIndexed { index, _ ->
                    val x = space * index
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            days[index],
                            x,
                            height + 20.dp.toPx(),
                            Paint().apply {
                                textAlign = Paint.Align.CENTER
                                textSize = 30f
                                color = android.graphics.Color.BLACK
                            }
                        )
                    }
                }

                val step = maxValue / 5
                for (i in 1..5) {
                    val y = height - (i * step / maxValue) * height
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1f
                    )
                }
            }
        }
    }
}

private fun parseCurrencyAmount(total: String): Long =
    total.replace(Regex("[^0-9]"), "").toLongOrNull() ?: 0L

private fun formatRupiah(amount: Long): String =
    "Rp ${NumberFormat.getNumberInstance(Locale.US).format(amount)}"

private fun buildWeeklyOrderCounts(orders: List<SellerOrderItem>): List<Pair<String, Int>> {
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH)
    val today = LocalDate.now()
    val dates = (6 downTo 0).map { today.minusDays(it.toLong()) }
    val counts = dates.associateWith { 0 }.toMutableMap()

    orders.forEach { order ->
        val parsedDate = runCatching { LocalDate.parse(order.date, formatter) }.getOrNull() ?: return@forEach
        if (parsedDate in counts.keys) {
            counts[parsedDate] = (counts[parsedDate] ?: 0) + 1
        }
    }

    return dates.map { date ->
        date.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() } to (counts[date] ?: 0)
    }
}

@Composable
private fun OrderFilterChips(
    selected: String,
    onSelected: (String) -> Unit
) {
    val filters = listOf("All", "ORDERED", "COOKING", "DELIVERING", "COMPLETED", "CANCELLED")

    LazyRow(
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
                    filter.toUiFilterLabel(),
                    color = if (isSelected) AppColor.White else Color.Black,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

private fun matchesOrderFilter(selectedFilter: String, status: String): Boolean {
    if (selectedFilter == "All") return true
    return status.equals(selectedFilter, ignoreCase = true)
}

private fun String.toUiFilterLabel(): String = when (this) {
    "ORDERED" -> "Ordered"
    "COOKING" -> "Cooking"
    "DELIVERING" -> "Delivering"
    "COMPLETED" -> "Completed"
    "CANCELLED" -> "Cancelled"
    else -> this
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SellerDashboardPreview() {
    val navController = rememberNavController()

    val mockOrders = listOf(
        SellerOrderItem(
            id = "1",
            invoice = "INV-001",
            customer = "John Doe",
            total = "Rp 120.000",
            date = "18 Feb 2026",
            time = "09:45",
            paymentMethod = "Transfer",
            status = "Pending",
            location = "Puri Lestari"
        ),
        SellerOrderItem(
            id = "2",
            invoice = "INV-002",
            customer = "Jane Smith",
            total = "Rp 250.000",
            date = "17 Feb 2026",
            time = "14:30",
            paymentMethod = "E-Wallet",
            status = "Completed",
            location = "Puri Indah"
        )
    )

    Scaffold(
        bottomBar = {
            SellerBottomNavigationBar(navController)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(bottom = padding.calculateBottomPadding())
                .fillMaxSize()
                .background(AppColor.White)
        ) {

            SellerDashboardScreen(
                state = SellerDashboardUiState(
                    isLoading = false,
                    orders = mockOrders,
                    isOnline = true,
                    selectedFilter = "All",
                    selectedSort = "Asc"
                ),
                event = {}
            )
        }
    }
}
