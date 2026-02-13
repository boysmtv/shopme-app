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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
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

@Composable
fun HomeScreen(
    uiState: HomeStateListener,
    uiData: HomeDataListener,
    uiEvent: HomeEventListener,
    uiNavigation: HomeNavigationListener
) {
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
            .padding(start = 20.dp, end = 20.dp, top = 32.dp)
    ) {
        HomeHeader()

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                Column {
                    HomeSearch(
                        query = "",
                        onQueryChange = {},
                        onClearClick = {}
                    )

                    Spacer(Modifier.height(16.dp))

                    HomePromoBanner()

                    Spacer(Modifier.height(16.dp))

                    HomeMenuBar()

                    Spacer(Modifier.height(20.dp))
                }
            }

            item {
                HomeMenuTitle()
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
private fun HomeMenuTitle() {
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
                    text = "Location",
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
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Discover amazing food offers",
            color = Color.Black,
            fontSize = 22.sp,
            fontFamily = PoppinsFont
        )

        Spacer(Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(Color.White, RoundedCornerShape(24.dp))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.DarkGray.copy(alpha = 0.7f),
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        singleLine = true,
                        cursorBrush = SolidColor(Color.LightGray),
                        textStyle = TextStyle(
                            color = Color.DarkGray,
                            fontSize = 16.sp
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (query.isEmpty()) {
                                    Text(
                                        text = "Order your food here...",
                                        color = Color.Gray,
                                        fontSize = 14.sp,
                                        fontFamily = PoppinsFont
                                    )
                                }
                                innerTextField()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(start = 8.dp, end = 12.dp)
                    )

                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = onClearClick,
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear text",
                                tint = Color.DarkGray.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
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
            .clickable {
                onClickDetail()
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(item.imageUrl),
                contentDescription = item.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentScale = ContentScale.Crop
            )

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