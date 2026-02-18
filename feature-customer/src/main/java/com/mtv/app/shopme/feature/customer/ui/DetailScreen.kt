/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: DetailScreen.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 08.58
 */

package com.mtv.app.shopme.feature.customer.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.feature.customer.contract.DetailDataListener
import com.mtv.app.shopme.feature.customer.contract.DetailEventListener
import com.mtv.app.shopme.feature.customer.contract.DetailNavigationListener
import com.mtv.app.shopme.feature.customer.contract.DetailStateListener

data class SimilarItem(
    val image: Int,
    val title: String,
    val price: Double
)

enum class OrderStatus {
    READY,
    PREORDER,
    JASTIP
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    uiState: DetailStateListener,
    uiData: DetailDataListener,
    uiEvent: DetailEventListener,
    uiNavigation: DetailNavigationListener
) {
    val similarItems = listOf(
        SimilarItem(R.drawable.image_cheese_burger, "Cheese Burger", 8.99),
        SimilarItem(R.drawable.image_burger, "Double Cheese", 10.49),
        SimilarItem(R.drawable.image_pizza, "Beef Burger", 9.29),
        SimilarItem(R.drawable.image_platbread, "Veggie Burger", 7.99)
    )

    var showSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            VariantBottomSheetContent(
                onAddToCart = {
                    uiNavigation.onAddToCart()
                },
                onClose = {
                    showSheet = false
                }
            )

        }
    }

    Scaffold(
        bottomBar = {
            AddToCartBar(
                onChatClick = {
                    uiNavigation.onChatClick()
                },
                onCartClick = {
                    showSheet = true
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
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
                .padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            item { DetailHeader() }
            item { Spacer(Modifier.height(16.dp)) }

            item { DetailImage() }
            item { Spacer(Modifier.height(16.dp)) }

            item { DetailTitle() }
            item { Spacer(Modifier.height(6.dp)) }

            item {
                DetailLocation(
                    onClickCafe = { uiNavigation.onClickCafe() }
                )
            }
            item { Spacer(Modifier.height(12.dp)) }

            item { DetailDescription() }
            item { Spacer(Modifier.height(20.dp)) }

            item { DetailStatsRow() }
            item { Spacer(Modifier.height(22.dp)) }

            item { SectionTitle("Bahan-bahan") }
            item { Spacer(Modifier.height(12.dp)) }

            item { IngredientsRow() }
            item { Spacer(Modifier.height(20.dp)) }

            item { SectionTitle("Menu lainnya") }
            item { Spacer(Modifier.height(12.dp)) }

            items(similarItems) { item ->
                SimilarItemRow(item.image, item.title, item.price)
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun AddToCartBar(
    onChatClick: () -> Unit,
    onCartClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { onChatClick() },
            colors = ButtonDefaults.buttonColors(Color(0xFF25D366)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                .height(56.dp)
                .weight(1f)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Chat,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = "Tanya",
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = PoppinsFont
            )
        }

        Spacer(Modifier.width(16.dp))

        Button(
            onClick = { onCartClick() },
            colors = ButtonDefaults.buttonColors(AppColor.Orange),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(end = 16.dp, top = 16.dp, bottom = 16.dp)
                .height(56.dp)
                .weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = "Keranjang",
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = PoppinsFont
            )
        }
    }
}

@Composable
fun DetailHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(12.dp),
            tint = Color.Black
        )

        Text(
            text = "Food Detail",
            fontFamily = PoppinsFont,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(12.dp),
            tint = Color.Red
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailImage() {
    val images = listOf(
        R.drawable.image_burger,
        R.drawable.image_pizza,
        R.drawable.image_platbread,
        R.drawable.image_cheese_burger,
        R.drawable.image_padang,
        R.drawable.image_sate,
        R.drawable.image_pempek,
    )

    val pagerState = rememberPagerState(
        pageCount = { images.size }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Image(
                painter = painterResource(images[page]),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(images.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (isSelected) 10.dp else 8.dp)
                        .background(
                            color = if (isSelected) Color.White else Color.LightGray,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
fun DetailTitle() {
    Text(
        text = "Double Beef Cheese Burger",
        fontSize = 24.sp,
        fontFamily = PoppinsFont,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun DetailLocation(
    onClickCafe: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = Modifier
                .clickable {
                    onClickCafe()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                tint = AppColor.Orange
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Mamah Al Cafe",
                color = Color.DarkGray,
                fontSize = 14.sp,
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = AppColor.Orange
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Puri Lestari - Blok H12/01",
            color = Color.DarkGray,
            fontSize = 14.sp,
            fontFamily = PoppinsFont
        )
    }
}

@Composable
fun DetailDescription() {
    Text(
        "This is the heart and soul of the burger This is the heart and soul of the burger This is the heart and soul of the burger...",
        color = AppColor.Gray,
        fontSize = 14.sp,
        fontFamily = PoppinsFont
    )
}

@Composable
fun DetailStatsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        StatusStatItem(OrderStatus.READY)

        StatItem(
            icon = Icons.Default.Inventory2,
            text = "Tersedia 20 Pcs"
        )

        StatItem(
            icon = Icons.Default.AccessTime,
            text = "20–25 Min",
        )
    }
}

@Composable
fun StatusStatItem(
    status: OrderStatus,
) {
    val (icon, color, text) = when (status) {
        OrderStatus.READY -> Triple(
            Icons.Default.CheckCircle,
            Color(0xFF4CAF50),
            "Ready"
        )

        OrderStatus.PREORDER -> Triple(
            Icons.Default.Schedule,
            Color(0xFFFF9800),
            "Pre-order"
        )

        OrderStatus.JASTIP -> Triple(
            Icons.Default.LocalShipping,
            Color(0xFF2196F3),
            "Jastip"
        )
    }

    Row(
        modifier = Modifier
            .background(
                color = color.copy(alpha = 0.12f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = color,
            fontFamily = PoppinsFont
        )
    }
}

@Composable
fun StatItem(
    icon: ImageVector,
    text: String
) {
    Row(
        modifier = Modifier
            .background(
                color = AppColor.Orange.copy(alpha = 0.12f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppColor.Orange,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = AppColor.Orange,
            fontFamily = PoppinsFont
        )
    }
}


@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontFamily = PoppinsFont
    )
}


@Composable
fun IngredientsRow() {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(7) {
            IngredientItem(R.drawable.ic_location_white)
        }
    }
}

@Composable
fun IngredientItem(image: Int) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(AppColor.White),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = AppColor.Orange
        )
    }
}

@Composable
fun SimilarItemRow(image: Int, title: String, price: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color.Black.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontFamily = PoppinsFont
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "$${price}",
                color = AppColor.Orange,
                fontSize = 14.sp,
                fontFamily = PoppinsFont
            )
        }

        Box(
            contentAlignment = Alignment.Center
        ) {
            StatusStatItem(OrderStatus.PREORDER)
        }

        Spacer(Modifier.width(16.dp))

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(AppColor.Orange),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = AppColor.White
            )
        }
    }
}

