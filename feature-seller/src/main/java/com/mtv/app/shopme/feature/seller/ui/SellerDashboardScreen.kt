/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerDashboardScreen.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 12.16
 */

package com.mtv.app.shopme.feature.seller.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.mtv.app.shopme.feature.seller.contract.SellerDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerStateListener
import kotlin.math.max


@Composable
fun SellerDashboardScreen(
    uiState: SellerStateListener,
    uiData: SellerDataListener,
    uiEvent: SellerEventListener,
    uiNavigation: SellerNavigationListener
) {
    var isOnline by remember { mutableStateOf(true) }
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedSort by remember { mutableStateOf("Asc") }

    val orders = listOf(
        mapOf(
            "invoice" to "INV-2026-001",
            "customer" to "John Doe",
            "total" to "Rp 120.000",
            "date" to "18 Feb 2026",
            "payment" to "Transfer Bank",
            "status" to "Pending",
            "location" to "Puri Lestari - Blok H12/12"
        ),
        mapOf(
            "invoice" to "INV-2026-002",
            "customer" to "Jane Smith",
            "total" to "Rp 250.000",
            "date" to "17 Feb 2026",
            "payment" to "E-Wallet",
            "status" to "Completed",
            "location" to "Puri Lestari - Blok H11/10"
        ),
        mapOf(
            "invoice" to "INV-2026-003",
            "customer" to "Michael Johnson",
            "total" to "Rp 180.000",
            "date" to "16 Feb 2026",
            "payment" to "Cash",
            "status" to "Cooking",
            "location" to "Puri Indah - Blok A5/3"
        ),
        mapOf(
            "invoice" to "INV-2026-004",
            "customer" to "Emily Davis",
            "total" to "Rp 300.000",
            "date" to "15 Feb 2026",
            "payment" to "Transfer Bank",
            "status" to "Cancelled",
            "location" to "Puri Lestari - Blok H13/5"
        ),
    )

    val sortedOrders = remember(orders, selectedSort) {
        if (selectedSort == "Asc") orders.sortedBy { it["location"] }
        else orders.sortedByDescending { it["location"] }
    }

    Scaffold { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F6FA))
                .padding(padding),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {

            item { DashboardHeader(isOnline) { isOnline = !isOnline } }

            item { Spacer(Modifier.height(20.dp)) }

            item { WeeklySummaryCard() }

            item { Spacer(Modifier.height(24.dp)) }

            item { WeeklyOrdersChart() }

            item { Spacer(Modifier.height(20.dp)) }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OrderFilterChips(selected = selectedFilter, onSelected = { selectedFilter = it })

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Asc", "Desc").forEach { sortOption ->
                            val isSelected = sortOption == selectedSort
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(
                                        if (isSelected) Color(0xFFFF6B00)
                                        else Color.LightGray.copy(alpha = 0.3f)
                                    )
                                    .clickable { selectedSort = sortOption }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    sortOption,
                                    color = if (isSelected) Color.White else Color.Black,
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
                if (selectedFilter == "All" || order["status"] == selectedFilter) {
                    ModernOrderItemCompact(
                        invoice = order["invoice"]!!,
                        customer = order["customer"]!!,
                        total = order["total"]!!,
                        date = order["date"]!!,
                        paymentMethod = order["payment"]!!,
                        status = order["status"]!!,
                        location = order["location"]!!
                    )
                    Spacer(Modifier.height(8.dp))
                }
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
    paymentMethod: String,
    status: String,
    location: String
) {
    val statusColor = when (status.lowercase()) {
        "completed" -> Color(0xFF4CAF50)
        "pending" -> Color(0xFFFFC107)
        "canceled" -> Color(0xFFF44336)
        else -> Color(0xFFFF6B00)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(invoice, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(customer, fontSize = 12.sp, color = Color.Gray, maxLines = 1)
                    Text(location, fontSize = 11.sp, color = Color.Gray, maxLines = 1)
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
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(date, fontSize = 11.sp, color = Color.Gray)
                    Spacer(Modifier.width(12.dp))
                    Icon(Icons.Default.Payment, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
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
    isOnline: Boolean,
    onToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFF6B00), Color(0xFFFF8C42))
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
                        text = "Joe's Burger Cafe",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    
                    Text(
                        text = "Puri Lestari - Blok H12/12",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(8.dp))

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
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(Modifier.weight(1f))
                NotificationBadge(count = 3)
            }

            Spacer(Modifier.height(8.dp))

            Switch(
                checked = isOnline,
                onCheckedChange = { onToggle() }
            )
        }
    }
}

@Composable
fun NotificationBadge(count: Int) {
    Box {
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(40.dp)
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
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun WeeklySummaryCard() {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(Modifier.padding(20.dp)) {

            Text("Weekly Summary", fontWeight = FontWeight.SemiBold)

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                SummaryItem("Orders", "86")
                SummaryItem("Revenue", "Rp 8.2M")
                SummaryItem("Rating", "4.8")
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
fun WeeklyOrdersChart() {
    val data = listOf(12f, 25f, 18f, 30f, 22f, 35f, 28f)
    val maxValue = max(data.max(), 1f)
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
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
                        color = Color(0xFFFF6B00),
                        radius = 6f,
                        center = Offset(x, y)
                    )
                }

                drawPath(
                    path = path,
                    color = Color(0xFFFF6B00),
                    style = Stroke(width = 4f)
                )

                data.forEachIndexed { index, _ ->
                    val x = space * index
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            days[index],
                            x,
                            height + 20.dp.toPx(),
                            android.graphics.Paint().apply {
                                textAlign = android.graphics.Paint.Align.CENTER
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

@Composable
fun OrderFilterChips(
    selected: String,
    onSelected: (String) -> Unit
) {
    val filters = listOf("All", "Ordered", "Cooking", "Completed", "Cancelled")

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(filters.size) { index ->
            val filter = filters[index]
            val isSelected = filter == selected

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (isSelected) Color(0xFFFF6B00)
                        else Color.LightGray.copy(alpha = 0.3f)
                    )
                    .clickable { onSelected(filter) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    filter,
                    color = if (isSelected) Color.White else Color.Black,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SellerDashboardPreview() {
    SellerDashboardScreen(
        uiState = SellerStateListener(),
        uiData = SellerDataListener(),
        uiEvent = SellerEventListener(),
        uiNavigation = SellerNavigationListener()
    )
}