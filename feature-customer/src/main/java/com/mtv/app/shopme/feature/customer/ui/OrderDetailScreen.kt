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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
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
import com.mtv.app.shopme.common.ShimmerBlock
import com.mtv.app.shopme.common.ShimmerLine
import com.mtv.app.shopme.common.toRupiah
import com.mtv.app.shopme.domain.model.Order
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.domain.model.OrderTimeline
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
        if (state.showCancelDialog) {
            CancelOrderDialog(
                title = "Batalkan Pesanan",
                reason = state.cancelReason,
                onReasonChange = { event(OrderDetailEvent.ChangeCancelReason(it)) },
                onDismiss = { event(OrderDetailEvent.DismissCancelDialog) },
                onConfirm = { event(OrderDetailEvent.SubmitCancelOrder) }
            )
        }

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
                    OrderTimelineSection(order = order)
                    OrderDetailSection(
                        title = "Alamat Pengiriman",
                        value = order.deliveryAddress.ifBlank { "-" }
                    )
                    OrderDetailItems(order = order)
                    PaymentDetailSection(order = order)
                    OrderDetailSection(
                        title = "Total",
                        value = order.totalPrice.toRupiah()
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

                    if (order.canCustomerCancel()) {
                        OutlinedButton(
                            onClick = { event(OrderDetailEvent.ClickCancelOrder) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626))
                        ) {
                            Text("Batalkan Pesanan", fontFamily = PoppinsFont)
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ShimmerLine(widthFraction = 0.46f, heightDp = 20)
                ShimmerLine(widthFraction = 0.34f, heightDp = 14)
                HorizontalDivider(color = AppColor.Green.copy(alpha = 0.12f))
                ShimmerLine(widthFraction = 0.58f, heightDp = 13)
                ShimmerLine(widthFraction = 0.52f, heightDp = 13)
            }
        }

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
                ShimmerLine(widthFraction = 0.34f, heightDp = 16)
                repeat(2) { index ->
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        ShimmerLine(widthFraction = 0.5f, heightDp = 14)
                        ShimmerLine(widthFraction = 0.3f, heightDp = 12)
                        if (index == 0) {
                            ShimmerLine(widthFraction = 0.62f, heightDp = 12)
                        }
                    }
                    if (index == 0) {
                        HorizontalDivider(color = AppColor.Green.copy(alpha = 0.12f))
                    }
                }
            }
        }

        repeat(2) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ShimmerLine(widthFraction = 0.28f, heightDp = 16)
                    ShimmerLine(widthFraction = 0.74f, heightDp = 13)
                }
            }
        }

        ShimmerBlock(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(14.dp)
        )
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
                text = order.items.firstOrNull()?.foodName?.ifBlank { null }
                    ?: "Pesanan ${order.status.name}",
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.SemiBold
            )
            HorizontalDivider(color = AppColor.Green.copy(alpha = 0.15f))
            Text(text = "Status pesanan: ${order.status.displayLabel()}", color = AppColor.Gray, fontSize = 13.sp)
            Text(text = "Dibuat: ${order.createdAt.ifBlank { "-" }}", color = AppColor.Gray, fontSize = 13.sp)
        }
    }
}

@Composable
private fun CancelOrderDialog(
    title: String,
    reason: String,
    onReasonChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(title, fontFamily = PoppinsFont, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Tulis alasan singkat agar status pesanan jelas di timeline.",
                    color = AppColor.Gray,
                    fontSize = 13.sp,
                    fontFamily = PoppinsFont
                )
                OutlinedTextField(
                    value = reason,
                    onValueChange = onReasonChange,
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    label = { Text("Alasan") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Batalkan", color = Color(0xFFDC2626), fontFamily = PoppinsFont)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup", fontFamily = PoppinsFont)
            }
        }
    )
}

