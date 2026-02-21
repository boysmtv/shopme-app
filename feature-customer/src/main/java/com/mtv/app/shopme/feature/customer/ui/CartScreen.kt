/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartScreen.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 09.14
 */

package com.mtv.app.shopme.feature.customer.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.navigation.compose.rememberNavController
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.feature.customer.contract.CartDataListener
import com.mtv.app.shopme.feature.customer.contract.CartEventListener
import com.mtv.app.shopme.feature.customer.contract.CartNavigationListener
import com.mtv.app.shopme.feature.customer.contract.CartStateListener
import com.mtv.app.shopme.nav.CustomerBottomNavigationBar
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

@Composable
fun CartScreen(
    uiState: CartStateListener,
    uiData: CartDataListener,
    uiEvent: CartEventListener,
    uiNavigation: CartNavigationListener
) {
    var showCheckoutDialog by remember { mutableStateOf(false) }
    var showPinSheet by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

//    if (showCheckoutDialog) {
//        CheckoutConfirmationDialog(
//            total = grandTotal,
//            onDismiss = { showCheckoutDialog = false },
//            onConfirm = {
//                showCheckoutDialog = false
//                uiNavigation.onNavigateToOrder()
//            }
//        )
//    }

    if (showCheckoutDialog) {
        PremiumCheckoutSheet(
            total = grandTotal,
            onDismiss = { showCheckoutDialog = false },
            onConfirm = {
                showCheckoutDialog = false
                showPinSheet = true
            }
        )
    }

    if (showPinSheet) {
        PinVerificationSheet(
            onDismiss = { showPinSheet = false },
            onSuccess = {
                showPinSheet = false
                showSuccessDialog = true
            }
        )
    }

    if (showSuccessDialog) {
        OrderSuccessDialog(
            onConfirm = {
                showSuccessDialog = false
                uiNavigation.onNavigateToOrder()
            }
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.White)
            .padding(start = 16.dp, end = 16.dp)
            .height(56.dp)
            .statusBarsPadding()
    ) {
        CartHeader()

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total ${cartItems.size} Items",
                fontFamily = PoppinsFont,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Clear Cart",
                fontFamily = PoppinsFont,
                fontSize = 14.sp,
                color = AppColor.Green,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val groupedByCafe = cartItems.groupBy { it.cafeId }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            groupedByCafe.forEach { (_, items) ->
                item {
                    CafeGroupCard(items)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    Text(
                        fontFamily = PoppinsFont,
                        text = "Total shopping cart",
                        color = AppColor.Gray,
                        fontSize = 12.sp,
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        fontFamily = PoppinsFont,
                        text = "$${"%.2f".format(grandTotal)}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        showCheckoutDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(AppColor.Green),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .height(42.dp)
                ) {
                    Text(
                        fontFamily = PoppinsFont,
                        text = "Checkout",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                    )
                }
            }
        }
    }
}

