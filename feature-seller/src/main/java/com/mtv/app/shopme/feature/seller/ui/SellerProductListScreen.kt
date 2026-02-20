/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductListScreen.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.54
 */

package com.mtv.app.shopme.feature.seller.ui

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.feature.seller.contract.SellerProductListDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerProductListEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerProductListNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerProductListStateListener
import com.mtv.app.shopme.feature.seller.model.SellerProduct
import com.mtv.app.shopme.nav.SellerBottomNavigationBar

@Composable
fun SellerProductListScreen(
    uiState: SellerProductListStateListener,
    uiData: SellerProductListDataListener,
    uiEvent: SellerProductListEventListener,
    uiNavigation: SellerProductListNavigationListener
) {

    val totalProduct = uiState.productList.size
    val totalStock = uiState.productList.sumOf { it.stock }
    val lowStockCount = uiState.productList.count { it.stock <= 5 }

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        containerColor = AppColor.WhiteSoft,
        floatingActionButton = {
            Button(
                onClick = { uiNavigation.onNavigateToAdd() },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(AppColor.Blue),
                modifier = Modifier.shadow(8.dp, RoundedCornerShape(50))
            ) {
                Text("+ Add Product", color = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            ProductHeader(
                totalProduct = totalProduct,
                totalStock = totalStock,
                lowStock = lowStockCount,
                onBack = uiNavigation.onBack
            )

            Spacer(Modifier.height(16.dp))

            if (uiState.productList.isEmpty()) {
                EmptyProductState()
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    items(uiState.productList) { product ->
                        ModernProductItem(
                            product = product,
                            onEdit = { uiNavigation.onNavigateToEdit() },
                            onDelete = { uiEvent.onDeleteProduct() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductHeader(
    totalProduct: Int,
    totalStock: Int,
    lowStock: Int,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColor.Blue)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Product Management",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        // 🔹 Stats Container (Solid White Card)
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                CleanStatItem("Products", totalProduct)
                CleanStatItem("Total Stock", totalStock)
                CleanStatItem(
                    "Low Stock",
                    lowStock,
                    isWarning = lowStock > 0
                )
            }
        }
    }
}

@Composable
fun CleanStatItem(
    label: String,
    value: Int,
    isWarning: Boolean = false
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = value.toString(),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = if (isWarning) Color.Red else AppColor.Blue
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun ModernProductItem(
    product: SellerProduct,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    val isLowStock = product.stock <= 5
    val placeholderRes = product.id.hashCode() % 8

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(
                        id = when (placeholderRes) {
                            0 -> com.mtv.app.shopme.common.R.drawable.image_burger
                            1 -> com.mtv.app.shopme.common.R.drawable.image_pizza
                            2 -> com.mtv.app.shopme.common.R.drawable.image_platbread
                            3 -> com.mtv.app.shopme.common.R.drawable.image_cheese_burger
                            4 -> com.mtv.app.shopme.common.R.drawable.image_bakso
                            5 -> com.mtv.app.shopme.common.R.drawable.image_pempek
                            6 -> com.mtv.app.shopme.common.R.drawable.image_padang
                            7 -> com.mtv.app.shopme.common.R.drawable.image_sate
                            else -> com.mtv.app.shopme.common.R.drawable.image_burger
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.width(16.dp))

            // 🔹 Product Info
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = product.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = product.price,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColor.Blue
                )

                Spacer(Modifier.height(8.dp))

                ModernStockBadge(product.stock, isLowStock)
            }

            // 🔹 Action Buttons
            Column(
                horizontalAlignment = Alignment.End
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.DeleteOutline,
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
            }
        }
    }
}


@Composable
fun ModernStockBadge(stock: Int, isLow: Boolean) {

    val bg =
        if (isLow) Color(0xFFFFEBEE)
        else AppColor.Blue.copy(alpha = 0.1f)

    val textColor =
        if (isLow) Color(0xFFD32F2F)
        else AppColor.Blue

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            if (isLow) "Low Stock • $stock"
            else "Stock • $stock",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}


@Composable
fun EmptyProductState(
    onAddClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(AppColor.Blue.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "📦",
                fontSize = 42.sp
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "No Products Yet",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Start selling by adding your first product.\nManage stock, pricing and availability easily.",
            fontSize = 13.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        if (onAddClick != null) {
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onAddClick,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(AppColor.Blue)
            ) {
                Text("Add Product", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SellerProductListScreenPreview() {
    val navController = rememberNavController()

    val mockState = SellerProductListStateListener(
        productList = listOf(
            SellerProduct("1", "Double Beef Burger", "Rp 60.000", 10),
            SellerProduct("2", "Cheese Pizza", "Rp 75.000", 5),
            SellerProduct("3", "Padang Rice Set", "Rp 50.000", 12),
            SellerProduct("4", "Padang Rice Set", "Rp 50.000", 12),
            SellerProduct("5", "Padang Rice Set", "Rp 50.000", 12),
            SellerProduct("6", "Padang Rice Set", "Rp 50.000", 12),
        )
    )

    Scaffold(
        bottomBar = {
            SellerBottomNavigationBar(navController)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(bottom = padding.calculateBottomPadding())
                .fillMaxSize()
                .background(AppColor.White)
        ) {
            SellerProductListScreen(
                uiState = mockState,
                uiData = SellerProductListDataListener(),
                uiEvent = SellerProductListEventListener({}),
                uiNavigation = SellerProductListNavigationListener({}, {}, {})
            )
        }
    }
}
