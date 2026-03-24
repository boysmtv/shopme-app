/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeScreen.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26
 */

package com.mtv.app.shopme.feature.customer.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.mtv.app.shopme.common.*
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.common.navbar.customer.CustomerBottomNavigationBar
import com.mtv.app.shopme.data.mock.HomeUiMock
import com.mtv.app.shopme.domain.model.*
import com.mtv.app.shopme.feature.customer.contract.HomeUiState
import com.mtv.app.shopme.feature.customer.utils.*
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    state: HomeUiState,
    onClickFood: (String) -> Unit,
    onClickSearch: () -> Unit,
    onClickNotif: () -> Unit,
    onRefresh: () -> Unit,
) {

    if (state.isFoodsLoading && state.foods.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LinearProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        AppColor.GreenSoft,
                        AppColor.WhiteSoft,
                        AppColor.White
                    )
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
    ) {

        Spacer(Modifier.height(16.dp))

        HomeHeader(
            customerData = state.customer,
            onNotifClick = onClickNotif
        )

        Spacer(Modifier.height(16.dp))

        state.error?.let {
            ErrorBanner(
                message = it.message,
                onDismiss = onRefresh
            )
        }

        Spacer(Modifier.height(8.dp))

        HomeContent(
            state = state,
            onClickFood = onClickFood,
            onClickSearch = onClickSearch
        )
    }
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    onClickFood: (String) -> Unit,
    onClickSearch: () -> Unit
) {

    val listState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }

    val scope = rememberCoroutineScope()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {

        item {
            Column {

                HomeSearch { onClickSearch() }

                Spacer(Modifier.height(16.dp))

                HomePromoBanner()

                Spacer(Modifier.height(16.dp))

                HomeMenuBar()

                Spacer(Modifier.height(20.dp))
            }
        }

        item {
            HomeMenuTitle {
                scope.launch {
                    listState.animateScrollToItem(1)
                }
            }
        }

        gridItems(
            data = state.foods,
            columnCount = 2,
            horizontalSpacing = 16.dp,
            verticalSpacing = 16.dp
        ) { item ->

            FoodCard(
                item = item,
                onClickDetail = { onClickFood(item.id) }
            )
        }

        if (state.isLoading && state.foods.isNotEmpty()) {
            item {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }

        if (state.foods.isEmpty() && !state.isLoading) {
            item {
                EmptyState()
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Icon(
                imageVector = Icons.Default.Fastfood,
                contentDescription = null,
                tint = Color.Gray
            )

            Spacer(Modifier.height(8.dp))

            Text("No food available", color = Color.Gray)
        }
    }
}

@Composable
private fun HomeMenuTitle(
    onSeeAllClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Top choices for you",
            fontWeight = FontWeight.SemiBold,
            fontFamily = PoppinsFont,
            fontSize = 16.sp,
        )

        Text(
            text = "See All >>",
            fontWeight = FontWeight.Thin,
            fontFamily = PoppinsFont,
            fontSize = 16.sp,
            color = AppColor.Green,
            modifier = Modifier.clickable {
                onSeeAllClick()
            }
        )
    }

    Spacer(Modifier.height(16.dp))
}

fun <T> LazyListScope.gridItems(
    data: List<T>,
    columnCount: Int,
    horizontalSpacing: Dp,
    verticalSpacing: Dp,
    modifier: Modifier = Modifier,
    itemContent: @Composable BoxScope.(T) -> Unit
) {
    val rows = data.chunked(columnCount)

    items(rows) { rowItems ->
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)
        ) {
            rowItems.forEach { item ->
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    itemContent(item)
                }
            }

            if (rowItems.size < columnCount) {
                repeat(columnCount - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(verticalSpacing))
    }
}

@Composable
private fun HomeHeader(
    customerData: Customer?,
    onNotifClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_location_white),
                contentDescription = "Location",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(12.dp),
                tint = Color.Black
            )

            Column(
                modifier = Modifier.padding(start = 12.dp)
            ) {
                Text(
                    text = checkAddress(customerData),
                    color = Color.DarkGray.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontFamily = PoppinsFont,
                    style = MaterialTheme.typography.titleSmall,
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = checkName(customerData),
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontFamily = PoppinsFont,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_notification_white),
            contentDescription = "Notification",
            modifier = Modifier
                .clickable { onNotifClick() }
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(12.dp),
            tint = Color.Black
        )
    }
}

@Composable
private fun HomeSearch(
    onClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Discover amazing food offers",
            color = Color.Black,
            fontSize = 20.sp,
            fontFamily = PoppinsFont
        )

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.White, RoundedCornerShape(24.dp))
                .clickable { onClick() }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.DarkGray.copy(alpha = 0.7f),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "Order your food here...",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontFamily = PoppinsFont
                )
            }
        }
    }
}


@Composable
private fun HomePromoBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .background(
                color = AppColor.Green,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Special Offer 🔥",
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.White
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Get 30% discount\non your first order",
                    fontFamily = InterFont,
                    fontSize = 13.sp,
                    color = Color.White
                )
            }

            Image(
                painter = painterResource(id = R.drawable.ic_location_white),
                contentDescription = null,
                modifier = Modifier.size(90.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
private fun HomeMenuBar() {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            listOf("Burger", "Sandwich", "Sushi", "Pizza", "Noodles", "Steak", "Coffee", "Dessert")
        ) { title ->
            CategoryItem(title)
        }
    }
}

@Composable
fun CategoryItem(title: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .size(80.dp)
            .background(AppColor.White, RoundedCornerShape(16.dp))
            .clickable {}
            .padding(12.dp)
    ) {
        Icon(Icons.Default.Fastfood, contentDescription = null, tint = AppColor.Green)
        Spacer(Modifier.height(6.dp))
        Text(title, fontFamily = PoppinsFont, fontSize = 11.sp)
    }
}

@Composable
fun FoodCard(
    item: Food,
    onClickDetail: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (item.isActive) {
                    Modifier.clickable { onClickDetail() }
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {

            Box {
                AsyncImage(
                    model = item.images.first(),
                    contentDescription = item.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentScale = ContentScale.Crop
                )

                if (!item.isActive) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                    )

                    Text(
                        text = "Tidak Tersedia",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = PoppinsFont,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .background(
                                Color.Black.copy(alpha = 0.7f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Column(Modifier.padding(12.dp)) {
                Text(
                    text = item.name,
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = item.price.toRupiah(),
                    fontFamily = InterFont,
                    fontWeight = FontWeight.Medium,
                    color = AppColor.Green
                )
            }
        }
    }
}

@Composable
private fun ErrorBanner(
    message: String,
    onDismiss: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Red.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            .clickable { onDismiss() }
            .padding(8.dp)
    ) {
        Text(
            text = message,
            color = Color.Red,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun HomeScreenPreview() {

    val state = HomeUiState(
        customer = HomeUiMock.customer(),
        foods = HomeUiMock.foods(),
        isLoading = false,
        error = null
    )

    Scaffold(
        bottomBar = {
            CustomerBottomNavigationBar(rememberNavController())
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(bottom = padding.calculateBottomPadding())
                .fillMaxSize()
        ) {
            HomeScreen(
                state = state,
                onEvent = {}
            )
        }
    }
}