@Composable
private fun OrderTimelineSection(order: Order) {
    val timeline = order.timeline.ifEmpty {
        listOf(
            OrderTimeline(
                status = order.status,
                actorRole = "system",
                createdAt = order.createdAt
            )
        )
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Timeline Pesanan",
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.Bold,
                color = AppColor.Green
            )
            timeline.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 3.dp)
                            .size(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(if (index == timeline.lastIndex) AppColor.Green else AppColor.Gray)
                    )
                    Spacer(Modifier.width(10.dp))
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = item.status.displayLabel(),
                            fontFamily = PoppinsFont,
                            fontWeight = FontWeight.SemiBold,
                            color = AppColor.Black,
                            fontSize = 13.sp
                        )
                        Text(
                            text = buildTimelineSubtitle(item),
                            color = AppColor.Gray,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

private fun buildTimelineSubtitle(item: OrderTimeline): String {
    val actor = when (item.actorRole.lowercase()) {
        "seller" -> "Seller"
        "customer" -> "Customer"
        else -> "Sistem"
    }
    val time = item.createdAt.ifBlank { "-" }
    val reason = item.reason.ifBlank { "" }
    return if (reason.isBlank()) "$actor - $time" else "$actor - $time - $reason"
}

@Composable
private fun PaymentDetailSection(order: Order) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Pembayaran",
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.Bold,
                color = AppColor.Green
            )
            PaymentInfoRow("Metode", order.paymentMethod.displayLabel())
            PaymentInfoRow("Status", order.paymentStatus.displayLabel(), highlight = true)
            val note = order.paymentInstruction()
            if (note.isNotBlank()) {
                Text(
                    text = note,
                    color = AppColor.Gray,
                    fontSize = 12.sp,
                    fontFamily = PoppinsFont
                )
            }
        }
    }
}

@Composable
private fun PaymentInfoRow(
    label: String,
    value: String,
    highlight: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = AppColor.Gray, fontSize = 13.sp, fontFamily = PoppinsFont)
        Text(
            value,
            color = if (highlight) AppColor.Green else AppColor.Black,
            fontWeight = if (highlight) FontWeight.SemiBold else FontWeight.Medium,
            fontSize = 13.sp,
            fontFamily = PoppinsFont
        )
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
                        text = "${item.quantity}x • ${item.price.toRupiah()}",
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
            paymentStatus in setOf(PaymentStatus.WAITING_UPLOAD, PaymentStatus.FAILED) &&
            status in setOf(OrderStatus.COOKING, OrderStatus.DELIVERING)
}

private fun Order.canCustomerCancel(): Boolean {
    return status in setOf(OrderStatus.UNPAID, OrderStatus.ORDERED)
}

private fun Order.paymentInstruction(): String {
    return when {
        paymentMethod == PaymentMethod.CASH -> "Bayar langsung saat pesanan diterima."
        paymentStatus == PaymentStatus.WAITING_UPLOAD &&
                status in setOf(OrderStatus.COOKING, OrderStatus.DELIVERING) ->
            "Pesanan sudah diproses. Silakan konfirmasi transfer."
        paymentStatus == PaymentStatus.WAITING_UPLOAD ->
            "Konfirmasi transfer akan tersedia setelah seller mulai memproses pesanan."
        paymentStatus == PaymentStatus.WAITING_CONFIRMATION ->
            "Bukti transfer sedang menunggu konfirmasi seller."
        paymentStatus == PaymentStatus.PAID -> "Pembayaran sudah dikonfirmasi."
        paymentStatus == PaymentStatus.FAILED -> "Pembayaran gagal. Silakan konfirmasi ulang."
        else -> ""
    }
}

private fun PaymentMethod.displayLabel(): String = when (this) {
    PaymentMethod.CASH -> "Bayar di tempat"
    PaymentMethod.TRANSFER -> "Transfer bank"
}

private fun PaymentStatus.displayLabel(): String = when (this) {
    PaymentStatus.UNPAID -> "Belum dibayar"
    PaymentStatus.WAITING_UPLOAD -> "Menunggu konfirmasi transfer"
    PaymentStatus.WAITING_CONFIRMATION -> "Menunggu verifikasi seller"
    PaymentStatus.PAID -> "Sudah dibayar"
    PaymentStatus.FAILED -> "Pembayaran gagal"
}

private fun OrderStatus.displayLabel(): String = when (this) {
    OrderStatus.UNPAID -> "Belum dibayar"
    OrderStatus.ORDERED -> "Dipesan"
    OrderStatus.COOKING -> "Diproses"
    OrderStatus.DELIVERING -> "Dikirim"
    OrderStatus.COMPLETED -> "Selesai"
    OrderStatus.CANCELLED -> "Dibatalkan"
}