@Composable
private fun CartHeader() {
    Box(
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "My Cart",
            color = Color.Black,
            fontFamily = PoppinsFont,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Center),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun CafeGroupCard(items: List<CartItem>) {
    val cafeSubtotal = items.sumOf { it.totalPrice() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = AppColor.WhiteSoft,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = AppColor.Green,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = items.first().cafeName,
                    fontFamily = PoppinsFont,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = AppColor.Green,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(color = AppColor.Gray, modifier = Modifier.height(1.dp))

        items.forEachIndexed { index, item ->
            CartItemRow(item)

            if (index != items.lastIndex) {
                HorizontalDivider(
                    color = Color.LightGray.copy(alpha = 0.3f),
                    thickness = 1.dp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    AppColor.GreenSoft.copy(alpha = 0.7f),
                    RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total ${items.size} items",
                fontFamily = PoppinsFont,
                fontSize = 13.sp,
                color = AppColor.Gray
            )

            Text(
                text = "$${"%.2f".format(cafeSubtotal)}",
                fontFamily = PoppinsFont,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun CafeHeader(
    cafeName: String,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColor.White)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = cafeName,
            fontFamily = PoppinsFont,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Clear",
            fontFamily = PoppinsFont,
            fontSize = 13.sp,
            color = Color.Red,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun CafeSubtotal(items: List<CartItem>) {
    val subtotal = items.sumOf { it.price * it.quantity }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColor.GreenSoft.copy(alpha = 0.15f))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Subtotal",
            fontFamily = PoppinsFont,
            fontSize = 14.sp,
            color = AppColor.Gray
        )
        Text(
            text = "$${"%.2f".format(subtotal)}",
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}


@Composable
fun CartItemRow(item: CartItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = item.image),
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    fontFamily = PoppinsFont,
                    text = item.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                StatusStatItem(OrderStatus.READY)
            }

            Spacer(Modifier.height(6.dp))

            Text(
                fontFamily = PoppinsFont,
                text = "Notes: ${item.notes}",
                color = Color.DarkGray,
                fontSize = 12.sp
            )

            Spacer(Modifier.height(6.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    fontFamily = PoppinsFont,
                    text = "$${item.price}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )

                QuantityCounter(item.quantity)
            }
        }
    }
}

@Composable
fun QuantityCounter(quantity: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(
                    if (quantity == 1)
                        Color.Red.copy(alpha = 0.15f)
                    else
                        Color.LightGray.copy(alpha = 0.3f)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (quantity == 1) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = null,
                    tint = AppColor.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Text(
            fontFamily = PoppinsFont,
            text = quantity.toString(),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )

        Spacer(Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(AppColor.Green),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

val cartItems = listOf(
    CartItem(
        cafeId = "cafe_1",
        cafeName = "Burger Queen",
        title = "Classic Burger",
        weight = "250g",
        price = 9.99,
        image = R.drawable.image_burger,
        quantity = 2,
        notes = "Extra cheese"
    ),
    CartItem(
        cafeId = "cafe_1",
        cafeName = "Burger Queen",
        title = "Cheeseburger",
        weight = "270g",
        price = 10.49,
        image = R.drawable.image_cheese_burger,
        quantity = 1,
    ),

    CartItem(
        cafeId = "cafe_2",
        cafeName = "Pizza Palace",
        title = "Pepperoni Pizza",
        weight = "300g",
        price = 10.99,
        image = R.drawable.image_pizza,
        quantity = 1,
        notes = "Sedang"
    ),
    CartItem(
        cafeId = "cafe_2",
        cafeName = "Pizza Palace",
        title = "Margherita Pizza",
        weight = "280g",
        price = 9.49,
        image = R.drawable.image_platbread,
        quantity = 2,
        notes = "Banyakin Sambel"
    ),

    CartItem(
        cafeId = "cafe_3",
        cafeName = "Coffee Corner",
        title = "Cappuccino",
        weight = "200ml",
        price = 4.99,
        image = R.drawable.image_cheese_burger,
        quantity = 2,
        notes = "Jangan Pakai Kuah"
    ),
    CartItem(
        cafeId = "cafe_3",
        cafeName = "Coffee Corner",
        title = "Latte",
        weight = "250ml",
        price = 5.49,
        image = R.drawable.image_bakso,
        quantity = 1
    ),

    CartItem(
        cafeId = "cafe_4",
        cafeName = "Sweet Bakery",
        title = "Chocolate Croissant",
        weight = "120g",
        price = 3.99,
        image = R.drawable.image_padang,
        quantity = 3
    ),
    CartItem(
        cafeId = "cafe_4",
        cafeName = "Sweet Bakery",
        title = "Blueberry Muffin",
        weight = "130g",
        price = 3.49,
        image = R.drawable.image_pempek,
        quantity = 2
    )
)

fun CartItem.totalPrice(): Double = price * quantity
val totalQuantity = cartItems.sumOf { it.quantity }
val grandTotal = cartItems.sumOf { it.totalPrice() }

data class CartItem(
    val cafeId: String,
    val cafeName: String,
    val title: String,
    val weight: String,
    val price: Double,
    val image: Int,
    val quantity: Int,
    val notes: String = EMPTY_STRING
)

enum class PaymentMethod {
    CASH,
    TRANSFER
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumCheckoutSheet(
    total: Double,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var paymentMethod by remember {
        mutableStateOf(PaymentMethod.TRANSFER)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AppColor.GreenSoft,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .size(width = 42.dp, height = 4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(AppColor.Gray.copy(alpha = 0.35f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(AppColor.White)
                    .border(1.dp, AppColor.Green.copy(.18f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = AppColor.Green,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Konfirmasi Checkout",
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Pesanan akan dikirim ke penjual",
                fontFamily = PoppinsFont,
                fontSize = 13.sp,
                color = AppColor.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Metode Pembayaran",
                fontFamily = PoppinsFont,
                fontSize = 14.sp,
                color = AppColor.Gray
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                PaymentPill(
                    title = "Cash",
                    selected = paymentMethod == PaymentMethod.CASH,
                    onClick = { paymentMethod = PaymentMethod.CASH }
                )

                PaymentPill(
                    title = "Transfer",
                    selected = paymentMethod == PaymentMethod.TRANSFER,
                    onClick = { paymentMethod = PaymentMethod.TRANSFER }
                )
            }

            if (paymentMethod == PaymentMethod.TRANSFER) {

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(AppColor.White)
                        .border(
                            width = 1.dp,
                            color = AppColor.Green.copy(alpha = 0.18f),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Segera upload bukti pembayaran di halaman Order setelah melakukan transfer.",
                        fontFamily = PoppinsFont,
                        fontSize = 13.sp,
                        color = Color(0xFF444444),
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Total Pembayaran",
                fontFamily = PoppinsFont,
                fontSize = 12.sp,
                color = AppColor.Gray
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "$${"%.2f".format(total)}",
                fontFamily = PoppinsFont,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AppColor.Green
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(text = "Batal", fontFamily = PoppinsFont)
                }

                Button(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(AppColor.Green),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Checkout",
                        fontFamily = PoppinsFont,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun PaymentPill(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (selected) AppColor.Green.copy(alpha = 0.12f)
                else AppColor.White
            )
            .border(
                width = 1.dp,
                color = if (selected) AppColor.Green else AppColor.Gray.copy(.25f),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontFamily = PoppinsFont,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (selected) AppColor.Green else Color(0xFF444444)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinVerificationSheet(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var pin by remember { mutableStateOf(value = "") }
    var isError by remember { mutableStateOf(value = false) }

    val correctPin = "123456"

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AppColor.GreenSoft,
        shape = RoundedCornerShape(
            topStart = 28.dp,
            topEnd = 28.dp
        ),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .size(width = 42.dp, height = 4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(AppColor.Gray.copy(alpha = 0.35f))
            )
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Masukkan PIN",
                fontFamily = PoppinsFont,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(height = 8.dp))

            Text(
                text = "Untuk konfirmasi pembayaran",
                fontFamily = PoppinsFont,
                fontSize = 14.sp,
                color = AppColor.Gray.copy(alpha = 0.75f)
            )

            Spacer(modifier = Modifier.height(height = 28.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
            ) {
                repeat(times = 6) { index ->
                    val filled = index < pin.length

                    Box(
                        modifier = Modifier
                            .size(size = 14.dp)
                            .clip(shape = CircleShape)
                            .background(
                                color = if (filled) AppColor.Green else Color.Transparent
                            )
                            .border(
                                width = 1.5.dp,
                                color = if (filled) AppColor.Green else Color.LightGray,
                                shape = CircleShape
                            )
                    )
                }
            }

            if (isError) {
                Spacer(modifier = Modifier.height(height = 10.dp))
                Text(
                    text = "PIN salah",
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(height = 28.dp))

            PinKeypad(
                onNumberClick = { number ->
                    if (pin.length < 6) {
                        pin += number
                        isError = false

                        if (pin.length == 6) {
                            if (pin == correctPin) {
                                onSuccess()
                            } else {
                                isError = true
                                pin = ""
                            }
                        }
                    }
                },
                onDeleteClick = {
                    if (pin.isNotEmpty()) {
                        pin = pin.dropLast(n = 1)
                    }
                }
            )

            Spacer(modifier = Modifier.height(height = 16.dp))
        }
    }
}

@Composable
fun PinKeypad(
    onNumberClick: (String) -> Unit,
    onDeleteClick: () -> Unit
) {

    val buttons = listOf(
        "1", "2", "3",
        "4", "5", "6",
        "7", "8", "9",
        "", "0", "del"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        buttons.chunked(size = 3).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(space = 32.dp),
                modifier = Modifier.wrapContentWidth()
            ) {
                row.forEach { key ->
                    val size = 84.dp

                    Box(
                        modifier = Modifier
                            .size(size = size)
                            .clip(shape = CircleShape)
                            .background(color = AppColor.White)
                            .border(
                                width = 1.dp,
                                color = AppColor.Green.copy(alpha = 0.1f),
                                shape = CircleShape
                            )
                            .clickable(enabled = key.isNotEmpty()) {
                                when (key) {
                                    "del" -> onDeleteClick()
                                    else -> onNumberClick(key)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        when (key) {

                            "del" -> Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = AppColor.Gray.copy(alpha = 0.8f),
                                modifier = Modifier.size(size = 20.dp)
                            )

                            "" -> Spacer(modifier = Modifier.size(size = size))

                            else -> Text(
                                text = key,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = PoppinsFont
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderSuccessDialog(
    onConfirm: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.45f)),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .padding(horizontal = 28.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(AppColor.White)
                .padding(horizontal = 24.dp, vertical = 28.dp),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ===== SUCCESS ICON =====
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(AppColor.Green.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = AppColor.Green,
                        modifier = Modifier.size(42.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Pesanan Berhasil 🎉",
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = Color(0xFF1A1A1A)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Pesanan telah dikirimkan ke penjual.\nSilakan cek status di menu Order.",
                    fontFamily = PoppinsFont,
                    fontSize = 14.sp,
                    color = AppColor.Gray.copy(alpha = 0.85f),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(26.dp))

                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColor.Green
                    )
                ) {
                    Text(
                        text = "Lihat Pesanan",
                        fontFamily = PoppinsFont,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun CartScreenPreview() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            CustomerBottomNavigationBar(navController)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(bottom = padding.calculateBottomPadding())
                .fillMaxSize()
                .background(Color.White)
        ) {
            CartScreen(
                uiState = CartStateListener(),
                uiData = CartDataListener(),
                uiEvent = CartEventListener(),
                uiNavigation = CartNavigationListener()
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun PremiumCheckoutSheetPreview() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x66000000)),
        contentAlignment = Alignment.BottomCenter
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.55f)
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(Color.White)
        ) {
            PremiumCheckoutSheet(
                total = 129.97,
                onDismiss = {},
                onConfirm = {}
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun PinKeypadPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x66000000)),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.60f)
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(Color.White)
        ) {
            PinVerificationSheet(
                onDismiss = {},
                onSuccess = {}
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun OrderSuccessDialogPreview() {
    OrderSuccessDialog(
        onConfirm = {}
    )
}