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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.mtv.app.shopme.common.ShimmerBlock
import com.mtv.app.shopme.common.ShimmerLine
import com.mtv.app.shopme.domain.model.PaymentMethod
import com.mtv.app.shopme.domain.model.PaymentStatus
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailEvent
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailUiState
import com.mtv.app.shopme.feature.seller.contract.SellerOrderLineItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SellerOrderDetailScreen(
    state: SellerOrderDetailUiState,
    event: (SellerOrderDetailEvent) -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = { event(SellerOrderDetailEvent.Load) }
    )
    Scaffold(
        containerColor = AppColor.White,
        bottomBar = {
            UpdateStatusBottomBar(
                currentStatus = state.currentStatus,
                onUpdate = {
                    event(SellerOrderDetailEvent.ChangeStatus(it))
                    event(SellerOrderDetailEvent.SaveStatus)
                },
                onCancel = { event(SellerOrderDetailEvent.ClickCancelOrder) }
            )
        }
    ) { innerPadding ->
        if (state.showCancelDialog) {
            CancelSellerOrderDialog(
                reason = state.cancelReason,
                onReasonChange = { event(SellerOrderDetailEvent.ChangeCancelReason(it)) },
                onDismiss = { event(SellerOrderDetailEvent.DismissCancelDialog) },
                onConfirm = { event(SellerOrderDetailEvent.SubmitCancelOrder) }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .padding(innerPadding)
        ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SellerOrderDetailHeader(
                status = state.currentStatus,
                onBack = { event(SellerOrderDetailEvent.ClickBack) },
                onChat = { event(SellerOrderDetailEvent.ClickChat) }
            )

            if (state.isLoading) {
                SellerOrderDetailShimmer()
            } else {
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
                    item { PaymentSection(state.total, state.paymentMethod, state.paymentStatus, state.currentStatus) }
                }
            }
        }
            PullRefreshIndicator(
                refreshing = state.isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun SellerOrderDetailShimmer() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .background(AppColor.WhiteSoft)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ShimmerLine(widthFraction = 0.3f, heightDp = 16)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(4) { index ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            ShimmerBlock(
                                modifier = Modifier.size(28.dp),
                                shape = CircleShape
                            )
                            Spacer(Modifier.height(6.dp))
                            ShimmerLine(widthFraction = 0.16f, heightDp = 10)
                        }
                        if (index < 3) {
                            ShimmerBlock(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(4.dp),
                                shape = RoundedCornerShape(2.dp)
                            )
                        }
                    }
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ShimmerLine(widthFraction = 0.26f, heightDp = 16)
                repeat(2) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            ShimmerLine(widthFraction = 0.42f, heightDp = 14)
                            Spacer(Modifier.height(8.dp))
                            ShimmerLine(widthFraction = 0.2f, heightDp = 12)
                            Spacer(Modifier.height(8.dp))
                            ShimmerLine(widthFraction = 0.56f, heightDp = 12)
                        }
                        Spacer(Modifier.width(12.dp))
                        ShimmerLine(widthFraction = 0.18f, heightDp = 14)
                    }
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ShimmerLine(widthFraction = 0.28f, heightDp = 16)
                repeat(2) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ShimmerLine(widthFraction = 0.2f, heightDp = 13)
                        ShimmerLine(widthFraction = 0.34f, heightDp = 13)
                    }
                }
            }
        }
    }
}

@Composable
fun SellerOrderDetailHeader(
    status: OrderStatus,
    onBack: () -> Unit,
    onChat: () -> Unit
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
                text = "Detail Pesanan",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                fontFamily = PoppinsFont
            )
            Spacer(Modifier.height(4.dp))
            StatusChip(status)
        }

        Spacer(Modifier.weight(1f))

        IconButton(
            onClick = onChat,
            modifier = Modifier
                .size(44.dp)
                .background(AppColor.BlueSoft, CircleShape)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Chat,
                contentDescription = "Chat customer",
                tint = AppColor.Blue
            )
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
            text = status.displayLabel(),
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
        Text("Progress Pesanan", fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont)
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
            Text(step.displayLabel(), fontSize = 10.sp, color = color, fontFamily = PoppinsFont)
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
        Text("Item Pesanan", fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont)
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
            Text("Jumlah: $qty", fontSize = 12.sp, color = Color.Gray)
            if (notes.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                Text("Catatan: $notes", fontSize = 12.sp, color = Color.Gray)
            }
        }

        Text(price, fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont)
    }
}

@Composable
fun CustomerSection(customerName: String, customerAddress: String) {
    Column {
        Text("Info Customer", fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont)
        Spacer(Modifier.height(12.dp))
        InfoCard("Nama", customerName)
        Spacer(Modifier.height(8.dp))
        InfoCard("Alamat", customerAddress)
    }
}

