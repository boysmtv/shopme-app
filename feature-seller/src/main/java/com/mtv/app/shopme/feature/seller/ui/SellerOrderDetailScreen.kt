package com.mtv.app.shopme.feature.seller.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.mtv.app.shopme.data.OrderStatus
import com.mtv.app.shopme.feature.seller.contract.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerOrderDetailScreen(
    uiState: SellerOrderDetailStateListener,
    uiData: SellerOrderDetailDataListener,
    uiEvent: SellerOrderDetailEventListener,
    uiNavigation: SellerOrderDetailNavigationListener
) {

    Scaffold(
        topBar = {
            SellerOrderDetailHeader(
                orderId = uiState.orderId,
                status = uiState.currentStatus,
                onBack = uiNavigation.onBack
            )
        },
        bottomBar = {
            UpdateStatusBottomBar(
                currentStatus = uiState.currentStatus,
                onUpdate = { uiEvent.onChangeStatus(it) }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColor.WhiteSoft)
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {
                OrderTimeline(uiState.currentStatus)
            }

            item {
                OrderItemSection()
            }

            item {
                CustomerSection(customerName = uiData.customerName)
            }

            item {
                PaymentSection(total = uiData.total)
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

// ------------------- HEADER -------------------
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

// ------------------- STATUS CHIP -------------------
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

// ------------------- TIMELINE -------------------
@Composable
fun OrderTimeline(status: OrderStatus) {
    val steps = OrderStatus.entries

    Column {
        Text("Order Progress", fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont)
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, step ->
                val isActive = step.ordinal <= status.ordinal
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
                                        if (steps[index + 1].ordinal <= status.ordinal) steps[index + 1].statusColor() else Color.LightGray
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

// ------------------- ORDER ITEM -------------------
@Composable
fun OrderItemSection() {
    Column {
        Text("Order Items", fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont)
        Spacer(Modifier.height(12.dp))

        repeat(2) {
            OrderItemRow(
                title = "Double Beef Burger",
                qty = 2,
                price = "Rp 60.000"
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
    imageUrl: String? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable { expanded = !expanded }
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
            AnimatedVisibility(expanded) {
                Spacer(Modifier.height(6.dp))
                Text("Notes: Extra spicy, no onion", fontSize = 12.sp, color = Color.Gray)
            }
        }

        Text(price, fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont)
    }
}

// ------------------- CUSTOMER & PAYMENT -------------------
@Composable
fun CustomerSection(customerName: String) {
    Column {
        Text("Customer Info", fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont)
        Spacer(Modifier.height(12.dp))
        InfoCard("Name", customerName)
        Spacer(Modifier.height(8.dp))
        InfoCard("Address", "Puri Lestari Blok H12/01")
    }
}

@Composable
fun PaymentSection(total: String) {
    Column {
        Text("Payment Info", fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont)
        Spacer(Modifier.height(12.dp))
        InfoCard("Method", "Bank Transfer")
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
            color = if (highlight) AppColor.Orange else Color.Black
        )
    }
}

// ------------------- BOTTOM BAR -------------------
@Composable
fun UpdateStatusBottomBar(currentStatus: OrderStatus, onUpdate: (OrderStatus) -> Unit) {
    val nextStatus = OrderStatus.entries.getOrNull(currentStatus.ordinal + 1) ?: return
    val color = nextStatus.statusColor()

    Button(
        onClick = { onUpdate(nextStatus) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(56.dp),
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

// ------------------- ORDER STATUS COLOR -------------------
fun OrderStatus.statusColor(): Color {
    return when (this) {
        OrderStatus.ORDERED -> Color(0xFFFF9800)
        OrderStatus.COOKING -> Color(0xFF2196F3)
        OrderStatus.DELIVERING -> Color(0xFF4CAF50)
        OrderStatus.COMPLETED -> Color(0xFFF44336)
    }
}

// ------------------- PREVIEW -------------------
@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SellerOrderDetailPreview() {
    SellerOrderDetailScreen(
        uiState = SellerOrderDetailStateListener(
            orderId = "INV-001",
            currentStatus = OrderStatus.COOKING
        ),
        uiData = SellerOrderDetailDataListener(
            customerName = "Dedy Wijaya",
            total = "Rp 120.000"
        ),
        uiEvent = SellerOrderDetailEventListener(),
        uiNavigation = SellerOrderDetailNavigationListener()
    )
}
