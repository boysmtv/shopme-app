/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderScreen.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 09.14
 */

package com.mtv.app.shopme.feature.customer.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.data.OrderItemModel
import com.mtv.app.shopme.data.OrderModel
import com.mtv.app.shopme.data.OrderStatus
import com.mtv.app.shopme.feature.customer.contract.OrderDataListener
import com.mtv.app.shopme.feature.customer.contract.OrderEventListener
import com.mtv.app.shopme.feature.customer.contract.OrderNavigationListener
import com.mtv.app.shopme.feature.customer.contract.OrderStateListener

@Composable
fun OrderScreen(
    uiState: OrderStateListener,
    uiData: OrderDataListener,
    uiEvent: OrderEventListener,
    uiNavigation: OrderNavigationListener
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.WhiteSoft)
    ) {
        OrderHeader(
            onBack = uiNavigation.onBack,
            onChatClick = uiNavigation.onChatClick
        )
        OrderListScrollable(
            orders = uiData.orders,
            uiEvent = uiEvent,
            uiNavigation = uiNavigation
        )
    }
}

@Composable
fun OrderHeader(
    onBack: () -> Unit,
    onChatClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            Text(
                text = "My Order",
                fontFamily = PoppinsFont,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            IconButton(onClick = onChatClick) {
                Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Chat")
            }
        }

        HorizontalDivider()
    }
}

@Composable
fun OrderListScrollable(
    orders: List<OrderModel>,
    uiEvent: OrderEventListener,
    uiNavigation: OrderNavigationListener
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(orders.size) { index ->
            val order = orders[index]
            OrderCard(order = order, onClick = {
                uiEvent.onOrderClick(order.id)
                uiNavigation.onDetail(order.id)
            })
        }
    }
}

@Composable
fun OrderCard(order: OrderModel, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(AppColor.White, RoundedCornerShape(8.dp))
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            OrderCardHeader(order.status, order.cafeId)

            Spacer(Modifier.height(8.dp))
            HorizontalDivider()

            Spacer(Modifier.height(12.dp))
            OrderItems(order.items)

            Spacer(Modifier.height(12.dp))
            OrderTotalAndButtons(order, onClick)
        }
    }
}

@Composable
fun OrderCardHeader(status: OrderStatus, cafeId: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(cafeId, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
        Spacer(Modifier.weight(1f))
        StatusBadge(status)
    }
}

@Composable
fun OrderItems(items: List<OrderItemModel>) {
    items.forEach { item ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val imageRes = when (item.foodId) {
                0 -> R.drawable.image_burger
                1 -> R.drawable.image_pizza
                2 -> R.drawable.image_platbread
                3 -> R.drawable.image_cheese_burger
                4 -> R.drawable.image_bakso
                5 -> R.drawable.image_pempek
                6 -> R.drawable.image_padang
                7 -> R.drawable.image_sate
                else -> R.drawable.image_burger
            }

            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text("Produk ${item.foodId}", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(4.dp))
                Text("${item.quantity} barang • ${item.price.toInt()} /pcs", fontSize = 12.sp, color = Color.Gray)
                Spacer(Modifier.height(4.dp))
                Text("Berat: ${(item.quantity * 0.3)} kg", fontSize = 12.sp, color = Color.Gray)
            }

            Text(
                "Rp ${(item.price * item.quantity).toInt()}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun OrderTotalAndButtons(order: OrderModel, onClick: () -> Unit) {
    Column {
        HorizontalDivider()
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total (${order.items.sumOf { it.quantity }} barang)", fontSize = 12.sp, color = Color.Gray)
            Text("Rp ${order.totalPrice.toInt()}", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                onClick = onClick, shape = RoundedCornerShape(6.dp), colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = AppColor.White,
                    contentColor = AppColor.Orange
                )
            ) {
                Text(
                    "Chat",
                    color = AppColor.Orange
                )
            }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(
                onClick = onClick, shape = RoundedCornerShape(6.dp), colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = AppColor.Orange,
                    contentColor = AppColor.White
                )
            ) {
                Text("Beli Lagi")
            }
        }
    }
}

@Composable
fun StatusBadge(status: OrderStatus) {
    val color = when (status) {
        OrderStatus.ORDERED -> Color(0xFFFFA726)
        OrderStatus.COOKING -> AppColor.Orange
        OrderStatus.DELIVERING -> Color(0xFF1E88E5)
        OrderStatus.COMPLETED -> Color(0xFF2E7D32)
    }

    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = status.value,
            color = color,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun OrderScreenPreview() {
    val dummyOrders = listOf(
        OrderModel(
            id = "A001",
            customerId = "C001",
            cafeId = "Mamah Al Cafe",
            items = listOf(
                OrderItemModel(foodId = 0, quantity = 2, price = 15000.0),
                OrderItemModel(foodId = 3, quantity = 1, price = 15000.0)
            ),
            totalPrice = 45000.0,
            status = OrderStatus.COOKING
        ),
        OrderModel(
            id = "A002",
            customerId = "C001",
            cafeId = "Mamah Al Cafe",
            items = listOf(
                OrderItemModel(foodId = 1, quantity = 1, price = 30000.0)
            ),
            totalPrice = 30000.0,
            status = OrderStatus.DELIVERING
        ),
        OrderModel(
            id = "A003",
            customerId = "C001",
            cafeId = "Mamah Al Cafe",
            items = listOf(
                OrderItemModel(foodId = 2, quantity = 1, price = 20000.0),
                OrderItemModel(foodId = 5, quantity = 2, price = 16000.0)
            ),
            totalPrice = 52000.0,
            status = OrderStatus.COMPLETED
        )
    )

    OrderScreen(
        uiState = OrderStateListener(isLoading = false),
        uiData = OrderDataListener(orders = dummyOrders),
        uiEvent = OrderEventListener(),
        uiNavigation = OrderNavigationListener()
    )
}