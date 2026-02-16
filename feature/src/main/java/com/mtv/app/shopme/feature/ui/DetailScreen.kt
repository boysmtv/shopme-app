/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: DetailScreen.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 08.58
 */

package com.mtv.app.shopme.feature.ui

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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.mtv.app.shopme.feature.contract.DetailDataListener
import com.mtv.app.shopme.feature.contract.DetailEventListener
import com.mtv.app.shopme.feature.contract.DetailNavigationListener
import com.mtv.app.shopme.feature.contract.DetailStateListener
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

data class SimilarItem(
    val image: Int,
    val title: String,
    val price: Double
)

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

    Scaffold(
        bottomBar = { AddToCartBar() }
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

            item { DetailLocation(
                onClickCafe = { uiNavigation.onclickCafe() }
            ) }
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
fun AddToCartBar() {
    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(AppColor.Orange),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(56.dp)
    ) {
        Text("Add to cart  •  $9.99", color = Color.White, fontSize = 17.sp)
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
            .height(280.dp)
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
                    .clip(RoundedCornerShape(16.dp)),
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

enum class OrderStatus {
    READY,
    PREORDER,
    JASTIP
}

@Composable
fun StatusStatItem(
    status: OrderStatus,
    total: String = EMPTY_STRING
) {
    val (icon, color, text) = when (status) {
        OrderStatus.READY -> Triple(
            Icons.Default.CheckCircle,
            Color(0xFF4CAF50),
            if (total.isNotEmpty()) "Ready / $total pcs" else "Ready"
        )

        OrderStatus.PREORDER -> Triple(
            Icons.Default.Schedule,
            Color(0xFFFF9800),
            if (total.isNotEmpty()) "Pre-order / $total pcs" else "Pre-order"
        )

        OrderStatus.JASTIP -> Triple(
            Icons.Default.LocalShipping,
            Color(0xFF2196F3),
            if (total.isNotEmpty()) "Jastip / $total pcs" else "Jastip"
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
            color = color
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
            color = AppColor.Orange
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
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "$${price}",
                color = AppColor.Orange,
                fontSize = 14.sp
            )
        }

        Box(
            contentAlignment = Alignment.Center
        ) {
            StatusStatItem(OrderStatus.PREORDER, "5")
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



