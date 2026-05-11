/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerOrderDetailScreen.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 13.04
 */

package com.mtv.app.shopme.feature.seller.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailEvent
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailUiState
import com.mtv.app.shopme.feature.seller.contract.SellerOrderLineItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerOrderDetailScreen(
    state: SellerOrderDetailUiState,
    event: (SellerOrderDetailEvent) -> Unit
) {
    Scaffold(
        containerColor = AppColor.White,
        bottomBar = {
            UpdateStatusBottomBar(
                currentStatus = state.currentStatus,
                onUpdate = {
                    event(SellerOrderDetailEvent.ChangeStatus(it))
                    event(SellerOrderDetailEvent.SaveStatus)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SellerOrderDetailHeader(
                orderId = state.orderId,
                status = state.currentStatus,
                onBack = { event(SellerOrderDetailEvent.ClickBack) }
            )

            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(AppColor.WhiteSoft)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item { OrderTimeline(state.currentStatus) }
                item { OrderItemSection(state.items) }
                item { CustomerSection(state.customerName, state.customerAddress) }
                item { PaymentSection(state.total, state.paymentMethod) }
            }
        }
    }
}

@Composable
fun SellerOrderDetailHeader(
    orderId: String,
    status: OrderStatus,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(44.dp)
                .background(Color.White, CircleShape)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                text = "Order #$orderId",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                fontFamily = PoppinsFont
            )
            Spacer(Modifier.height(4.dp))
            StatusChip(status)
        }
    }
}

@Composable
fun StatusChip(status: OrderStatus) {
    val color by animateColorAsState(targetValue = status.statusColor())
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = status.name,
            color = color,
            fontWeight = FontWeight.SemiBold,
            fontFamily = PoppinsFont,
            fontSize = 12.sp
        )
    }
}

@Composable
fun OrderTimeline(status: OrderStatus) {
    val steps = sellerProgressSteps()
    val activeIndex = steps.indexOf(status)
    Column {
        Text("Order Progress", fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont)
        Spacer(Modifier.height(16.dp))

        if (status == OrderStatus.UNPAID || status == OrderStatus.CANCELLED) {
            StatusChip(status)
            Spacer(Modifier.height(12.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, step ->
                val isActive = activeIndex >= 0 && index <= activeIndex
                val color = if (isActive) step.statusColor() else Color.LightGray

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(color.copy(alpha = 0.2f))
                            .padding(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(step.name, fontSize = 10.sp, color = color, fontFamily = PoppinsFont)
                }

                if (index < steps.lastIndex) {
                    Box(
                        modifier = Modifier
                            .height(4.dp)
                            .weight(1f)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        color,
                                        if (activeIndex >= 0 && index + 1 <= activeIndex) {
                                            steps[index + 1].statusColor()
                                        } else {
                                            Color.LightGray
                                        }
                                    )
                                ),
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun OrderItemSection(items: List<SellerOrderLineItem>) {
    Column {
        Text("Order Items", fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont)
        Spacer(Modifier.height(12.dp))
        items.forEach { item ->
            OrderItemRow(
                title = item.title,
                qty = item.qty,
                price = item.price,
                notes = item.notes
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun OrderItemRow(
    title: String,
    qty: Int,
    price: String,
    notes: String,
    imageUrl: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        if (imageUrl != null) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray)
            )
            Spacer(Modifier.width(12.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium, fontFamily = PoppinsFont)
            Spacer(Modifier.height(6.dp))
            Text("Qty: $qty", fontSize = 12.sp, color = Color.Gray)
            if (notes.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                Text("Notes: $notes", fontSize = 12.sp, color = Color.Gray)
            }
        }

        Text(price, fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont)
    }
}

@Composable
fun CustomerSection(customerName: String, customerAddress: String) {
    Column {
        Text("Customer Info", fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont)
        Spacer(Modifier.height(12.dp))
        InfoCard("Name", customerName)
        Spacer(Modifier.height(8.dp))
        InfoCard("Address", customerAddress)
    }
}

@Composable
fun PaymentSection(total: String, paymentMethod: String) {
    Column {
        Text("Payment Info", fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont)
        Spacer(Modifier.height(12.dp))
        InfoCard("Method", paymentMethod)
        Spacer(Modifier.height(8.dp))
        InfoCard("Total Paid", total, highlight = true)
    }
}

@Composable
fun InfoCard(label: String, value: String, highlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontFamily = PoppinsFont)
        Text(
            value,
            fontFamily = PoppinsFont,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Medium,
            color = if (highlight) AppColor.Green else Color.Black
        )
    }
}

@Composable
fun UpdateStatusBottomBar(currentStatus: OrderStatus, onUpdate: (OrderStatus) -> Unit) {
    val nextStatus = nextSellerStatus(currentStatus) ?: return
    val color = nextStatus.statusColor()

    Button(
        onClick = { onUpdate(nextStatus) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(56.dp)
            .navigationBarsPadding(),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(28.dp)
    ) {
        Text(
            "Mark as ${nextStatus.name}",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontFamily = PoppinsFont
        )
    }
}

private fun sellerProgressSteps(): List<OrderStatus> = listOf(
    OrderStatus.ORDERED,
    OrderStatus.COOKING,
    OrderStatus.DELIVERING,
    OrderStatus.COMPLETED
)

private fun nextSellerStatus(currentStatus: OrderStatus): OrderStatus? = when (currentStatus) {
    OrderStatus.ORDERED -> OrderStatus.COOKING
    OrderStatus.COOKING -> OrderStatus.DELIVERING
    OrderStatus.DELIVERING -> OrderStatus.COMPLETED
    else -> null
}

fun OrderStatus.statusColor(): Color = when (this) {
    OrderStatus.UNPAID -> Color(0xFFDC2626)
    OrderStatus.ORDERED -> Color(0xFFFFA000)
    OrderStatus.COOKING -> Color(0xFF1E88E5)
    OrderStatus.DELIVERING -> Color(0xFF00897B)
    OrderStatus.COMPLETED -> Color(0xFF2E7D32)
    OrderStatus.CANCELLED -> Color(0xFF6B7280)
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SellerOrderDetailPreview() {
    SellerOrderDetailScreen(
        state = SellerOrderDetailUiState(
            orderId = "INV-001",
            currentStatus = OrderStatus.COOKING,
            customerName = "Dedy Wijaya",
            total = "Rp 120.000",
            paymentMethod = "TRANSFER",
            items = listOf(
                SellerOrderLineItem("Iced Latte", 2, "Rp 60.000", "Less sugar")
            )
        ),
        event = {}
    )
}
