/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerOrderScreen.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 14.10
 */

package com.mtv.app.shopme.feature.seller.ui

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.ShimmerBlock
import com.mtv.app.shopme.common.ShimmerLine
import com.mtv.app.shopme.feature.seller.contract.OrderSummary
import com.mtv.app.shopme.feature.seller.contract.SellerOrderEvent
import com.mtv.app.shopme.feature.seller.contract.SellerOrderUiState

@Composable
fun SellerOrderScreen(
    state: SellerOrderUiState,
    event: (SellerOrderEvent) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Blue)
            .statusBarsPadding()
    ) {

        SellerOrderHeader(
            isOnline = state.isOnline,
            totalOrders = state.orders.size,
            onToggle = {
                event(SellerOrderEvent.ToggleOnline)
            }
        )

        Column(
            modifier = Modifier.background(AppColor.WhiteSoft)
        ) {

            Spacer(Modifier.height(16.dp))

            OrderFilterChips(
                selected = state.selectedFilter,
                onSelected = {
                    event(SellerOrderEvent.SelectFilter(it))
                }
            )

            Spacer(Modifier.height(12.dp))

            if (state.isLoading) {
                SellerOrderShimmer()
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColor.WhiteSoft)
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                val filtered = state.orders.filter {
                    state.selectedFilter == "All" || it.status.equals(state.selectedFilter, true)
                }

                items(filtered) { order ->
                    ModernOrderCard(
                        invoice = order.orderId,
                        customer = order.customerName,
                        location = order.location,
                        total = order.total,
                        date = order.date,
                        time = order.time,
                        paymentMethod = order.paymentMethod,
                        status = order.status,
                        onClick = {
                            event(SellerOrderEvent.ClickOrder(order.orderId))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SellerOrderShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(3) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
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
                            ShimmerLine(widthFraction = 0.32f, heightDp = 14)
                            Spacer(Modifier.height(8.dp))
                            ShimmerLine(widthFraction = 0.52f, heightDp = 12)
                        }
                        ShimmerBlock(
                            modifier = Modifier
                                .width(74.dp)
                                .height(22.dp),
                            shape = RoundedCornerShape(50)
                        )
                    }
                    ShimmerLine(widthFraction = 0.68f, heightDp = 12)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ShimmerBlock(
                            modifier = Modifier
                                .width(88.dp)
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
    }
}

@Composable
fun SellerOrderHeader(
    isOnline: Boolean,
    totalOrders: Int,
    onToggle: () -> Unit
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

            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    imageVector = Icons.Default.Receipt,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    "Orders",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(if (isOnline) Color.Green else Color.Red)
                        .clickable { onToggle() }
                )

                Spacer(Modifier.width(6.dp))

                Text(
                    if (isOnline) "Online" else "Offline",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                "$totalOrders total orders",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ModernOrderCard(
    invoice: String,
    customer: String,
    location: String,
    total: String,
    date: String,
    time: String,
    paymentMethod: String,
    status: String,
    onClick: () -> Unit
) {
    val statusColor = when (status.lowercase()) {
        "ordered", "pending", "unpaid" -> Color(0xFFFFB74D)
        "cooking" -> Color(0xFFFF8A65)
        "delivering" -> Color(0xFF29B6F6)
        "completed" -> Color(0xFF81C784)
        "cancelled" -> Color(0xFFE57373)
        else -> Color.LightGray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(20.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(customer, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Text(total, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = AppColor.Blue)
                }

                Spacer(Modifier.height(12.dp))
                HorizontalDivider(
                    color = AppColor.BlueMedium,
                    thickness = 1.dp,
                )

                Spacer(Modifier.height(12.dp))
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(location, fontSize = 13.sp, color = Color.Gray)
                    }

                    Spacer(Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DocumentScanner,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(invoice, fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
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
                        .background(statusColor.copy(alpha = 0.25f))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(status, color = statusColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}


@Composable
private fun OrderFilterChips(selected: String, onSelected: (String) -> Unit) {
    val filters = listOf("All", "ORDERED", "COOKING", "DELIVERING", "COMPLETED", "CANCELLED")
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(filters.size) { index ->
            val filter = filters[index]
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (filter.equals(selected, ignoreCase = true)) AppColor.Blue else Color.LightGray.copy(alpha = 0.3f))
                    .clickable { onSelected(filter) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    when (filter) {
                        "ORDERED" -> "Ordered"
                        "COOKING" -> "Cooking"
                        "DELIVERING" -> "Delivering"
                        "COMPLETED" -> "Completed"
                        "CANCELLED" -> "Cancelled"
                        else -> filter
                    },
                    color = if (filter.equals(selected, ignoreCase = true)) AppColor.White else Color.Black
                )
            }
        }
    }
}

val orders = listOf(
    mapOf(
        "invoice" to "INV-2026-001",
        "customer" to "John Doe",
        "total" to "Rp 120.000",
        "date" to "18 Feb 2026",
        "time" to "09:45",
        "payment" to "Transfer Bank",
        "status" to "Pending",
        "location" to "Puri Lestari - Blok H12/12"
    ),
    mapOf(
        "invoice" to "INV-2026-002",
        "customer" to "Jane Smith",
        "total" to "Rp 250.000",
        "date" to "17 Feb 2026",
        "time" to "14:30",
        "payment" to "E-Wallet",
        "status" to "Completed",
        "location" to "Puri Lestari - Blok H11/10"
    ),
    mapOf(
        "invoice" to "INV-2026-003",
        "customer" to "Michael Johnson",
        "total" to "Rp 180.000",
        "date" to "16 Feb 2026",
        "time" to "11:15",
        "payment" to "Cash",
        "status" to "Cooking",
        "location" to "Puri Indah - Blok A5/3"
    ),
    mapOf(
        "invoice" to "INV-2026-004",
        "customer" to "Emily Davis",
        "total" to "Rp 300.000",
        "date" to "15 Feb 2026",
        "time" to "16:50",
        "payment" to "Transfer Bank",
        "status" to "Cancelled",
        "location" to "Puri Lestari - Blok H13/5"
    )
)

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SellerOrderScreenPreview() {

    val dummy = listOf(
        OrderSummary(
            orderId = "INV-001",
            customerName = "John Doe",
            total = "Rp 120.000",
            date = "18 Feb 2026",
            time = "09:45",
            paymentMethod = "Transfer",
            status = "Pending",
            location = "Jakarta"
        ),
        OrderSummary(
            orderId = "INV-002",
            customerName = "Jane Smith",
            total = "Rp 250.000",
            date = "17 Feb 2026",
            time = "14:30",
            paymentMethod = "QRIS",
            status = "Completed",
            location = "Bandung"
        )
    )

    SellerOrderScreen(
        state = SellerOrderUiState(
            orders = dummy,
            selectedFilter = "All",
            isOnline = true
        ),
        event = {}
    )
}
