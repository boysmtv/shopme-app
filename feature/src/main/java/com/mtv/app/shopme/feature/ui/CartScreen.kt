/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartScreen.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 09.14
 */

package com.mtv.app.shopme.feature.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.feature.contract.CartDataListener
import com.mtv.app.shopme.feature.contract.CartEventListener
import com.mtv.app.shopme.feature.contract.CartNavigationListener
import com.mtv.app.shopme.feature.contract.CartStateListener
import com.mtv.app.shopme.nav.BottomNavigationBar
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

@Composable
fun CartScreen(
    uiState: CartStateListener,
    uiData: CartDataListener,
    uiEvent: CartEventListener,
    uiNavigation: CartNavigationListener
) {
    var showCheckoutDialog by remember { mutableStateOf(false) }

    if (showCheckoutDialog) {
        CheckoutConfirmationDialog(
            total = grandTotal,
            onDismiss = { showCheckoutDialog = false },
            onConfirm = {
                showCheckoutDialog = false
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
                color = AppColor.Orange,
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
                    colors = ButtonDefaults.buttonColors(AppColor.Orange),
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
            ){
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = AppColor.Orange,
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
                tint = AppColor.Orange,
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
                    AppColor.LightOrange.copy(alpha = 0.7f),
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
            .background(AppColor.LightOrange.copy(alpha = 0.15f))
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
                .background(AppColor.Orange),
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

@Composable
fun CheckoutConfirmationDialog(
    total: Double,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = "Konfirmasi Pesanan",
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column {
                Text(
                    text = "Pesanan akan dikirim ke penjual dengan total harga:",
                    fontFamily = PoppinsFont,
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "$${"%.2f".format(total)}",
                    fontFamily = PoppinsFont,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColor.Orange
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Apakah kamu ingin melanjutkan?",
                    fontFamily = PoppinsFont,
                    fontSize = 14.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(AppColor.Orange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Ya, Lanjutkan",
                    fontFamily = PoppinsFont,
                    color = Color.White
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(Color.LightGray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Batal",
                    fontFamily = PoppinsFont,
                    color = Color.Black
                )
            }
        }
    )
}


@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun CartScreenPreview() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
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
