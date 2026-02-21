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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.navigation.compose.rememberNavController
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerOrderEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerOrderNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerOrderStateListener
import com.mtv.app.shopme.nav.SellerBottomNavigationBar

@Composable
fun SellerOrderScreen(
    uiState: SellerOrderStateListener,
    uiData: SellerOrderDataListener,
    uiEvent: SellerOrderEventListener,
    uiNavigation: SellerOrderNavigationListener
) {
    var selectedFilter by remember { mutableStateOf("All") }
    var isOnline by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Blue)
            .statusBarsPadding()
    ) {
        SellerOrderHeader(isOnline, orders)

        Column(
            modifier = Modifier
                .background(AppColor.WhiteSoft)
        ) {
            Spacer(Modifier.height(16.dp))
            OrderFilterChips(selectedFilter) { selectedFilter = it }

            Spacer(Modifier.height(12.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColor.WhiteSoft)
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(orders.filter { selectedFilter == "All" || it["status"] == selectedFilter }) { order ->
                    ModernOrderCard(
                        invoice = order["invoice"]!!,
                        customer = order["customer"]!!,
                        location = order["location"]!!,
                        total = order["total"]!!,
                        date = order["date"]!!,
                        time = order["time"]!!,
                        paymentMethod = order["payment"]!!,
                        status = order["status"]!!,
                        onClick = { uiNavigation.onNavigateToOrderDetail() }
                    )
                }
            }
        }
    }
}

@Composable
fun SellerOrderHeader(
    isOnline: Boolean, orders: List<Map<String, String>>
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
                Text("Orders", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(if (isOnline) Color.Green else Color.Red)
                )
                Spacer(Modifier.width(6.dp))
                Text(if (isOnline) "Online" else "Offline", color = Color.White, fontSize = 14.sp)
            }

            Spacer(Modifier.height(8.dp))
            Text("${orders.size} total orders", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
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
        "pending" -> Color(0xFFFFB74D)
        "cooking" -> Color(0xFFFF8A65)
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
    val filters = listOf("All", "Pending", "Cooking", "Completed", "Cancelled")
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
                    .background(if (filter == selected) AppColor.Blue else Color.LightGray.copy(alpha = 0.3f))
                    .clickable { onSelected(filter) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    filter,
                    color = if (filter == selected) AppColor.White else Color.Black
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
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { SellerBottomNavigationBar(navController) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
        ) {
            SellerOrderScreen(
                uiState = SellerOrderStateListener(),
                uiData = SellerOrderDataListener(),
                uiEvent = SellerOrderEventListener({}, {}),
                uiNavigation = SellerOrderNavigationListener({})
            )
        }
    }
}