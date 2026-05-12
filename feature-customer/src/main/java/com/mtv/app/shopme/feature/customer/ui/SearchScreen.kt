/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SearchScreen.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 15.16
 */

package com.mtv.app.shopme.feature.customer.ui

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.ContentErrorState
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.common.SmartImage
import com.mtv.app.shopme.common.navbar.customer.CustomerBottomNavigationBar
import com.mtv.app.shopme.domain.model.SearchFood
import com.mtv.app.shopme.feature.customer.contract.SearchEvent
import com.mtv.app.shopme.feature.customer.contract.SearchUiState
import com.mtv.app.shopme.feature.customer.ui.shimmer.ShimmerSearchItem
import com.mtv.app.shopme.feature.customer.ui.shimmer.ShimmerSearchScreen
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING
import java.math.BigDecimal
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun SearchScreen(
    state: SearchUiState,
    event: (SearchEvent) -> Unit
) {
    val listState = rememberLazyListState()
    val items = (state.foods as? LoadState.Success)?.data ?: emptyList()
    val isLoading = state.foods is LoadState.Loading

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }
            .distinctUntilChanged()
            .collect { lastVisible ->
                val total = listState.layoutInfo.totalItemsCount
                if (lastVisible != null && lastVisible >= total - 2 && total > 0) {
                    event(SearchEvent.LoadNextPage)
                }
            }
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
            .padding(horizontal = 20.dp)
            .statusBarsPadding()
    ) {
        Spacer(Modifier.height(16.dp))

        SearchHeader(
            query = state.query,
            onQueryChanged = { event(SearchEvent.QueryChanged(it)) }
        )

        Spacer(Modifier.height(16.dp))

        when {
            state.query.isNotEmpty() && items.isEmpty() && !isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No result found",
                        fontFamily = PoppinsFont,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            isLoading && items.isEmpty() -> {
                ShimmerSearchScreen()
            }

            state.foods is LoadState.Error && items.isEmpty() -> {
                ContentErrorState(
                    title = "Gagal memuat hasil pencarian",
                    message = (state.foods as LoadState.Error).error.message,
                    actionLabel = "Coba lagi",
                    onRetry = { event(SearchEvent.Load) }
                )
            }

            else -> {
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(
                    items = items,
                    key = { _, item -> item.id }
                ) { index, food ->
                    SearchItem(
                        food = food,
                        isFavorite = state.favoriteIds.contains(food.id),
                        onClickItem = { event(SearchEvent.ClickFood(it)) },
                        onToggleFavorite = { event(SearchEvent.ToggleFavorite(food.id)) },
                        previewDrawable = getPreviewDrawable(index)
                    )
                }

                    // Loading more indicator
                    if (state.isLoadingMore) {
                        item {
                            SearchPaginationShimmer()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchPaginationShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(2) {
            ShimmerSearchItem()
        }
    }
}

@Composable
fun SearchHeader(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    var localQuery by remember(query) { mutableStateOf(query) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .background(Color.White, RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.padding(start = 16.dp)
                )

                Spacer(Modifier.width(8.dp))

                BasicTextField(
                    value = localQuery,
                    onValueChange = {
                        localQuery = it
                        onQueryChanged(it)
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontFamily = PoppinsFont
                    ),
                    cursorBrush = SolidColor(AppColor.Green),
                    modifier = Modifier.weight(1f),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (localQuery.isEmpty()) {
                                Text(
                                    text = "Search food here...",
                                    color = Color.Gray,
                                    fontFamily = PoppinsFont,
                                    fontSize = 14.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                if (localQuery.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            localQuery = EMPTY_STRING
                            onQueryChanged(EMPTY_STRING)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null
                        )
                    }
                }
            }
        }

        Spacer(Modifier.width(16.dp))

        Icon(
            imageVector = Icons.Default.Favorite,
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

@Composable
fun SearchItem(
    food: SearchFood,
    isFavorite: Boolean,
    onClickItem: (String) -> Unit,
    onToggleFavorite: () -> Unit,
    previewDrawable: Int? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickItem(food.id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            val imageModifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 0.5.dp,
                    color = Color.DarkGray.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                )

            Box {
                SmartImage(
                    model = food.image,
                    contentDescription = null,
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(previewDrawable ?: R.drawable.no_image),
                    error = painterResource(previewDrawable ?: R.drawable.no_image)
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

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = food.name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )

                Text(
                    text = "Rp ${food.price.toPlainString()}",
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = AppColor.Green
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = AppColor.Green,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = food.cafeName,
                        fontSize = 10.sp,
                        fontFamily = PoppinsFont,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.DarkGray.copy(alpha = 0.3f)
        )
    }
}

fun getPreviewDrawable(index: Int): Int? = when (index) {
    0 -> R.drawable.image_burger
    1 -> R.drawable.image_pizza
    2 -> R.drawable.image_platbread
    3 -> R.drawable.image_cheese_burger
    4 -> R.drawable.image_bakso
    5 -> R.drawable.image_pempek
    6 -> R.drawable.image_padang
    7 -> R.drawable.image_sate
    else -> null
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SearchScreenPreview() {

    val mockItems = listOf(
        SearchFood(
            id = "1",
            name = "Burger Cheese",
            price = BigDecimal("25000"),
            image = "",
            cafeName = "Cafe Jakarta"
        ),
        SearchFood(
            id = "2",
            name = "Pizza Pepperoni",
            price = BigDecimal("50000"),
            image = "",
            cafeName = "Pizza House"
        ),
        SearchFood(
            id = "3",
            name = "Bakso Urat",
            price = BigDecimal("20000"),
            image = "",
            cafeName = "Bakso Pak Kumis"
        ),
        SearchFood(
            id = "4",
            name = "Nasi Goreng Spesial",
            price = BigDecimal("30000"),
            image = "",
            cafeName = "Warung Nusantara"
        ),
        SearchFood(
            id = "5",
            name = "Sate Ayam",
            price = BigDecimal("28000"),
            image = "",
            cafeName = "Sate Madura"
        ),
        SearchFood(
            id = "6",
            name = "Ice Matcha Latte",
            price = BigDecimal("28000"),
            image = "",
            cafeName = "Matcha House"
        ),
        SearchFood(
            id = "7",
            name = "Cappuccino",
            price = BigDecimal("32000"),
            image = "",
            cafeName = "Coffee Corner"
        ),
        SearchFood(
            id = "8",
            name = "French Fries",
            price = BigDecimal("18000"),
            image = "",
            cafeName = "Snack Bar"
        )
    )

    val mockState = SearchUiState(
        query = "burger",
        foods = LoadState.Success(mockItems),
        isLoadingMore = false,
        isLastPage = true,
        page = 0
    )

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
            SearchScreen(
                state = mockState,
                event = {}
            )
        }
    }
}