@Composable
fun VariantBottomSheetContent(
    onAddToCart: () -> Unit,
    onClose: () -> Unit
) {
    var quantity by remember { mutableIntStateOf(1) }
    var selectedSize by remember { mutableStateOf("Medium") }
    var selectedSpicy by remember { mutableStateOf("Normal") }
    var extraCheese by remember { mutableStateOf(false) }
    var doublePatty by remember { mutableStateOf(true) }

    val basePrice = 35000

    val sizePrice = when (selectedSize) {
        "Medium" -> 0
        "Large" -> 5000
        else -> 0
    }

    val spicyPrice = when (selectedSpicy) {
        "Normal" -> 0
        "Sedang" -> 2000
        "Pedas 🔥" -> 3000
        else -> 0
    }

    val toppingPrice =
        (if (extraCheese) 5000 else 0) +
                (if (doublePatty) 12000 else 0)

    val singlePrice = basePrice + sizePrice + spicyPrice + toppingPrice
    val totalPrice = singlePrice * quantity

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Pilih Varian",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PoppinsFont
            )

            IconButton(
                onClick = { onClose() }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = AppColor.Orange
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Ukuran",
            fontWeight = FontWeight.SemiBold,
            fontFamily = PoppinsFont
        )
        Spacer(Modifier.height(8.dp))

        VariantSelector(
            options = listOf("Medium", "Large"),
            selected = selectedSize,
            onSelect = { selectedSize = it }
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Level Pedas",
            fontWeight = FontWeight.SemiBold,
            fontFamily = PoppinsFont
        )
        Spacer(Modifier.height(8.dp))

        VariantSelector(
            options = listOf("Normal", "Sedang", "Pedas 🔥"),
            selected = selectedSpicy,
            onSelect = { selectedSpicy = it }
        )

        Spacer(Modifier.height(16.dp))
        HorizontalDivider(color = AppColor.LightOrange, modifier = Modifier.height(1.dp))

        Spacer(Modifier.height(8.dp))
        Text(
            text = "Tambahan",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = PoppinsFont
        )

        Spacer(Modifier.height(16.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AddOnItem(
                title = "Extra Cheese",
                price = 5000,
                selected = extraCheese,
                onClick = { extraCheese = !extraCheese }
            )

            AddOnItem(
                title = "Double Patty",
                price = 12000,
                selected = doublePatty,
                onClick = { doublePatty = !doublePatty }
            )
        }

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            PriceRow("Harga", basePrice)
            if (sizePrice > 0) PriceRow("Ukuran ($selectedSize)", sizePrice)
            if (spicyPrice > 0) PriceRow("Level ($selectedSpicy)", spicyPrice)
            if (extraCheese) PriceRow("Extra Cheese", 5000)
            if (doublePatty) PriceRow("Double Patty", 12000)
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(AppColor.Orange.copy(alpha = 0.08f))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Jumlah",
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                            if (quantity > 1) AppColor.Orange else Color.Gray.copy(alpha = 0.3f)
                        )
                        .clickable {
                            if (quantity > 1) quantity--
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "-",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.width(12.dp))

                Text(
                    text = quantity.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PoppinsFont
                )

                Spacer(Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(AppColor.Orange)
                        .clickable {
                            quantity++
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Total: Rp $totalPrice",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppColor.Orange,
            fontFamily = PoppinsFont,
            textAlign = TextAlign.End
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { onAddToCart() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(AppColor.Orange),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = "Tambah ($quantity) • Rp $totalPrice",
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = PoppinsFont,
            )
        }
    }
}

