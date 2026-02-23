/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderHistoryScreen.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 09.59
 */

package com.mtv.app.shopme.feature.customer.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
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
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryDataListener
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryEventListener
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryItem
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryNavigationListener
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryStateListener
import com.mtv.app.shopme.feature.customer.contract.OrderStatusFilter

@Composable
fun OrderHistoryScreen(
    uiState: OrderHistoryStateListener,
    uiData: OrderHistoryDataListener,
    uiEvent: OrderHistoryEventListener,
    uiNavigation: OrderHistoryNavigationListener
) {

    val filteredOrders = uiData.orders.filter {
        uiData.selectedFilter == OrderStatusFilter.SEMUA ||
                it.status == uiData.selectedFilter.name
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(AppColor.Green, AppColor.GreenSoft)
                )
            )
            .statusBarsPadding()
    ) {

        ModernTopBar(uiNavigation)

        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            colors = CardDefaults.cardColors(containerColor = AppColor.WhiteSoft)
        ) {

            Column(Modifier.fillMaxSize()) {
                ModernFilter(uiData.selectedFilter, uiEvent)
                if (uiState.loading) {
                    ModernSkeleton()
                } else if (filteredOrders.isEmpty()) {
                    ModernEmpty()
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        items(filteredOrders) { order ->
                            ModernOrderCard(order) {
                                uiNavigation.onDetail(order)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ModernTopBar(nav: OrderHistoryNavigationListener) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { nav.onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
        }

        Spacer(Modifier.width(14.dp))

        Column {
            Text(
                "Riwayat Belanja",
                color = Color.White,
                fontFamily = PoppinsFont,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Lihat semua pesanan kamu",
                color = Color.White.copy(.8f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun ModernFilter(
    selected: OrderStatusFilter,
    event: OrderHistoryEventListener
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(50))
            .background(AppColor.GreenSoft),
    ) {

        OrderStatusFilter.entries.forEach { filter ->

            val selectedState = selected == filter

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (selectedState) AppColor.Green else Color.Transparent
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        event.onFilterChange(filter)
                    }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = filter.name.lowercase().replaceFirstChar { it.uppercase() },
                    fontFamily = PoppinsFont,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (selectedState) Color.White else AppColor.Green
                )
            }
        }
    }
}

@Composable
fun ModernOrderCard(
    item: OrderHistoryItem,
    onClick: () -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    val statusColor = when (item.status) {
        "SELESAI" -> Color(0xFF16A34A)
        "DIPROSES" -> Color(0xFFF59E0B)
        "DIKIRIM" -> Color(0xFF2563EB)
        else -> Color(0xFFDC2626)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = AppColor.White)
    ) {

        Column(Modifier.padding(16.dp)) {

            // ===== STORE NAME =====
            Text(
                item.storeName,
                fontFamily = PoppinsFont,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = AppColor.Green
            )

            Spacer(Modifier.height(6.dp))

            // ===== HEADER =====
            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    Icons.AutoMirrored.Filled.ReceiptLong,
                    contentDescription = null,
                    tint = AppColor.Green,
                    modifier = Modifier.size(26.dp)
                )

                Spacer(Modifier.width(10.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        item.title,
                        fontFamily = PoppinsFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                    Text(
                        "Order ID • ${item.id}",
                        fontSize = 11.sp,
                        color = AppColor.Gray
                    )
                }

                StatusBadge(item.status, statusColor)
            }

            Spacer(Modifier.height(10.dp))
            HorizontalDivider(color = AppColor.Green.copy(.15f))
            Spacer(Modifier.height(10.dp))

            // ===== MINI INFO =====
            InfoRow("Tanggal", item.date)
            InfoRow("Items", "${item.totalItems} item")
            InfoRow("Pembayaran", item.paymentMethod)
            InfoRow("Delivery", item.deliveryType)

            Spacer(Modifier.height(12.dp))

            // ===== PROGRESS MINI TIMELINE =====
            MiniTimeline(item.status)

            Spacer(Modifier.height(12.dp))

            HorizontalDivider(color = AppColor.Green.copy(.15f))
            Spacer(Modifier.height(12.dp))

            // ===== TOTAL =====
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Total Pembayaran",
                    fontSize = 12.sp,
                    color = AppColor.Gray
                )
                Spacer(Modifier.weight(1f))
                Text(
                    item.price,
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = AppColor.Green
                )
            }

            // ===== EXPANDABLE DETAIL =====
            AnimatedVisibility(expanded) {
                Column {
                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(16.dp))

                    Text(
                        "Detail Pesanan",
                        fontFamily = PoppinsFont,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.height(8.dp))

                    Text("• Cappuccino x2")
                    Text("• Croissant x1")

                    Spacer(Modifier.height(16.dp))

                    Row {

                        if (item.status == "SELESAI") {
                            OutlinedButton(
                                onClick = { /* Rating */ },
                                shape = RoundedCornerShape(50)
                            ) {
                                Text("Beri Rating")
                            }
                            Spacer(Modifier.width(10.dp))
                        }

                        Button(
                            onClick = { /* Reorder */ },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppColor.Green
                            )
                        ) {
                            Text("Pesan Lagi", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MiniTimeline(status: String) {

    val steps = listOf("DIPROSES", "DIKIRIM", "SELESAI")

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {

        steps.forEach { step ->

            val active = steps.indexOf(step) <= steps.indexOf(status)

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            if (active) AppColor.Green else AppColor.Gray.copy(.3f),
                            RoundedCornerShape(50)
                        )
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    step.lowercase().replaceFirstChar { it.uppercase() },
                    fontSize = 10.sp,
                    color = if (active) AppColor.Green else AppColor.Gray
                )
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Text(
            label,
            fontSize = 12.sp,
            color = AppColor.Gray
        )
        Spacer(Modifier.weight(1f))
        Text(
            value,
            fontFamily = PoppinsFont,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun StatusBadge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(.15f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ModernEmpty() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                contentDescription = null,
                tint = AppColor.Gray,
                modifier = Modifier.size(70.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text("Belum ada riwayat", fontFamily = PoppinsFont)
        }
    }
}

@Composable
fun ModernSkeleton() {
    LazyColumn(Modifier.padding(16.dp)) {
        items(4) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .padding(bottom = 14.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AppColor.WhiteSoft)
            ) {}
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun OrderHistoryPreview() {
    OrderHistoryScreen(
        uiState = OrderHistoryStateListener(loading = false),
        uiData = OrderHistoryDataListener(
            selectedFilter = OrderStatusFilter.SEMUA,
            orders = listOf(
                OrderHistoryItem(
                    "1",
                    "Cafe Kopi Boy",
                    "Cappuccino",
                    "12 Feb 2026",
                    "Rp 25.000",
                    "SELESAI",
                    3,
                    "E-Wallet",
                    "Diantar"
                ),
                OrderHistoryItem(
                    "2",
                    "Kopi Nusantara",
                    "Caramel Latte",
                    "11 Feb 2026",
                    "Rp 32.000",
                    "DIPROSES",
                    2,
                    "QRIS",
                    "Pickup"
                ),
                OrderHistoryItem(
                    "3",
                    "Burger Town",
                    "Cheese Burger Combo",
                    "10 Feb 2026",
                    "Rp 48.000",
                    "DIKIRIM",
                    4,
                    "Cash",
                    "Diantar"
                ),
                OrderHistoryItem(
                    "4",
                    "Coffee Corner",
                    "Americano",
                    "08 Feb 2026",
                    "Rp 22.000",
                    "SELESAI",
                    1,
                    "E-Wallet",
                    "Pickup"
                ),
                OrderHistoryItem(
                    "5",
                    "Cafe Kopi Boy",
                    "Matcha Latte",
                    "05 Feb 2026",
                    "Rp 30.000",
                    "BATAL",
                    2,
                    "QRIS",
                    "Diantar"
                )
            )
        ),
        uiEvent = OrderHistoryEventListener(),
        uiNavigation = OrderHistoryNavigationListener()
    )
}