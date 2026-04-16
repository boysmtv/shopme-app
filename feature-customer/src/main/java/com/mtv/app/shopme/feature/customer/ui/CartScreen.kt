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
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.common.toRupiah
import com.mtv.app.shopme.data.mock.DataUiMock
import com.mtv.app.shopme.domain.model.Cart
import com.mtv.app.shopme.domain.model.FoodStatus
import com.mtv.app.shopme.domain.model.PaymentMethod
import com.mtv.app.shopme.domain.model.SessionToken
import com.mtv.app.shopme.feature.customer.contract.CartEvent
import com.mtv.app.shopme.feature.customer.contract.CartUiState
import com.mtv.app.shopme.feature.customer.ui.shimmer.ShimmerCartScreen
import com.mtv.app.shopme.feature.customer.utils.StatusStatItem
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING
import java.math.BigDecimal
import kotlinx.coroutines.launch

@Composable
fun CartScreen(
    state: CartUiState,
    event: (CartEvent) -> Unit
) {
    when (val cartState = state.cartItems) {

        is LoadState.Loading -> {
            ShimmerCartScreen()
        }

        is LoadState.Success -> {
            val cartItems = cartState.data

            if (cartItems.isEmpty()) {
                EmptyCartView()
            } else {
                CartContent(
                    cartItems = cartItems,
                    state = state,
                    event = event
                )
            }
        }

        else -> Unit
    }
}

@Composable
fun CartContent(
    cartItems: List<Cart>,
    state: CartUiState,
    event: (CartEvent) -> Unit
) {
    var showCheckoutDialog by remember { mutableStateOf(false) }
    var showPinSheet by remember { mutableStateOf(false) }
    var selectedCartIds by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedPaymentMethod by remember { mutableStateOf(PaymentMethod.CASH) }

    val groupedByCafe = cartItems.groupBy { it.cafeId }

    val grandTotal = cartItems.fold(BigDecimal.ZERO) { acc, item ->
        val itemTotal = item.price +
                item.variants.fold(BigDecimal.ZERO) { acc, variant ->
                    acc + variant.price
                }

        acc + (itemTotal * item.quantity.toBigDecimal())
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // ✅ CHECKOUT SHEET (tetap)
    if (showCheckoutDialog) {
        PremiumCheckoutSheet(
            total = grandTotal,
            onDismiss = { showCheckoutDialog = false },
            onConfirm = { paymentMethod ->
                selectedPaymentMethod = paymentMethod
                showCheckoutDialog = false
                event(CartEvent.GetSession)
            }
        )
    }

    // ✅ PIN SHEET (adjust)
    if (showPinSheet) {
        PinVerificationSheet(
            isLoading = false,
            isError = false,
            onDismiss = { showPinSheet = false },
            onSuccess = { pin ->
                val token = (state.sessionToken as? LoadState.Success)?.data?.token.orEmpty()

                event(
                    CartEvent.VerifyPin(
                        token = token,
                        pin = pin
                    )
                )
            }
        )
    }

    // 🔥 REPLACEMENT LaunchedEffect lama
    LaunchedEffect(state.sessionToken) {
        showPinSheet = true
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColor.White)
                .padding(start = 16.dp, end = 16.dp)
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
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clickable {
                            event(CartEvent.ClearCart)
                        }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                groupedByCafe.forEach { (_, items) ->
                    item {
                        CafeGroupCard(
                            items = items,
                            onNavigateToDetail = { /* handle via route */ },
                            onDeleteFoodByCafe = {
                                event(CartEvent.ClearCartByCafe(it))
                            },
                            onMinus = { id, qty ->
                                event(
                                    CartEvent.ChangeQuantity(
                                        id,
                                        if (qty == 1) 0 else qty - 1
                                    )
                                )
                            },
                            onPlus = { id, qty ->
                                event(CartEvent.ChangeQuantity(id, qty + 1))
                            }
                        )
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
                            text = grandTotal.toRupiah(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            if (cartItems.isEmpty()) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Keranjang masih kosong")
                                }
                                return@Button
                            }
                            selectedCartIds = cartItems.map { it.id }
                            showCheckoutDialog = true
                        },
                        enabled = cartItems.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (cartItems.isNotEmpty()) AppColor.Green else Color.Gray
                        ),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.height(42.dp)
                    ) {
                        Text(
                            fontFamily = PoppinsFont,
                            text = "Checkout",
                            color = Color.White,
                            fontSize = 14.sp,
                        )
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
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
fun CafeGroupCard(
    items: List<Cart>,
    onNavigateToDetail: (String) -> Unit,
    onDeleteFoodByCafe: (String) -> Unit,
    onMinus: (String, Int) -> Unit,
    onPlus: (String, Int) -> Unit,
) {
    Column {

        Row {
            Text(items.first().cafeName)

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                modifier = Modifier.clickable {
                    onDeleteFoodByCafe(items.first().cafeId)
                }
            )
        }

        items.forEach { item ->
            CartItemRow(
                item = item,
                onFoodClick = { onNavigateToDetail(item.foodId) },
                onMinusClick = { onMinus(item.id, item.quantity) },
                onPlusClick = { onPlus(item.id, item.quantity) }
            )
        }
    }
}


