package com.mtv.app.shopme.feature.seller.ui

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.ContentErrorState
import com.mtv.app.shopme.common.ShimmerBlock
import com.mtv.app.shopme.common.ShimmerLine
import com.mtv.app.shopme.common.navbar.seller.SellerBottomNavigationBar
import com.mtv.app.shopme.domain.model.ChartDataItem
import com.mtv.app.shopme.domain.model.SellerDashboard
import com.mtv.app.shopme.domain.model.SellerOrderItem
import com.mtv.app.shopme.domain.model.StatusCountItem
import com.mtv.app.shopme.domain.model.TopProductItem
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardEvent
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardUiState
import com.mtv.app.shopme.feature.seller.ui.component.OrderFilterChips
import com.mtv.app.shopme.feature.seller.ui.component.matchesOrderFilter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SellerDashboardScreen(
    state: SellerDashboardUiState,
    event: (SellerDashboardEvent) -> Unit
) {
    val isOnline = state.isOnline
    val selectedFilter = state.selectedFilter
    val selectedSort = state.selectedSort
    val orders = state.orders
    val dashboard = state.dashboard

    val visibleOrders = remember(orders, selectedFilter, selectedSort) {
        val filtered = orders.filter { matchesOrderFilter(selectedFilter, it.status) }
        if (selectedSort == "Asc") filtered.sortedBy { it.location }
        else filtered.sortedByDescending { it.location }
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = { event(SellerDashboardEvent.Refresh) }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
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

            if (state.isLoading && dashboard == null) {
                item { SellerDashboardShimmer() }
            } else if (!state.errorMessage.isNullOrBlank() && dashboard == null) {
                item {
                    ContentErrorState(
                        title = "Gagal memuat dashboard",
                        message = state.errorMessage,
                        actionLabel = "Muat ulang",
                        onRetry = { event(SellerDashboardEvent.Load) }
                    )
                }
            } else {
                dashboard?.let { data ->
                    item { RevenueCardsSection(data) }
                    item { Spacer(Modifier.height(16.dp)) }
                    item { QuickStatsSection(data) }
                    item { Spacer(Modifier.height(16.dp)) }

                    if (data.lowStockProducts > 0) {
                        item { LowStockWarning(data.lowStockProducts) }
                        item { Spacer(Modifier.height(16.dp)) }
                    }

                    item { WeeklyRevenueChart(data.weeklyRevenue) }
                    item { Spacer(Modifier.height(16.dp)) }

                    if (data.topProducts.isNotEmpty()) {
                        item { TopProductsSection(data.topProducts) }
                        item { Spacer(Modifier.height(16.dp)) }
                    }

                    item { OrderStatusBreakdown(data.orderStatusBreakdown) }
                    item { Spacer(Modifier.height(16.dp)) }
                }

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

                items(
                    items = visibleOrders,
                    key = { order -> order.id }
                ) { order ->
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

        PullRefreshIndicator(
            refreshing = state.isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun RevenueCardsSection(dashboard: SellerDashboard) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        RevenueCard(
            title = "Hari Ini",
            value = dashboard.todayRevenue,
            color = Color(0xFF4CAF50),
            modifier = Modifier.weight(1f)
        )
        RevenueCard(
            title = "Minggu Ini",
            value = dashboard.thisWeekRevenue,
            color = Color(0xFF2196F3),
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(Modifier.height(8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        RevenueCard(
            title = "Bulan Ini",
            value = dashboard.thisMonthRevenue,
            color = Color(0xFFFF9800),
            modifier = Modifier.weight(1f)
        )
        RevenueCard(
            title = "Total",
            value = dashboard.totalRevenue,
            color = Color(0xFF9C27B0),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun RevenueCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun QuickStatsSection(dashboard: SellerDashboard) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Ringkasan", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                StatItem("Total Pesanan", dashboard.totalOrders.toString())
                StatItem("Produk Aktif", dashboard.activeProducts.toString())
                StatItem("Terjual", dashboard.totalSold.toString())
                StatItem("Pending", dashboard.pendingOrders.toString())
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(label, fontSize = 11.sp, color = Color.Gray)
    }
}

@Composable
private fun LowStockWarning(count: Long) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable { },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF9800).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text("!", color = Color(0xFFFF9800), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "Stok Menipis",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color(0xFFE65100)
                )
                Text(
                    "$count produk memiliki stok tersisa sedikit",
                    fontSize = 12.sp,
                    color = Color(0xFFBF360C)
                )
            }
        }
    }
}

@Composable
private fun WeeklyRevenueChart(weeklyData: List<ChartDataItem>) {
    if (weeklyData.isEmpty()) return

    val maxValue = max(weeklyData.maxOfOrNull { it.value } ?: 1L, 1L)

    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("Pendapatan Mingguan", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(Modifier.height(16.dp))
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                val chartWidth = size.width
                val chartHeight = size.height - 40.dp.toPx()
                val barCount = weeklyData.size
                val barSpacing = 12.dp.toPx()
                val totalSpacing = barSpacing * (barCount - 1)
                val barWidth = (chartWidth - totalSpacing) / barCount

                weeklyData.forEachIndexed { index, item ->
                    val barHeight = (item.value.toFloat() / maxValue) * chartHeight
                    val x = index * (barWidth + barSpacing)
                    val y = chartHeight - barHeight

                    drawRect(
                        color = Color(0xFF1BA0E2),
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight),
                        style = Stroke(width = 0f)
                    )
                    drawRect(
                        color = Color(0xFF1BA0E2).copy(alpha = 0.8f),
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight)
                    )

                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            item.label.take(3),
                            x + barWidth / 2,
                            chartHeight + 24.dp.toPx(),
                            android.graphics.Paint().apply {
                                textAlign = android.graphics.Paint.Align.CENTER
                                textSize = 28f
                                color = android.graphics.Color.GRAY
                            }
                        )
                    }

                    val valueLabel = if (item.value >= 1_000_000) "${item.value / 1_000_000}jt" else if (item.value >= 1_000) "${item.value / 1_000}rb" else item.value.toString()
                    if (barHeight > 30.dp.toPx()) {
                        drawContext.canvas.nativeCanvas.apply {
                            drawText(
                                valueLabel,
                                x + barWidth / 2,
                                y - 8.dp.toPx(),
                                android.graphics.Paint().apply {
                                    textAlign = android.graphics.Paint.Align.CENTER
                                    textSize = 24f
                                    color = android.graphics.Color.DKGRAY
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderStatusBreakdown(items: List<StatusCountItem>) {
    val total = items.sumOf { it.count }.toFloat().coerceAtLeast(1f)
    val statusColors = mapOf(
        "UNPAID" to Color(0xFFFF5722),
        "ORDERED" to Color(0xFF2196F3),
        "COOKING" to Color(0xFFFF9800),
        "DELIVERING" to Color(0xFF29B6F6),
        "COMPLETED" to Color(0xFF4CAF50),
        "CANCELLED" to Color(0xFFF44336)
    )

    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("Status Pesanan", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val fraction = item.count / total
                    if (fraction > 0f) {
                        Box(
                            modifier = Modifier
                                .weight(fraction)
                                .fillMaxSize()
                                .clip(
                                    if (item == items.first()) RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp)
                                    else if (item == items.last()) RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
                                    else RoundedCornerShape(0.dp)
                                )
                                .background(statusColors[item.status] ?: Color.Gray)
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(statusColors[item.status] ?: Color.Gray)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            when (item.status) {
                                "UNPAID" -> "Belum Bayar"
                                "ORDERED" -> "Pesanan Baru"
                                "COOKING" -> "Dimasak"
                                "DELIVERING" -> "Diantar"
                                "COMPLETED" -> "Selesai"
                                "CANCELLED" -> "Dibatalkan"
                                else -> item.status
                            },
                            fontSize = 13.sp
                        )
                    }
                    Text(
                        item.count.toString(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun TopProductsSection(products: List<TopProductItem>) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("Produk Terlaris", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(Modifier.height(12.dp))
            products.forEachIndexed { index, product ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                when (index) {
                                    0 -> Color(0xFFFFD700)
                                    1 -> Color(0xFFC0C0C0)
                                    2 -> Color(0xFFCD7F32)
                                    else -> Color.Gray.copy(alpha = 0.3f)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "${index + 1}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (index < 3) Color.Black else Color.Gray
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            product.productName,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            product.price,
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "${product.totalSold} terjual",
                            fontSize = 12.sp,
                            color = AppColor.Blue,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            product.revenue,
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
                if (index < products.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = Color.Gray.copy(alpha = 0.15f)
                    )
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
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ShimmerLine(widthFraction = 0.34f, heightDp = 16)
                ShimmerLine(widthFraction = 0.22f, heightDp = 12)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    repeat(4) {
                        ShimmerBlock(
                            modifier = Modifier
                                .weight(1f)
                                .height(72.dp),
                            shape = RoundedCornerShape(18.dp)
                        )
                    }
                }
            }
        }
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ShimmerLine(widthFraction = 0.4f, heightDp = 16)
                repeat(3) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            ShimmerLine(widthFraction = 0.58f, heightDp = 13)
                            Spacer(Modifier.height(8.dp))
                            ShimmerLine(widthFraction = 0.3f, heightDp = 11)
                        }
                        ShimmerBlock(
                            modifier = Modifier
                                .width(70.dp)
                                .height(20.dp),
                            shape = RoundedCornerShape(50)
                        )
                    }
                }
            }
        }
        repeat(2) {
            OrderItemShimmer()
        }
    }
}

@Composable
private fun OrderItemShimmer() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    ShimmerLine(widthFraction = 0.36f, heightDp = 14)
                    Spacer(Modifier.height(8.dp))
                    ShimmerLine(widthFraction = 0.58f, heightDp = 12)
                }
                ShimmerLine(widthFraction = 0.18f, heightDp = 14)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ShimmerBlock(
                    modifier = Modifier
                        .width(84.dp)
                        .height(18.dp),
                    shape = RoundedCornerShape(10.dp)
                )
                ShimmerBlock(
                    modifier = Modifier
                        .width(96.dp)
                        .height(18.dp),
                    shape = RoundedCornerShape(10.dp)
                )
            }
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
                        contentDescription = "Date",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("$time - $date", fontSize = 11.sp, color = Color.Gray)
                    Spacer(Modifier.width(12.dp))
                    Icon(
                        Icons.Default.Payment,
                        contentDescription = "Payment",
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
            contentDescription = "Notifications",
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
                    text = min(count, 99).toString(),
                    color = AppColor.White,
                    fontSize = 12.sp
                )
            }
        }
    }
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
