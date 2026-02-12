/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: CartScreen.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 09.14
 */

package com.mtv.app.shopme.feature.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.mtv.app.shopme.feature.contract.HomeDataListener
import com.mtv.app.shopme.feature.contract.HomeEventListener
import com.mtv.app.shopme.feature.contract.HomeNavigationListener
import com.mtv.app.shopme.feature.contract.HomeStateListener
import com.mtv.app.shopme.nav.BottomNavigationBar

@Composable
fun CartScreen(
    uiState: CartStateListener,
    uiData: CartDataListener,
    uiEvent: CartEventListener,
    uiNavigation: CartNavigationListener
) {
    Column(
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
    ) {
        CartHeader()

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = "${cartItems.size} Items",
            fontFamily = PoppinsFont,
            modifier = Modifier.padding(start = 20.dp, bottom = 12.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            items(cartItems) { item ->
                CartItemRow(item)

                HorizontalDivider(
                    color = Color.LightGray.copy(alpha = 0.3f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
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
                        text = "$40.99",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(AppColor.Orange),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .height(42.dp)
                ) {
                    Text(
                        fontFamily = PoppinsFont,
                        text = "Continue to checkout",
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
            .height(64.dp)
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            text = "My Cart",
            color = Color.Black,
            fontFamily = PoppinsFont,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Center),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun CartItemRow(item: CartItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
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
                Text(
                    fontFamily = PoppinsFont,
                    text = item.weight,
                    color = AppColor.Gray,
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.height(6.dp))

            Text(
                fontFamily = PoppinsFont,
                text = "Quantity: ${item.quantity} large pieces",
                color = AppColor.Gray,
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
        title = "Burger",
        weight = "250g",
        price = 9.99,
        image = R.drawable.image_burger,
        quantity = 2
    ),
    CartItem(
        title = "Pepperoni pizza",
        weight = "300g",
        price = 10.99,
        image = R.drawable.image_pizza,
        quantity = 1
    ),
    CartItem(
        title = "Simple Margarita Flatbread",
        weight = "300g",
        price = 9.99,
        image = R.drawable.image_platbread,
        quantity = 3
    ),
    CartItem(
        title = "Cheeseburger",
        weight = "250g",
        price = 9.99,
        image = R.drawable.image_cheese_burger,
        quantity = 2
    ),
    CartItem(
        title = "Burger",
        weight = "250g",
        price = 9.99,
        image = R.drawable.image_burger,
        quantity = 2
    ),
    CartItem(
        title = "Pepperoni pizza",
        weight = "300g",
        price = 10.99,
        image = R.drawable.image_pizza,
        quantity = 1
    ),
    CartItem(
        title = "Simple Margarita Flatbread",
        weight = "300g",
        price = 9.99,
        image = R.drawable.image_platbread,
        quantity = 3
    ),
    CartItem(
        title = "Cheeseburger",
        weight = "250g",
        price = 9.99,
        image = R.drawable.image_cheese_burger,
        quantity = 2
    )
)

data class CartItem(
    val title: String,
    val weight: String,
    val price: Double,
    val image: Int,
    val quantity: Int
)

@Preview(showBackground = true, device = Devices.PIXEL_4)
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
