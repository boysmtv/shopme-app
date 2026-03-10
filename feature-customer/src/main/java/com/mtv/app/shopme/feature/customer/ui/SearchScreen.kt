/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SearchScreen.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 15.16
 */
package com.mtv.app.shopme.feature.customer.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
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
import com.mtv.app.shopme.common.*
import com.mtv.app.shopme.data.FoodItemModel
import com.mtv.app.shopme.feature.customer.contract.*
import com.mtv.app.shopme.nav.CustomerBottomNavigationBar
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.data.mockFoodList

@Composable
fun SearchScreen(
    uiState: SearchStateListener,
    uiData: SearchDataListener,
    uiEvent: SearchEventListener,
    uiNavigation: SearchNavigationListener
) {

    val listState = rememberLazyListState()

    val items = uiData.results.ifEmpty {
        mockFoodList
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.collect { lastVisible ->

            val total = listState.layoutInfo.totalItemsCount

            if (lastVisible != null && lastVisible >= total - 2 && total > 0) {
                uiEvent.onLoadNextPage()
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

        SearchHeader(uiData, uiEvent)

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {

            itemsIndexed(items) { index, food ->

                SearchItem(
                    movie = food as FoodItemModel,
                    onClickItem = {
                        uiNavigation.onDetailClick()
                    },
                    previewDrawable = when (index) {
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
                )
            }
        }
    }
}

@Composable
fun SearchHeader(
    uiData: SearchDataListener,
    uiEvent: SearchEventListener
) {
    var query by remember { mutableStateOf(uiData.query) }

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
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.padding(start = 16.dp)
                )

                Spacer(Modifier.width(8.dp))

                BasicTextField(
                    value = query,
                    onValueChange = {
                        query = it
                        uiEvent.onQueryChanged(it)
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
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (query.isEmpty()) {
                                Text(
                                    "Search food here...",
                                    color = Color.Gray,
                                    fontFamily = PoppinsFont,
                                    fontSize = 14.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            query = ""
                            uiEvent.onQueryChanged("")
                        }
                    ) {
                        Icon(Icons.Default.Clear, contentDescription = null)
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
    movie: FoodItemModel,
    onClickItem: (Int) -> Unit,
    previewDrawable: Int? = null
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickItem(movie.id) }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {

            val imageModifier = Modifier
                .width(140.dp)
                .height(100.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    0.5.dp,
                    Color.DarkGray.copy(alpha = 0.15f),
                    RoundedCornerShape(8.dp)
                )

            if (previewDrawable != null) {

                Image(
                    painter = painterResource(previewDrawable),
                    contentDescription = null,
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )

            } else {

                Box(
                    modifier = imageModifier.background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Image", fontSize = 12.sp)
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {

                Text(
                    text = movie.name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = movie.desc,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 10.sp,
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = null,
                        tint = AppColor.Green,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Mamah Al Cafe", fontSize = 10.sp)
                }

                Spacer(Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = AppColor.Green,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Puri Lestari - Blok H12/01", fontSize = 10.sp)
                }
            }
        }

        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.DarkGray.copy(alpha = 0.6f)
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SearchScreenPreview() {

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
                uiState = SearchStateListener(),
                uiData = SearchDataListener(),
                uiEvent = SearchEventListener(),
                uiNavigation = SearchNavigationListener()
            )
        }
    }
}