@Composable
fun CartItemRow(
    item: Cart,
    onFoodClick: () -> Unit,
    onMinusClick: () -> Unit,
    onPlusClick: () -> Unit,
) {

    val isPreview = LocalInspectionMode.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.Top
    ) {

        if (isPreview) {
            Image(
                painter = painterResource(R.drawable.image_burger),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            AsyncImage(
                model = item.image,
                placeholder = painterResource(R.drawable.image_burger),
                error = painterResource(R.drawable.image_burger),
                contentDescription = item.name,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
        }

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
                    text = item.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clickable {
                            onFoodClick()
                        }
                )
                StatusStatItem(FoodStatus.READY)
            }

            Text(
                fontFamily = PoppinsFont,
                text = "Notes: ${item.notes}",
                color = Color.DarkGray,
                fontSize = 12.sp
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = item.variants.joinToString(", ") { it.optionName },
                fontSize = 11.sp,
                color = Color.Gray
            )

            Spacer(Modifier.height(2.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    fontFamily = PoppinsFont,
                    text = item.price.toRupiah(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )

                QuantityCounter(
                    quantity = item.quantity,
                    onMinusClick = { onMinusClick() },
                    onPlusClick = { onPlusClick() }
                )
            }
        }
    }
}

@Composable
fun QuantityCounter(
    quantity: Int,
    onMinusClick: () -> Unit,
    onPlusClick: () -> Unit,
) {
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
                    modifier = Modifier
                        .size(16.dp)
                        .clickable {
                            onMinusClick()
                        }
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = null,
                    tint = AppColor.Gray,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable {
                            onMinusClick()
                        }
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
                modifier = Modifier
                    .size(18.dp)
                    .clickable {
                        onPlusClick()
                    }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumCheckoutSheet(
    total: BigDecimal,
    onDismiss: () -> Unit,
    onConfirm: (PaymentMethod) -> Unit
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
                text = total.toRupiah(),
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
                    onClick = { onConfirm(paymentMethod) },
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
    isLoading: Boolean,
    isError: Boolean,
    onDismiss: () -> Unit,
    onSuccess: (String) -> Unit
) {

    var pin by remember { mutableStateOf(value = EMPTY_STRING) }
    var isSubmitted by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(Unit) {
        pin = EMPTY_STRING
        isSubmitted = false
    }

    LaunchedEffect(isError) {
        if (isError) {
            pin = EMPTY_STRING
            isSubmitted = false
        }
    }

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
                isLoading = isLoading,
                onNumberClick = { number ->
                    if (pin.length < 6) {
                        pin += number

                        if (pin.length == 6 && !isLoading && !isSubmitted) {
                            isSubmitted = true
                            onSuccess(pin)
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
    isLoading: Boolean,
    onNumberClick: (String) -> Unit,
    onDeleteClick: () -> Unit
) {

    val buttons = listOf(
        "1", "2", "3",
        "4", "5", "6",
        "7", "8", "9",
        EMPTY_STRING, "0", "del"
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
                            .clickable(enabled = key.isNotEmpty() && !isLoading) {
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

                            EMPTY_STRING -> Spacer(modifier = Modifier.size(size = size))

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

@Composable
fun EmptyCartView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = null,
            tint = AppColor.Gray,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Keranjang kamu kosong 😢",
            fontFamily = PoppinsFont,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Yuk tambah menu dulu",
            fontFamily = PoppinsFont,
            fontSize = 13.sp,
            color = AppColor.Gray
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun CartScreenPreview() {

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .padding(bottom = padding.calculateBottomPadding())
                .fillMaxSize()
                .background(Color.White)
        ) {
            CartScreen(
                state = CartUiState(
                    cartItems = LoadState.Success(DataUiMock.cart()),
                    sessionToken = LoadState.Success(SessionToken("")),
                ),
                event = {}
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
                total = DataUiMock.cart().first().price,
                onDismiss = {},
                onConfirm = {}
            )
        }
    }
}