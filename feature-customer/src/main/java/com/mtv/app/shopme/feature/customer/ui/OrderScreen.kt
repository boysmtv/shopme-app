/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderScreen.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 09.14
 */

package com.mtv.app.shopme.feature.customer.ui

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.domain.model.Order
import com.mtv.app.shopme.domain.model.OrderItem
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.domain.model.PaymentMethod
import com.mtv.app.shopme.domain.model.PaymentStatus
import com.mtv.app.shopme.feature.customer.contract.OrderEvent
import com.mtv.app.shopme.feature.customer.contract.OrderUiState

enum class OrderFilter {
    SEMUA,
    ORDERED,
    COOKING,
    DELIVERING,
    COMPLETED
}

@Composable
fun OrderScreen(
    state: OrderUiState,
    event: (OrderEvent) -> Unit
) {
    var selectedFilter by remember { mutableStateOf(OrderFilter.SEMUA) }

    val filteredOrders = state.orders.filter {
        when (selectedFilter) {
            OrderFilter.SEMUA -> true
            OrderFilter.ORDERED -> it.status == OrderStatus.UNPAID || it.status == OrderStatus.ORDERED
            OrderFilter.COOKING -> it.status == OrderStatus.COOKING
            OrderFilter.DELIVERING -> it.status == OrderStatus.DELIVERING
            OrderFilter.COMPLETED -> it.status == OrderStatus.COMPLETED
        }
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

        ModernOrderTopBar(
            onBack = { event(OrderEvent.ClickBack) },
            onChat = { event(OrderEvent.ClickChat) }
        )
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            colors = CardDefaults.cardColors(containerColor = AppColor.WhiteSoft)
        ) {

            Column {

                ModernOrderFilter(
                    selected = selectedFilter,
                    onChange = { selectedFilter = it }
                )

                if (filteredOrders.isEmpty()) {
                    ModernEmptyOrder()
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        items(filteredOrders) { order ->
                            ModernOrderCard(
                                order = order,
                                onClick = {
                                    event(OrderEvent.ClickOrder(order.id))
                                },
                                onConfirmTransferClick = { orderId ->
                                    event(OrderEvent.ConfirmTransfer(orderId))
                                },
                                onChatClick = {
                                    event(OrderEvent.ClickChat)
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
fun ModernOrderFilter(
    selected: OrderFilter,
    onChange: (OrderFilter) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(50))
            .background(AppColor.GreenSoft),
    ) {

        OrderFilter.entries.forEach { filter ->
            val selectedState = selected == filter
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50))
                    .background(if (selectedState) AppColor.Green else Color.Transparent)
                    .clickable { onChange(filter) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    filter.name.lowercase().replaceFirstChar { it.uppercase() },
                    fontFamily = PoppinsFont,
                    color = if (selectedState) Color.White else AppColor.Green,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ModernOrderTopBar(
    onBack: () -> Unit,
    onChat: () -> Unit
) {
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
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.clickable { onBack() }
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(Modifier.weight(1f)) {
            Text(
                "Pesanan Aktif",
                color = Color.White,
                fontFamily = PoppinsFont,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Pantau status pesanan kamu",
                color = Color.White.copy(.8f),
                fontSize = 12.sp
            )
        }

        Icon(
            Icons.AutoMirrored.Filled.Chat,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.clickable { onChat() }
        )
    }
}

@Composable
fun ModernOrderCard(
    order: Order,
    onClick: () -> Unit,
    onConfirmTransferClick: (String) -> Unit,
    onChatClick: () -> Unit
) {

    val statusColor = when (order.status) {
        OrderStatus.UNPAID -> Color(0xFFDC2626)
        OrderStatus.ORDERED -> Color(0xFFF59E0B)
        OrderStatus.COOKING -> AppColor.Green
        OrderStatus.DELIVERING -> Color(0xFF2563EB)
        OrderStatus.COMPLETED -> Color(0xFF16A34A)
        OrderStatus.CANCELLED -> Color(0xFF6B7280)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = AppColor.White)
    ) {

        Column(Modifier.padding(16.dp)) {

            Text(
                "Cafe ID: ${order.cafeId}",
                fontFamily = PoppinsFont,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = AppColor.Green
            )

            Spacer(Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    Icons.AutoMirrored.Filled.ReceiptLong,
                    null,
                    tint = AppColor.Green,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(Modifier.width(10.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        "Order • ${order.id}",
                        fontFamily = PoppinsFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                    Text(
                        "${order.items.sumOf { it.quantity }} item",
                        fontSize = 11.sp,
                        color = AppColor.Gray
                    )
                }

                ModernStatusBadge(order.status.value, statusColor)
            }

            Spacer(Modifier.height(10.dp))
            HorizontalDivider(color = AppColor.Green.copy(.15f))
            Spacer(Modifier.height(10.dp))

            order.items.take(2).forEach {
                Text(
                    "• Produk ID ${it.foodId} x${it.quantity}",
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.height(12.dp))

            HorizontalDivider(color = AppColor.Green.copy(.15f))
            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Total Pembayaran",
                    fontSize = 12.sp,
                    color = AppColor.Gray
                )
                Spacer(Modifier.weight(1f))
                Text(
                    "Rp ${order.totalPrice.toInt()}",
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = AppColor.Green
                )
            }

            Spacer(Modifier.height(14.dp))

            val needsTransferConfirmation = order.paymentMethod == PaymentMethod.TRANSFER &&
                    order.paymentStatus in setOf(PaymentStatus.WAITING_UPLOAD, PaymentStatus.FAILED)
            val isCompleted = order.status == OrderStatus.COMPLETED

            Row(
                modifier = Modifier.align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (isCompleted) {

                    Button(
                        onClick = onClick,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColor.Green
                        )
                    ) {
                        Text("Beli Lagi", color = Color.White)
                    }

                } else {
                    if (needsTransferConfirmation) {

                        Button(
                            onClick = { onConfirmTransferClick(order.id) },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF59E0B)
                            )
                        ) {
                            Text("Konfirmasi Transfer", color = Color.White)
                        }

                        Spacer(Modifier.width(10.dp))
                    }

                    OutlinedButton(
                        onClick = onChatClick,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = AppColor.Green
                        )
                    ) {
                        Text("Chat")
                    }
                }
            }
        }
    }
}

@Composable
fun ModernStatusBadge(text: String, color: Color) {
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
fun ModernEmptyOrder() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.AutoMirrored.Filled.ReceiptLong,
                null,
                tint = AppColor.Gray,
                modifier = Modifier.size(70.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text("Belum ada pesanan aktif", fontFamily = PoppinsFont)
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun OrderScreenPreview() {
    val dummyOrders = listOf(
        Order(
            id = "A001",
            customerId = "C001",
            cafeId = "Mamah Al Cafe",
            items = listOf(
                OrderItem(foodId = "0", quantity = 2, price = 15000.0),
                OrderItem(foodId = "3", quantity = 1, price = 15000.0)
            ),
            totalPrice = 45000.0,
            status = OrderStatus.ORDERED,
            paymentMethod = PaymentMethod.TRANSFER
        ),
        Order(
            id = "A002",
            customerId = "C001",
            cafeId = "Mamah Al Cafe",
            items = listOf(
                OrderItem(foodId = "1", quantity = 1, price = 30000.0)
            ),
            totalPrice = 30000.0,
            status = OrderStatus.DELIVERING,
            paymentMethod = PaymentMethod.TRANSFER
        ),
        Order(
            id = "A003",
            customerId = "C001",
            cafeId = "Mamah Al Cafe",
            items = listOf(
                OrderItem(foodId = "2", quantity = 1, price = 20000.0),
                OrderItem(foodId = "5", quantity = 2, price = 16000.0)
            ),
            totalPrice = 52000.0,
            status = OrderStatus.COMPLETED,
            paymentMethod = PaymentMethod.TRANSFER
        )
    )

    OrderScreen(
        state = OrderUiState(
            isLoading = false,
            orders = dummyOrders
        ),
        event = {}
    )
}
