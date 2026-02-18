/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerOrderDetailScreen.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 13.07
 */

package com.mtv.app.shopme.feature.seller.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailStateListener

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
            TopAppBar(
                title = { Text("Order Detail") },
                navigationIcon = {
                    IconButton(onClick = uiNavigation.onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        SellerOrderDetailContent(
            modifier = Modifier.padding(padding),
            uiState = uiState,
            uiData = uiData,
            uiEvent = uiEvent,
            uiNavigation = uiNavigation
        )
    }
}


@Composable
private fun SellerOrderDetailContent(
    modifier: Modifier,
    uiState: SellerOrderDetailStateListener,
    uiData: SellerOrderDetailDataListener,
    uiEvent: SellerOrderDetailEventListener,
    uiNavigation: SellerOrderDetailNavigationListener,
) {

    Scaffold(
        topBar = {
            SellerOrderDetailHeader(
                onBack = uiNavigation.onBack
            )
        },
        bottomBar = {
            UpdateStatusBottomBar(
                currentStatus = uiState.currentStatus,
                onUpdate = { uiEvent.onChangeStatus(it) }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            AppColor.LightOrange,
                            AppColor.WhiteSoft,
                            AppColor.White
                        )
                    )
                )
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {
                OrderHeaderCard(
                    orderId = uiState.orderId,
                    total = uiData.total,
                    status = uiState.currentStatus
                )
            }

            item {
                OrderTimeline(status = uiState.currentStatus)
            }

            item {
                OrderItemSection()
            }

            item {
                CustomerSection(
                    customerName = uiData.customerName
                )
            }

            item {
                PaymentSection(total = uiData.total)
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun OrderHeaderCard(
    orderId: String,
    total: String,
    status: OrderStatus
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {

        Text(
            text = "Order #$orderId",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = PoppinsFont
        )

        Spacer(Modifier.height(8.dp))

        StatusChip(status)

        Spacer(Modifier.height(16.dp))

        Text(
            text = total,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AppColor.Orange,
            fontFamily = PoppinsFont
        )
    }
}

@Composable
fun OrderTimeline(status: OrderStatus) {

    val steps = OrderStatus.entries

    Column {
        Text(
            text = "Order Progress",
            fontWeight = FontWeight.SemiBold,
            fontFamily = PoppinsFont
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            steps.forEachIndexed { index, step ->

                val isActive = step.ordinal <= status.ordinal
                val color = if (isActive) step.statusColor() else Color.LightGray

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(color)
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = step.name,
                        fontSize = 11.sp,
                        fontFamily = PoppinsFont,
                        color = color
                    )
                }

                if (index < steps.lastIndex) {
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .weight(1f)
                            .background(
                                if (steps[index + 1].ordinal <= status.ordinal)
                                    steps[index + 1].statusColor()
                                else Color.LightGray
                            )
                    )
                }
            }
        }
    }
}


@Composable
fun OrderItemSection() {

    Column {
        Text(
            text = "Order Items",
            fontWeight = FontWeight.SemiBold,
            fontFamily = PoppinsFont
        )

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
    price: String
) {

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable { expanded = !expanded }
            .padding(16.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

            Column {
                Text(
                    title,
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    "Qty: $qty",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Text(
                price,
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.SemiBold
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Divider()
                Spacer(Modifier.height(8.dp))
                Text(
                    "Notes: Extra spicy, no onion",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}


@Composable
fun CustomerSection(
    customerName: String
) {
    Column {
        Text(
            text = "Customer Info",
            fontWeight = FontWeight.SemiBold,
            fontFamily = PoppinsFont
        )

        Spacer(Modifier.height(12.dp))

        InfoCard(
            label = "Name",
            value = customerName
        )

        Spacer(Modifier.height(8.dp))

        InfoCard(
            label = "Address",
            value = "Puri Lestari Blok H12/01"
        )
    }
}

@Composable
fun PaymentSection(
    total: String
) {
    Column {
        Text(
            text = "Payment Info",
            fontWeight = FontWeight.SemiBold,
            fontFamily = PoppinsFont
        )

        Spacer(Modifier.height(12.dp))

        InfoCard("Method", "Bank Transfer")
        Spacer(Modifier.height(8.dp))
        InfoCard("Total Paid", total, highlight = true)
    }
}

@Composable
fun InfoCard(
    label: String,
    value: String,
    highlight: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontFamily = PoppinsFont, color = Color.Gray)

        Text(
            value,
            fontFamily = PoppinsFont,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Medium,
            color = if (highlight) AppColor.Orange else Color.Black
        )
    }
}

@Composable
fun UpdateStatusBottomBar(
    currentStatus: OrderStatus,
    onUpdate: (OrderStatus) -> Unit
) {

    val nextStatus = OrderStatus.entries
        .getOrNull(currentStatus.ordinal + 1)

    if (nextStatus != null) {

        val color = nextStatus.statusColor()

        Button(
            onClick = { onUpdate(nextStatus) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(color),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = "Mark as ${nextStatus.name}",
                color = Color.White,
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


@Composable
fun StatusChip(status: OrderStatus) {

    val color by animateColorAsState(
        targetValue = status.statusColor(),
        label = ""
    )

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 14.dp, vertical = 6.dp),
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
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Composable
fun SellerOrderDetailHeader(
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable { onBack() }
                .padding(10.dp)
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = "Order Detail",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = PoppinsFont
        )
    }
}

@Composable
fun OrderInfoCard(
    orderId: String,
    customerName: String,
    total: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {

        InfoRow("Order ID", orderId)
        Spacer(Modifier.height(12.dp))

        InfoRow("Customer", customerName)
        Spacer(Modifier.height(12.dp))

        InfoRow("Total", total, highlight = true)
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String,
    highlight: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontFamily = PoppinsFont,
            color = Color.Gray
        )

        Text(
            text = value,
            fontFamily = PoppinsFont,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Medium,
            color = if (highlight) AppColor.Orange else Color.Black
        )
    }
}

fun OrderStatus.statusColor(): Color {
    return when (this) {
        OrderStatus.ORDERED -> Color(0xFFFF9800)
        OrderStatus.COOKING -> Color(0xFF2196F3)
        OrderStatus.DELIVERING -> Color(0xFF4CAF50)
        OrderStatus.COMPLETED -> Color(0xFFF44336)
    }
}

@Composable
fun CancelOrderDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Text("Yes, Cancel")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("No")
                }
            },
            title = { Text("Cancel Order?") },
            text = { Text("This action cannot be undone.") }
        )
    }
}


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
