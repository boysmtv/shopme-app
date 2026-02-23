/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderScreen.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 09.14
 */

package com.mtv.app.shopme.feature.customer.ui

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.data.OrderItemModel
import com.mtv.app.shopme.data.OrderModel
import com.mtv.app.shopme.data.OrderStatus
import com.mtv.app.shopme.data.PaymentMethod
import com.mtv.app.shopme.feature.customer.contract.OrderDataListener
import com.mtv.app.shopme.feature.customer.contract.OrderEventListener
import com.mtv.app.shopme.feature.customer.contract.OrderNavigationListener
import com.mtv.app.shopme.feature.customer.contract.OrderStateListener

enum class OrderFilter {
    SEMUA,
    ORDERED,
    COOKING,
    DELIVERING,
    COMPLETED
}

@Composable
fun OrderScreen(
    uiState: OrderStateListener,
    uiData: OrderDataListener,
    uiEvent: OrderEventListener,
    uiNavigation: OrderNavigationListener
) {
    var selectedFilter by remember { mutableStateOf(OrderFilter.SEMUA) }
    var showUploadSheet by remember { mutableStateOf(false) }
    var proofUri by remember { mutableStateOf<Uri?>(null) }
    var selectedOrderId by remember { mutableStateOf<String?>(null) }

    val filteredOrders = uiData.orders.filter {
        when (selectedFilter) {
            OrderFilter.SEMUA -> true
            OrderFilter.ORDERED -> it.status == OrderStatus.ORDERED
            OrderFilter.COOKING -> it.status == OrderStatus.COOKING
            OrderFilter.DELIVERING -> it.status == OrderStatus.DELIVERING
            OrderFilter.COMPLETED -> it.status == OrderStatus.COMPLETED
        }
    }
    if (showUploadSheet) {
        UploadProofSheet(
            imageUri = proofUri,
            onTakePhoto = { /* TODO camera */ },
            onPickGallery = { /* TODO gallery */ },
            onUpload = {
                showUploadSheet = false
            },
            onDismiss = {
                showUploadSheet = false
            }
        )
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

        ModernOrderTopBar(uiNavigation)

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
                                    uiEvent.onOrderClick(order.id)
                                    uiNavigation.onDetail(order.id)
                                },
                                onUploadProofClick = { orderId ->
                                    selectedOrderId = orderId
                                    showUploadSheet = true
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
private fun ModernOrderTopBar(nav: OrderNavigationListener) {
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
            tint = Color.White
        )
    }
}

@Composable
fun ModernOrderCard(
    order: OrderModel,
    onClick: () -> Unit,
    onUploadProofClick: (String) -> Unit
){

    val statusColor = when (order.status) {
        OrderStatus.ORDERED -> Color(0xFFF59E0B)
        OrderStatus.COOKING -> AppColor.Green
        OrderStatus.DELIVERING -> Color(0xFF2563EB)
        OrderStatus.COMPLETED -> Color(0xFF16A34A)
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
                order.cafeId,
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
                    "• Produk ${it.foodId} x${it.quantity}",
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

            val isOrdered = order.status == OrderStatus.ORDERED
            val isCompleted = order.status == OrderStatus.COMPLETED
            val isTransfer = order.paymentMethod == PaymentMethod.TRANSFER

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
                    if (isOrdered && isTransfer) {

                        Button(
                            onClick = { onUploadProofClick(order.id) },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF59E0B)
                            )
                        ) {
                            Text("Upload Bukti Transfer", color = Color.White)
                        }

                        Spacer(Modifier.width(10.dp))
                    }

                    OutlinedButton(
                        onClick = onClick,
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
        OrderModel(
            id = "A001",
            customerId = "C001",
            cafeId = "Mamah Al Cafe",
            items = listOf(
                OrderItemModel(foodId = 0, quantity = 2, price = 15000.0),
                OrderItemModel(foodId = 3, quantity = 1, price = 15000.0)
            ),
            totalPrice = 45000.0,
            status = OrderStatus.ORDERED,
            paymentMethod = PaymentMethod.TRANSFER
        ),
        OrderModel(
            id = "A002",
            customerId = "C001",
            cafeId = "Mamah Al Cafe",
            items = listOf(
                OrderItemModel(foodId = 1, quantity = 1, price = 30000.0)
            ),
            totalPrice = 30000.0,
            status = OrderStatus.DELIVERING,
            paymentMethod = PaymentMethod.TRANSFER
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
            status = OrderStatus.COMPLETED,
            paymentMethod = PaymentMethod.TRANSFER
        )
    )

    OrderScreen(
        uiState = OrderStateListener(isLoading = false),
        uiData = OrderDataListener(orders = dummyOrders),
        uiEvent = OrderEventListener(),
        uiNavigation = OrderNavigationListener()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadProofSheet(
    imageUri: Uri?,
    onTakePhoto: () -> Unit,
    onPickGallery: () -> Unit,
    onUpload: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = AppColor.GreenSoft,
        dragHandle = {
            Box(
                Modifier
                    .padding(vertical = 10.dp)
                    .size(width = 42.dp, height = 4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(AppColor.Gray.copy(alpha = 0.3f))
            )
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ===== HEADER ICON =====
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(AppColor.Green.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                    contentDescription = null,
                    tint = AppColor.Green,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = "Upload Bukti Transfer",
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Upload foto bukti pembayaran kamu",
                fontSize = 13.sp,
                color = AppColor.Gray
            )

            Spacer(Modifier.height(18.dp))

            // ===== IMAGE PREVIEW CARD =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFF7F7F7))
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE5E5E5),
                        shape = RoundedCornerShape(18.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                if (imageUri == null) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = AppColor.Gray,
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            "Belum ada foto",
                            fontSize = 13.sp,
                            color = AppColor.Gray
                        )
                    }

                } else {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Pastikan nominal & rekening terlihat jelas",
                fontSize = 12.sp,
                color = AppColor.Gray
            )

            Spacer(Modifier.height(18.dp))

            // ===== PICK BUTTONS =====
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedButton(
                    onClick = onTakePhoto,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PhotoCamera, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Kamera")
                }

                OutlinedButton(
                    onClick = onPickGallery,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Image, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Gallery")
                }
            }

            Spacer(Modifier.height(20.dp))

            // ===== UPLOAD BUTTON =====
            Button(
                onClick = onUpload,
                enabled = imageUri != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColor.Green,
                    disabledContainerColor = AppColor.Gray.copy(alpha = 0.3f)
                )
            ) {
                Text(
                    text = if (imageUri == null) "Pilih Foto Dulu" else "Upload Bukti Transfer",
                    color = Color.White,
                    fontFamily = PoppinsFont
                )
            }

            Spacer(Modifier.height(6.dp))
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun UploadProofSheetPreview_Empty() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x66000000)),
        contentAlignment = Alignment.BottomCenter
    ) {
        UploadProofSheet(
            imageUri = null,
            onTakePhoto = {},
            onPickGallery = {},
            onUpload = {},
            onDismiss = {}
        )
    }
}