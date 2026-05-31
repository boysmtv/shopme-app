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
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.ContentErrorState
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.common.SmartImage
import com.mtv.app.shopme.common.navbar.customer.CustomerBottomNavigationBar
import com.mtv.app.shopme.common.toRupiah
import com.mtv.app.shopme.data.mock.DataUiMock
import com.mtv.app.shopme.domain.model.Cart
import com.mtv.app.shopme.domain.model.FoodStatus
import com.mtv.app.shopme.domain.model.PaymentMethod
import com.mtv.app.shopme.feature.customer.contract.CartEffect
import com.mtv.app.shopme.feature.customer.contract.CartEvent
import com.mtv.app.shopme.feature.customer.contract.CartUiState
import com.mtv.app.shopme.feature.customer.presentation.CartViewModel
import com.mtv.app.shopme.feature.customer.ui.shimmer.ShimmerCartScreen
import com.mtv.app.shopme.feature.customer.utils.StatusStatItem
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.uicomponent.core.component.loading.LoadingV2
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING
import java.math.BigDecimal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CartScreen(
    state: CartUiState,
    effectFlow: Flow<CartEffect>,
    event: (CartEvent) -> Unit,
    onNavigateToDetail: (String) -> Unit = {},
    onNavigateToOrder: () -> Unit = {},
    onNavigateToEditProfile: () -> Unit = {}
) {
    val screenState = rememberCartScreenState()
    val snackbarHostState = remember { SnackbarHostState() }

    val cartItems = state.cartItems.dataOrEmpty()
    val groupedByCafe = cartItems.groupBy { it.cafeId }
    val grandTotal = cartItems.grandTotal()

    CartEffectHandler(
        effectFlow = effectFlow,
        screenState = screenState,
        snackbarHostState = snackbarHostState,
        onNavigateToOrder = onNavigateToOrder,
        onNavigateToEditProfile = onNavigateToEditProfile
    )

    when (state.cartItems) {
        is LoadState.Loading -> {
            ShimmerCartScreen()
            return
        }

        is LoadState.Error -> {
            ContentErrorState(
                title = "Gagal memuat keranjang",
                message = state.cartItems.error.message,
                actionLabel = "Muat ulang",
                onRetry = { event(CartEvent.Load) }
            )
            return
        }

        else -> Unit
    }

    CartMainContent(
        state = state,
        event = event,
        cartItems = cartItems,
        groupedByCafe = groupedByCafe,
        grandTotal = grandTotal,
        screenState = screenState,
        snackbarHostState = snackbarHostState,
        onNavigateToDetail = onNavigateToDetail
    )

    CartDialogs(
        state = state,
        event = event,
        grandTotal = grandTotal,
        screenState = screenState,
        onNavigateToOrder = onNavigateToOrder
    )
}

@Composable
private fun rememberCartScreenState(): CartScreenState {
    return remember { CartScreenState() }
}

@Stable
private class CartScreenState {
    var showCheckoutSheet by mutableStateOf(false)
    var showPinSheet by mutableStateOf(false)
    var showSuccessDialog by mutableStateOf(false)
    var selectedPaymentMethod by mutableStateOf(PaymentMethod.CASH)

    fun closeCheckout() {
        showCheckoutSheet = false
        showPinSheet = false
        selectedPaymentMethod = PaymentMethod.CASH
    }
}

