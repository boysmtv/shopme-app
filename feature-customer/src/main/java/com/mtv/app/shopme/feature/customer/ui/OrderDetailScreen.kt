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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.shimmerBrush
import com.mtv.app.shopme.domain.model.Order
import com.mtv.app.shopme.domain.model.PaymentMethod
import com.mtv.app.shopme.domain.model.PaymentStatus
import com.mtv.app.shopme.feature.customer.contract.OrderDetailEvent
import com.mtv.app.shopme.feature.customer.contract.OrderDetailUiState

@Composable
fun OrderDetailScreen(
    state: OrderDetailUiState,
    event: (OrderDetailEvent) -> Unit
) {
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
        OrderDetailHeader(
            onBack = { event(OrderDetailEvent.ClickBack) },
            onChat = { event(OrderDetailEvent.ClickChat) }
        )

        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            colors = CardDefaults.cardColors(containerColor = AppColor.WhiteSoft)
        ) {
            val order = state.order
            if (order == null) {
                if (state.isLoading) {
                    OrderDetailShimmer()
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Detail pesanan tidak tersedia",
                            fontFamily = PoppinsFont,
                            color = AppColor.Gray
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OrderDetailSummary(order = order)
                    OrderDetailSection(
                        title = "Alamat Pengiriman",
                        value = order.deliveryAddress.ifBlank { "-" }
                    )
                    OrderDetailItems(order = order)
                    OrderDetailSection(
                        title = "Pembayaran",
                        value = "${order.paymentMethod.name} • ${order.paymentStatus.name}"
                    )
                    OrderDetailSection(
                        title = "Total",
                        value = "Rp ${order.totalPrice.toInt()}"
                    )

                    if (order.canConfirmTransfer()) {
                        Button(
                            onClick = { event(OrderDetailEvent.ConfirmTransfer) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B))
                        ) {
                            Text("Konfirmasi Transfer", color = Color.White, fontFamily = PoppinsFont)
                        }
                    }

                    OutlinedButton(
                        onClick = { event(OrderDetailEvent.ClickChat) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColor.Green)
                    ) {
                        Text("Buka Chat", fontFamily = PoppinsFont)
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderDetailShimmer() {
    val brush = shimmerBrush()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(4) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.45f)
                            .height(16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(14.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderDetailHeader(
    onBack: () -> Unit,
    onChat: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable { onBack() }
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Detail Pesanan",
                color = Color.White,
                fontFamily = PoppinsFont,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Lihat status dan item pesanan kamu",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Chat,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable { onChat() }
        )
    }
}

@Composable
private fun OrderDetailSummary(order: Order) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = order.cafeName.ifBlank { order.cafeId },
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = AppColor.Green
            )
            Text(
                text = "Order ${order.id}",
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.SemiBold
            )
            HorizontalDivider(color = AppColor.Green.copy(alpha = 0.15f))
            Text(text = "Status pesanan: ${order.status.name}", color = AppColor.Gray, fontSize = 13.sp)
            Text(text = "Dibuat: ${order.createdAt.ifBlank { "-" }}", color = AppColor.Gray, fontSize = 13.sp)
        }
    }
}

@Composable
private fun OrderDetailItems(order: Order) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Item Pesanan",
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.Bold,
                color = AppColor.Green
            )
            order.items.forEachIndexed { index, item ->
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = item.foodName.ifBlank { item.foodId },
                        fontFamily = PoppinsFont,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${item.quantity}x • Rp ${item.price.toInt()}",
                        color = AppColor.Gray,
                        fontSize = 13.sp
                    )
                    if (!item.notes.isNullOrBlank()) {
                        Text(
                            text = "Catatan: ${item.notes}",
                            color = AppColor.Gray,
                            fontSize = 12.sp
                        )
                    }
                }
                if (index != order.items.lastIndex) {
                    HorizontalDivider(color = AppColor.Green.copy(alpha = 0.12f))
                }
            }
        }
    }
}

@Composable
private fun OrderDetailSection(
    title: String,
    value: String
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.Bold,
                color = AppColor.Green
            )
            Text(
                text = value,
                color = AppColor.Gray,
                fontSize = 13.sp
            )
        }
    }
}

private fun Order.canConfirmTransfer(): Boolean {
    return paymentMethod == PaymentMethod.TRANSFER &&
            paymentStatus in setOf(PaymentStatus.WAITING_UPLOAD, PaymentStatus.FAILED)
}