@Composable
fun PaymentSection(
    total: String,
    paymentMethod: String,
    paymentStatus: String,
    orderStatus: OrderStatus
) {
    Column {
        Text("Info Pembayaran", fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont)
        Spacer(Modifier.height(12.dp))
        InfoCard("Metode", paymentMethod.displayPaymentMethodLabel())
        Spacer(Modifier.height(8.dp))
        InfoCard("Status", paymentStatus.displayPaymentStatusLabel(), highlight = true)
        Spacer(Modifier.height(8.dp))
        InfoCard("Total", total, highlight = true)
        val note = paymentNote(paymentMethod, paymentStatus, orderStatus)
        if (note.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(note, color = Color.Gray, fontSize = 12.sp, fontFamily = PoppinsFont)
        }
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
fun UpdateStatusBottomBar(
    currentStatus: OrderStatus,
    onUpdate: (OrderStatus) -> Unit,
    onCancel: () -> Unit
) {
    val nextStatus = nextSellerStatus(currentStatus)
    if (nextStatus == null && !currentStatus.canSellerCancel()) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (nextStatus != null) {
            val color = nextStatus.statusColor()
            Button(
                onClick = { onUpdate(nextStatus) },
                modifier = Modifier
                    .fillMaxWidth()
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

        if (currentStatus.canSellerCancel()) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626))
            ) {
                Text(
                    "Tolak / Batalkan Pesanan",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PoppinsFont
                )
            }
        }
    }
}

@Composable
private fun CancelSellerOrderDialog(
    reason: String,
    onReasonChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Tolak / Batalkan Pesanan",
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    "Alasan ini akan masuk ke timeline dan notifikasi customer.",
                    color = Color.Gray,
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

private fun sellerProgressSteps(): List<OrderStatus> = listOf(
    OrderStatus.ORDERED,
    OrderStatus.COOKING,
    OrderStatus.DELIVERING,
    OrderStatus.COMPLETED
)

private fun nextSellerStatus(currentStatus: OrderStatus): OrderStatus? = when (currentStatus) {
    OrderStatus.UNPAID -> OrderStatus.COOKING
    OrderStatus.ORDERED -> OrderStatus.COOKING
    OrderStatus.COOKING -> OrderStatus.DELIVERING
    OrderStatus.DELIVERING -> OrderStatus.COMPLETED
    else -> null
}

private fun String.displayPaymentMethodLabel(): String = when (uppercase()) {
    PaymentMethod.CASH.name -> "Bayar di tempat"
    PaymentMethod.TRANSFER.name -> "Transfer bank"
    else -> ifBlank { "-" }
}

private fun String.displayPaymentStatusLabel(): String = when (uppercase()) {
    PaymentStatus.UNPAID.name -> "Belum dibayar"
    PaymentStatus.WAITING_UPLOAD.name -> "Menunggu konfirmasi transfer"
    PaymentStatus.WAITING_CONFIRMATION.name -> "Menunggu verifikasi seller"
    PaymentStatus.PAID.name -> "Sudah dibayar"
    PaymentStatus.FAILED.name -> "Pembayaran gagal"
    else -> ifBlank { "-" }
}

private fun paymentNote(method: String, status: String, orderStatus: OrderStatus): String {
    return when {
        method.equals(PaymentMethod.CASH.name, ignoreCase = true) -> "Customer akan bayar langsung saat pesanan diterima."
        status.equals(PaymentStatus.WAITING_UPLOAD.name, ignoreCase = true) &&
                orderStatus in setOf(OrderStatus.COOKING, OrderStatus.DELIVERING) ->
            "Customer sudah bisa konfirmasi transfer untuk pesanan ini."
        status.equals(PaymentStatus.WAITING_UPLOAD.name, ignoreCase = true) ->
            "Konfirmasi transfer belum diminta sampai pesanan mulai diproses."
        status.equals(PaymentStatus.WAITING_CONFIRMATION.name, ignoreCase = true) ->
            "Bukti transfer menunggu verifikasi seller."
        status.equals(PaymentStatus.PAID.name, ignoreCase = true) -> "Pembayaran sudah dikonfirmasi."
        status.equals(PaymentStatus.FAILED.name, ignoreCase = true) -> "Pembayaran gagal dan perlu dikonfirmasi ulang."
        else -> ""
    }
}

private fun OrderStatus.canSellerCancel(): Boolean {
    return this !in setOf(OrderStatus.COMPLETED, OrderStatus.CANCELLED)
}

fun OrderStatus.statusColor(): Color = when (this) {
    OrderStatus.UNPAID -> Color(0xFFDC2626)
    OrderStatus.ORDERED -> Color(0xFFFFA000)
    OrderStatus.COOKING -> Color(0xFF1E88E5)
    OrderStatus.DELIVERING -> Color(0xFF00897B)
    OrderStatus.COMPLETED -> Color(0xFF2E7D32)
    OrderStatus.CANCELLED -> Color(0xFF6B7280)
}

private fun OrderStatus.displayLabel(): String = when (this) {
    OrderStatus.UNPAID -> "Belum dibayar"
    OrderStatus.ORDERED -> "Dipesan"
    OrderStatus.COOKING -> "Diproses"
    OrderStatus.DELIVERING -> "Dikirim"
    OrderStatus.COMPLETED -> "Selesai"
    OrderStatus.CANCELLED -> "Dibatalkan"
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