@Composable
private fun CartEffectHandler(
    effectFlow: Flow<CartEffect>,
    screenState: CartScreenState,
    snackbarHostState: SnackbarHostState,
    onNavigateToOrder: () -> Unit,
    onNavigateToEditProfile: () -> Unit
) {
    LaunchedEffect(effectFlow) {
        effectFlow.collectLatest { effect ->
            when (effect) {
                is CartEffect.OpenPinSheet -> {
                    screenState.showPinSheet = true
                }

                is CartEffect.ShowSuccessDialog -> {
                    screenState.closeCheckout()
                    screenState.showSuccessDialog = true
                }

                is CartEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }

                is CartEffect.NavigateToOrder -> {
                    onNavigateToOrder()
                }

                is CartEffect.NavigateToEditProfile -> {
                    onNavigateToEditProfile()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CartMainContent(
    state: CartUiState,
    event: (CartEvent) -> Unit,
    cartItems: List<Cart>,
    groupedByCafe: Map<String, List<Cart>>,
    grandTotal: BigDecimal,
    screenState: CartScreenState,
    snackbarHostState: SnackbarHostState,
    onNavigateToDetail: (String) -> Unit
) {
    val scope = rememberCoroutineScope()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = { event(CartEvent.Load) }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColor.White)
                .padding(horizontal = 16.dp)
                .statusBarsPadding()
        ) {
            CartHeader()

            Spacer(modifier = Modifier.height(16.dp))

            CartTopActionBar(
                totalItems = cartItems.size,
                onClearCart = { event(CartEvent.ClearCart) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CartListContent(
                cartItems = cartItems,
                groupedByCafe = groupedByCafe,
                event = event,
                onNavigateToDetail = onNavigateToDetail
            )

            Spacer(modifier = Modifier.height(16.dp))

            CartSummaryBar(
                total = grandTotal,
                isCheckoutEnabled = cartItems.isNotEmpty(),
                onCheckoutClick = {
                    if (cartItems.isEmpty()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Keranjang masih kosong")
                        }
                        return@CartSummaryBar
                    }

                    screenState.showCheckoutSheet = true
                }
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )

        PullRefreshIndicator(
            refreshing = state.isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
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
private fun CartTopActionBar(
    totalItems: Int,
    onClearCart: () -> Unit
) {
    val hasItems = totalItems > 0

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Total $totalItems Items",
            fontFamily = PoppinsFont,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = "Clear Cart",
            fontFamily = PoppinsFont,
            fontSize = 14.sp,
            color = if (hasItems) AppColor.Green else AppColor.Gray,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable(enabled = hasItems) {
                onClearCart()
            }
        )
    }
}

@Composable
private fun ColumnScope.CartListContent(
    cartItems: List<Cart>,
    groupedByCafe: Map<String, List<Cart>>,
    event: (CartEvent) -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    if (cartItems.isEmpty()) {
        EmptyCartView(
            modifier = Modifier.weight(1f)
        )
        return
    }

    LazyColumn(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        groupedByCafe.forEach { (_, items) ->
            item {
                CafeGroupCard(
                    event = event,
                    itemResponses = items,
                    onNavigateToDetail = onNavigateToDetail,
                    onDeleteFoodByCafe = {
                        event(CartEvent.ClearCartByCafe(it))
                    }
                )
            }
        }
    }
}

@Composable
private fun CartSummaryBar(
    total: BigDecimal,
    isCheckoutEnabled: Boolean,
    onCheckoutClick: () -> Unit
) {
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
                    text = "Total shopping cart",
                    fontFamily = PoppinsFont,
                    color = AppColor.Gray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = total.toRupiah(),
                    fontFamily = PoppinsFont,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = onCheckoutClick,
                enabled = isCheckoutEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCheckoutEnabled) AppColor.Green else Color.Gray
                ),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.height(42.dp)
            ) {
                Text(
                    text = "Checkout",
                    fontFamily = PoppinsFont,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun CafeGroupCard(
    event: (CartEvent) -> Unit,
    itemResponses: List<Cart>,
    onNavigateToDetail: (foodId: String) -> Unit,
    onDeleteFoodByCafe: (cafeId: String) -> Unit
) {
    val cafeSubtotal = itemResponses.grandTotal()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = AppColor.WhiteSoft,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
    ) {
        CafeGroupHeader(
            cafeName = itemResponses.first().cafeName,
            onDeleteClick = {
                onDeleteFoodByCafe(itemResponses.first().cafeId)
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(
            color = AppColor.Gray,
            modifier = Modifier.height(1.dp)
        )

        itemResponses.forEachIndexed { index, item ->
            CartItemRow(
                itemResponse = item,
                onFoodClick = { onNavigateToDetail(item.foodId) },
                onMinusClick = {
                    val newQuantity = if (item.quantity == 1) 0 else item.quantity - 1

                    event(
                        CartEvent.ChangeQuantity(
                            cartId = item.id,
                            quantity = newQuantity
                        )
                    )
                },
                onPlusClick = {
                    event(
                        CartEvent.ChangeQuantity(
                            cartId = item.id,
                            quantity = item.quantity + 1
                        )
                    )
                }
            )

            if (index != itemResponses.lastIndex) {
                HorizontalDivider(
                    color = Color.LightGray.copy(alpha = 0.3f),
                    thickness = 1.dp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        CafeGroupFooter(
            totalItems = itemResponses.size,
            subtotal = cafeSubtotal
        )
    }
}

@Composable
private fun CafeGroupHeader(
    cafeName: String,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = cafeName,
                fontFamily = PoppinsFont,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = AppColor.Green,
            modifier = Modifier
                .size(24.dp)
                .clickable { onDeleteClick() }
        )
    }
}

@Composable
private fun CafeGroupFooter(
    totalItems: Int,
    subtotal: BigDecimal
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = AppColor.GreenSoft.copy(alpha = 0.9f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Total $totalItems items",
            fontFamily = PoppinsFont,
            fontSize = 13.sp,
            color = AppColor.Gray
        )

        Text(
            text = subtotal.toRupiah(),
            fontFamily = PoppinsFont,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CartItemRow(
    itemResponse: Cart,
    onFoodClick: () -> Unit,
    onMinusClick: () -> Unit,
    onPlusClick: () -> Unit
) {
    val isPreview = LocalInspectionMode.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        CartItemImage(
            isPreview = isPreview,
            image = itemResponse.image,
            name = itemResponse.name
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            CartItemTitleRow(
                name = itemResponse.name,
                onFoodClick = onFoodClick
            )

            Text(
                text = "Notes: ${itemResponse.notes}",
                fontFamily = PoppinsFont,
                color = Color.DarkGray,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = itemResponse.variants.joinToString(", ") { it.optionName },
                fontSize = 11.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(2.dp))

            CartItemPriceRow(
                price = itemResponse.price,
                quantity = itemResponse.quantity,
                onMinusClick = onMinusClick,
                onPlusClick = onPlusClick
            )
        }
    }
}

@Composable
private fun CartItemImage(
    isPreview: Boolean,
    image: String,
    name: String
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
        SmartImage(
            model = image,
            placeholder = painterResource(R.drawable.image_burger),
            error = painterResource(R.drawable.image_burger),
            contentDescription = name,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun CartItemTitleRow(
    name: String,
    onFoodClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            modifier = Modifier.clickable {
                onFoodClick()
            }
        )

        StatusStatItem(FoodStatus.READY)
    }
}

@Composable
private fun CartItemPriceRow(
    price: BigDecimal,
    quantity: Int,
    onMinusClick: () -> Unit,
    onPlusClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = price.toRupiah(),
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        QuantityCounter(
            quantity = quantity,
            onMinusClick = onMinusClick,
            onPlusClick = onPlusClick
        )
    }
}

@Composable
fun QuantityCounter(
    quantity: Int,
    onMinusClick: () -> Unit,
    onPlusClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        QuantityMinusButton(
            quantity = quantity,
            onClick = onMinusClick
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = quantity.toString(),
            fontFamily = PoppinsFont,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(12.dp))

        QuantityPlusButton(
            onClick = onPlusClick
        )
    }
}

@Composable
private fun QuantityMinusButton(
    quantity: Int,
    onClick: () -> Unit
) {
    val isDeleteMode = quantity == 1

    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(
                if (isDeleteMode) {
                    Color.Red.copy(alpha = 0.15f)
                } else {
                    Color.LightGray.copy(alpha = 0.3f)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isDeleteMode) Icons.Default.Delete else Icons.Default.Remove,
            contentDescription = null,
            tint = if (isDeleteMode) Color.Red else AppColor.Gray,
            modifier = Modifier
                .size(if (isDeleteMode) 16.dp else 18.dp)
                .clickable { onClick() }
        )
    }
}

@Composable
private fun QuantityPlusButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(AppColor.Green),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(18.dp)
                .clickable { onClick() }
        )
    }
}

@Composable
fun EmptyCartView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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
            text = "Tidak ada keranjang",
            fontFamily = PoppinsFont,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun CartDialogs(
    state: CartUiState,
    event: (CartEvent) -> Unit,
    grandTotal: BigDecimal,
    screenState: CartScreenState,
    onNavigateToOrder: () -> Unit
) {
    if (screenState.showCheckoutSheet) {
        PremiumCheckoutSheet(
            total = grandTotal,
            onDismiss = {
                screenState.showCheckoutSheet = false
                event(CartEvent.CheckoutCancelled)
            },
            onConfirm = { paymentMethod ->
                screenState.selectedPaymentMethod = paymentMethod
                screenState.showCheckoutSheet = false

                event(
                    CartEvent.CheckoutClicked(
                        cartIds = state.cartItems.dataOrEmpty().map { it.id },
                        payment = paymentMethod
                    )
                )
            }
        )
    }

    if (screenState.showPinSheet) {
        PinVerificationSheet(
            isLoading = state.isCheckoutLoading,
            onDismiss = {
                screenState.showPinSheet = false
                event(CartEvent.CheckoutCancelled)
            },
            onSuccess = { pin ->
                screenState.showPinSheet = false

                event(
                    CartEvent.PinSubmitted(
                        pin = pin
                    )
                )
            }
        )
    }

    if (state.isCheckoutLoading) {
        LoadingOverlay()
    }

    if (screenState.showSuccessDialog) {
        OrderSuccessDialog(
            onConfirm = {
                screenState.showSuccessDialog = false
                onNavigateToOrder()
            }
        )
    }
}

@Composable
private fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        LoadingV2()
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
        dragHandle = { SheetDragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CheckoutIcon()

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

            PaymentMethodSelector(
                selectedPaymentMethod = paymentMethod,
                onPaymentSelected = { paymentMethod = it }
            )

            if (paymentMethod == PaymentMethod.TRANSFER) {
                Spacer(modifier = Modifier.height(12.dp))
                TransferInformationBox()
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

            CheckoutActionButtons(
                onCancel = onDismiss,
                onConfirm = { onConfirm(paymentMethod) }
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun SheetDragHandle() {
    Box(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .size(width = 42.dp, height = 4.dp)
            .clip(RoundedCornerShape(50))
            .background(AppColor.Gray.copy(alpha = 0.35f))
    )
}

@Composable
private fun CheckoutIcon() {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(AppColor.White)
            .border(1.dp, AppColor.Green.copy(alpha = 0.18f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = null,
            tint = AppColor.Green,
            modifier = Modifier.size(36.dp)
        )
    }
}

@Composable
private fun PaymentMethodSelector(
    selectedPaymentMethod: PaymentMethod,
    onPaymentSelected: (PaymentMethod) -> Unit
) {
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
            selected = selectedPaymentMethod == PaymentMethod.CASH,
            onClick = { onPaymentSelected(PaymentMethod.CASH) }
        )

        PaymentPill(
            title = "Transfer",
            selected = selectedPaymentMethod == PaymentMethod.TRANSFER,
            onClick = { onPaymentSelected(PaymentMethod.TRANSFER) }
        )
    }
}

@Composable
private fun TransferInformationBox() {
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
            text = "Setelah transfer, buka halaman Order lalu tekan Konfirmasi Transfer.",
            fontFamily = PoppinsFont,
            fontSize = 13.sp,
            color = Color(0xFF444444),
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CheckoutActionButtons(
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Batal",
                fontFamily = PoppinsFont
            )
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
                if (selected) {
                    AppColor.Green.copy(alpha = 0.12f)
                } else {
                    AppColor.White
                }
            )
            .border(
                width = 1.dp,
                color = if (selected) AppColor.Green else AppColor.Gray.copy(alpha = 0.25f),
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
    onDismiss: () -> Unit,
    onSuccess: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var pin by remember { mutableStateOf(EMPTY_STRING) }
    var isSubmitted by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        pin = EMPTY_STRING
        isSubmitted = false
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AppColor.GreenSoft,
        shape = RoundedCornerShape(
            topStart = 28.dp,
            topEnd = 28.dp
        ),
        dragHandle = { SheetDragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Masukkan PIN",
                fontFamily = PoppinsFont,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Untuk konfirmasi pembayaran",
                fontFamily = PoppinsFont,
                fontSize = 14.sp,
                color = AppColor.Gray.copy(alpha = 0.75f)
            )

            Spacer(modifier = Modifier.height(28.dp))

            PinIndicator(pinLength = pin.length)

            Spacer(modifier = Modifier.height(28.dp))

            if (isLoading) {
                LoadingV2()
                Spacer(modifier = Modifier.height(20.dp))
            } else {
                PinKeypad(
                    isLoading = false,
                    onNumberClick = { number ->
                        if (pin.length < PIN_LENGTH) {
                            pin += number

                            if (pin.length == PIN_LENGTH && !isSubmitted) {
                                isSubmitted = true
                                onSuccess(pin)
                            }
                        }
                    },
                    onDeleteClick = {
                        if (pin.isNotEmpty()) {
                            pin = pin.dropLast(1)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PinIndicator(
    pinLength: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(PIN_LENGTH) { index ->
            val filled = index < pinLength

            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
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
}

@Composable
fun PinKeypad(
    isLoading: Boolean,
    onNumberClick: (String) -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PIN_BUTTONS.chunked(3).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier.wrapContentWidth()
            ) {
                row.forEach { key ->
                    PinKeypadButton(
                        key = key,
                        isLoading = isLoading,
                        onNumberClick = onNumberClick,
                        onDeleteClick = onDeleteClick
                    )
                }
            }
        }
    }
}

@Composable
private fun PinKeypadButton(
    key: String,
    isLoading: Boolean,
    onNumberClick: (String) -> Unit,
    onDeleteClick: () -> Unit
) {
    val buttonSize = 84.dp

    Box(
        modifier = Modifier
            .size(buttonSize)
            .clip(CircleShape)
            .background(AppColor.White)
            .border(
                width = 1.dp,
                color = AppColor.Green.copy(alpha = 0.1f),
                shape = CircleShape
            )
            .clickable(enabled = key.isNotEmpty() && !isLoading) {
                when (key) {
                    KEY_DELETE -> onDeleteClick()
                    else -> onNumberClick(key)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        when (key) {
            KEY_DELETE -> {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = AppColor.Gray.copy(alpha = 0.8f),
                    modifier = Modifier.size(20.dp)
                )
            }

            EMPTY_STRING -> {
                Spacer(modifier = Modifier.size(buttonSize))
            }

            else -> {
                Text(
                    text = key,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PoppinsFont
                )
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
                SuccessIcon()

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
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center
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
private fun SuccessIcon() {
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
}

private const val PIN_LENGTH = 6
private const val KEY_DELETE = "del"

private val PIN_BUTTONS = listOf(
    "1", "2", "3",
    "4", "5", "6",
    "7", "8", "9",
    EMPTY_STRING, "0", KEY_DELETE
)

private fun LoadState<List<Cart>>.dataOrEmpty(): List<Cart> {
    return when (this) {
        is LoadState.Success -> data
        else -> emptyList()
    }
}

private fun List<Cart>.grandTotal(): BigDecimal {
    return fold(BigDecimal.ZERO) { acc, item ->
        val variantTotal = item.variants.fold(BigDecimal.ZERO) { sum, variant ->
            sum + variant.price
        }

        val itemTotal = item.price + variantTotal
        acc + (itemTotal * item.quantity.toBigDecimal())
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
                state = CartUiState(
                    cartItems = LoadState.Success(DataUiMock.cart())
                ),
                effectFlow = emptyFlow(),
                event = {}
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun EmptyCartViewPreview() {
    EmptyCartView()
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
                .fillMaxHeight(0.60f)
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(AppColor.GreenSoft)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Masukkan PIN",
                    fontFamily = PoppinsFont,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Untuk konfirmasi pembayaran",
                    fontFamily = PoppinsFont,
                    fontSize = 14.sp,
                    color = AppColor.Gray.copy(alpha = 0.75f)
                )

                Spacer(modifier = Modifier.height(28.dp))

                PinIndicator(pinLength = 2)

                Spacer(modifier = Modifier.height(28.dp))

                PinKeypad(
                    isLoading = false,
                    onNumberClick = {},
                    onDeleteClick = {}
                )
            }
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