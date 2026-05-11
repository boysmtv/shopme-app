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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SetMeal
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.compose.rememberNavController
import com.mtv.app.shopme.common.*
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.common.navbar.customer.CustomerBottomNavigationBar
import com.mtv.app.shopme.data.mock.DataUiMock
import com.mtv.app.shopme.domain.model.*
import com.mtv.app.shopme.feature.customer.contract.HomeEvent
import com.mtv.app.shopme.feature.customer.contract.HomeUiState
import com.mtv.app.shopme.feature.customer.ui.shimmer.ShimmerHomeHeaderSkeleton
import com.mtv.app.shopme.feature.customer.ui.shimmer.ShimmerFoodSkeletonGrid
import com.mtv.app.shopme.feature.customer.utils.*
import com.mtv.based.core.network.utils.LoadState
import java.math.BigDecimal
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    state: HomeUiState,
    event: (HomeEvent) -> Unit
) {
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

        when (val customerState = state.customer) {
            is LoadState.Loading -> {
                ShimmerHomeHeaderSkeleton()
            }

            is LoadState.Success -> {
                HomeHeader(
                    customerData = customerState.data,
                    onNotifClick = { event(HomeEvent.ClickNotif) }
                )
            }

            else -> Unit
        }

        Spacer(Modifier.height(16.dp))

        HomeContent(
            state = state,
            onClickFood = { event(HomeEvent.ClickFood(it)) },
            onToggleFavorite = { event(HomeEvent.ToggleFavorite(it)) },
            onClickSearch = { event(HomeEvent.ClickSearch) },
            onCategoryClick = { event(HomeEvent.ClickCategory(it)) },
            onLoadNextPage = { event(HomeEvent.LoadNextPage) },
            onRetry = { event(HomeEvent.Load) }
        )
    }
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    onClickFood: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onClickSearch: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onLoadNextPage: () -> Unit,
    onRetry: () -> Unit
) {

    val listState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }
            .distinctUntilChanged()
            .collect { lastVisible ->
                val total = listState.layoutInfo.totalItemsCount
                if (lastVisible != null && lastVisible >= total - 2 && total > 0) {
                    onLoadNextPage()
                }
            }
    }

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

                HomeMenuBar(
                    onCategoryClick = onCategoryClick
                )

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

        when (val foodsState = state.foods) {
            is LoadState.Loading -> {
                item {
                    ShimmerFoodSkeletonGrid()
                }
            }

            is LoadState.Success -> {
                gridItems(
                    data = foodsState.data,
                    columnCount = 2,
                    horizontalSpacing = 16.dp,
                    verticalSpacing = 16.dp
                ) { item ->
                    SearchFoodCard(
                        item = item,
                        isFavorite = state.favoriteIds.contains(item.id),
                        onClickDetail = { onClickFood(item.id) },
                        onToggleFavorite = { onToggleFavorite(item.id) }
                    )
                }

                if (foodsState.data.isEmpty()) {
                    item { EmptyState() }
                }

                if (state.isLoadingMore) {
                    item {
                        PaginationFoodShimmer()
                    }
                }
            }

            is LoadState.Error -> {
                item {
                    ContentErrorState(
                        title = "Gagal memuat menu",
                        message = foodsState.error.message,
                        actionLabel = "Muat ulang",
                        onRetry = onRetry
                    )
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun PaginationFoodShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(2) {
            PaginationGridCardShimmer()
        }
    }
}

@Composable
private fun PaginationGridCardShimmer() {
    val brush = shimmerBrush()
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(brush)
            )
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.45f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush)
                )
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
private fun HomeMenuBar(
    onCategoryClick: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            listOf(
                "Makanan" to Icons.Default.SetMeal,
                "Minuman" to Icons.Default.LocalCafe,
                "Cemilan" to Icons.Default.Fastfood,
                "Produk" to Icons.Default.ShoppingBag,
                "Lainnya" to Icons.Default.LocalMall
            )
        ) { (title, icon) ->
            CategoryItem(
                title = title,
                icon = icon,
                onClick = { onCategoryClick(title) }
            )
        }
    }
}

@Composable
fun CategoryItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .size(80.dp)
            .background(AppColor.White, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Icon(icon, contentDescription = null, tint = AppColor.Green)
        Spacer(Modifier.height(6.dp))
        Text(title, fontFamily = PoppinsFont, fontSize = 11.sp)
    }
}

@Composable
fun SearchFoodCard(
    item: SearchFood,
    isFavorite: Boolean,
    onClickDetail: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickDetail() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {

            Box {
                SmartImage(
                    model = item.image,
                    contentDescription = item.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentScale = ContentScale.Crop
                )

                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = if (isFavorite) Color.Red else Color.White,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(22.dp)
                        .background(Color.Black.copy(alpha = 0.35f), CircleShape)
                        .clickable { onToggleFavorite() }
                        .padding(4.dp)
                )
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

                Spacer(Modifier.height(2.dp))

                Text(
                    text = item.cafeName,
                    fontFamily = InterFont,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun HomeScreenPreview() {

    val state = HomeUiState(
        customer = LoadState.Success(DataUiMock.customer()),
        foods = LoadState.Success(
            listOf(
                SearchFood("1", "Nasi Goreng Kampung", BigDecimal(24000), "", "Kopi Tugu Senja"),
                SearchFood("2", "Es Kopi Susu Aren", BigDecimal(19000), "", "Kopi Tugu Senja")
            )
        ),
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
                event = {}
            )
        }
    }
}
