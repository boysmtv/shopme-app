/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeScreen.kt
 *
 * Last modified by ChatGPT on 12/02/26
 */

package com.mtv.app.shopme.feature.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.InterFont
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.common.mockFoodList
import com.mtv.app.shopme.data.FoodItemModel
import com.mtv.app.shopme.feature.contract.HomeDataListener
import com.mtv.app.shopme.feature.contract.HomeEventListener
import com.mtv.app.shopme.feature.contract.HomeNavigationListener
import com.mtv.app.shopme.feature.contract.HomeStateListener
import com.mtv.app.shopme.nav.BottomNavigationBar
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    uiState: HomeStateListener,
    uiData: HomeDataListener,
    uiEvent: HomeEventListener,
    uiNavigation: HomeNavigationListener
) {

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        AppColor.LightOrange,
                        AppColor.WhiteSoft,
                        AppColor.White
                    )
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ HEADER (TIDAK IKUT SCROLL)
        HomeHeader()

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ LIST SCROLLABLE
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {

            item {
                Column {
                    HomeSearch(
                        onClick = {
                            uiNavigation.onNavigateToSearch()
                        }
                    )

                    Spacer(Modifier.height(16.dp))

                    HomePromoBanner()

                    Spacer(Modifier.height(16.dp))

                    HomeMenuBar()

                    Spacer(Modifier.height(20.dp))
                }
            }

            item(key = "top_choice_section") {
                HomeMenuTitle(
                    onSeeAllClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(
                                index = 1
                            )
                        }
                    }
                )
            }

            gridItems(
                data = mockFoodList,
                columnCount = 2,
                horizontalSpacing = 16.dp,
                verticalSpacing = 16.dp
            ) { item ->
                FoodCard(
                    item = item,
                    onClickDetail = { uiNavigation.onNavigateToDetail() }
                )
            }
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
            color = AppColor.Orange,
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
private fun HomeHeader() {
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
                    text = "Puri Lestari - Blok G06/01",
                    color = Color.DarkGray.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontFamily = PoppinsFont,
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = "Dedy Wijaya",
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
                color = AppColor.Orange,
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
        Icon(Icons.Default.Fastfood, contentDescription = null, tint = AppColor.Orange)
        Spacer(Modifier.height(6.dp))
        Text(title, fontFamily = PoppinsFont, fontSize = 11.sp)
    }
}

@Composable
fun FoodCard(
    item: FoodItemModel,
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
//            Image(
//                painter = rememberAsyncImagePainter(item.imageUrl),
//                contentDescription = item.name,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(100.dp),
//                contentScale = ContentScale.Crop
//            )

            val imageRes = when (item.id) {
                0 -> R.drawable.image_burger
                1 -> R.drawable.image_pizza
                2 -> R.drawable.image_platbread
                3 -> R.drawable.image_cheese_burger
                4 -> R.drawable.image_bakso
                5 -> R.drawable.image_pempek
                6 -> R.drawable.image_padang
                7 -> R.drawable.image_sate
                else -> R.drawable.image_burger
            }

            Box {
                Image(
                    painter = painterResource(imageRes),
                    contentDescription = null,
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
                    text = "$${item.price}",
                    fontFamily = InterFont,
                    fontWeight = FontWeight.Medium,
                    color = AppColor.Orange
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HomeHeaderPreview() {
//    HomeHeader()
//}
//
//@Preview(showBackground = true)
//@Composable
//fun HomePromoPreview() {
//    HomePromoBanner()
//}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun HomeScreenPreview() {
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
            HomeScreen(
                uiState = HomeStateListener(),
                uiData = HomeDataListener(),
                uiEvent = HomeEventListener({}),
                uiNavigation = HomeNavigationListener({})
            )
        }
    }
}