@Composable
fun PriceRow(label: String, price: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontFamily = PoppinsFont,
            fontSize = 14.sp
        )

        Text(
            text = "+ Rp $price",
            fontFamily = PoppinsFont,
            fontSize = 14.sp
        )
    }

    Spacer(Modifier.height(6.dp))
}


@Composable
fun AddOnItem(
    title: String,
    price: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    val borderColor =
        if (selected) AppColor.Orange else Color.Gray.copy(alpha = 0.3f)

    val backgroundColor =
        if (selected) AppColor.Orange.copy(alpha = 0.08f) else Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "+ Rp $price",
                fontSize = 13.sp,
                color = AppColor.Orange
            )
        }

        if (selected) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = AppColor.Orange
            )
        }
    }
}


@Composable
fun VariantSelector(
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option ->
            val isSelected = option == selected

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isSelected) AppColor.Orange
                        else AppColor.Orange.copy(alpha = 0.1f)
                    )
                    .clickable { onSelect(option) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    option,
                    color = if (isSelected) Color.White else AppColor.Orange,
                    fontFamily = PoppinsFont
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun DetailScreenPreview() {
    DetailScreen(
        uiState = DetailStateListener(),
        uiData = DetailDataListener(),
        uiEvent = DetailEventListener(),
        uiNavigation = DetailNavigationListener()
    )
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun VariantBottomSheetMockPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Gray),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(Color.White)
        ) {
            VariantBottomSheetContent(
                {}, {}
            )
        }
    }
}



