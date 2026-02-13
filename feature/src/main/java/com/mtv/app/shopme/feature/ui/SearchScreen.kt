/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SearchScreen.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 15.16
 */

package com.mtv.app.shopme.feature.ui

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
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
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.common.base64ToBitmap
import com.mtv.app.shopme.common.mockFoodList
import com.mtv.app.shopme.data.FoodItemModel
import com.mtv.app.shopme.feature.contract.SearchDataListener
import com.mtv.app.shopme.feature.contract.SearchEventListener
import com.mtv.app.shopme.feature.contract.SearchNavigationListener
import com.mtv.app.shopme.feature.contract.SearchStateListener
import com.mtv.app.shopme.nav.BottomNavigationBar

import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

@Composable
fun SearchScreen(
    uiState: SearchStateListener,
    uiData: SearchDataListener,
    uiEvent: SearchEventListener,
    uiNavigation: SearchNavigationListener
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
            .padding(start = 20.dp, end = 20.dp)
            .height(56.dp)
            .statusBarsPadding()
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        SearchHeader(uiData, uiEvent)

        Spacer(modifier = Modifier.height(16.dp))

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier.verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            mockFoodList.forEachIndexed { index, food ->
                SearchItem(
                    movie = food,
                    onClickMovie = {},
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
                    contentDescription = "Search",
                    tint = Color.Gray,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(Modifier.width(8.dp))
                BasicTextField(
                    value = query,
                    onValueChange = { query = it; uiEvent.onQueryChanged(it) },
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black, fontSize = 14.sp, fontFamily = PoppinsFont),
                    cursorBrush = SolidColor(AppColor.Orange),
                    decorationBox = { innerTextField ->
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (query.isEmpty()) Text(
                                "Search food here...",
                                color = Color.Gray,
                                fontFamily = PoppinsFont,
                                fontSize = 14.sp
                            )
                            innerTextField()
                        }
                    },
                    modifier = Modifier.weight(1f)
                )

                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = ""; uiEvent.onQueryChanged("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color.Gray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

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

@Composable
fun SearchItem(
    movie: FoodItemModel,
    onClickMovie: (Int) -> Unit,
    previewDrawable: Int? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickMovie(movie.id) }
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
                    width = 0.5.dp,
                    color = Color.DarkGray.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                )

            if (movie.imageUrl.isNotEmpty()) {
                val bitmap = base64ToBitmap(movie.imageUrl)
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = movie.name,
                        modifier = imageModifier,
                        contentScale = ContentScale.Crop
                    )
                } else if (previewDrawable != null) {
                    Image(
                        painter = painterResource(previewDrawable),
                        contentDescription = movie.name,
                        modifier = imageModifier,
                        contentScale = ContentScale.FillWidth
                    )
                } else {
                    Box(
                        modifier = imageModifier
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Image",
                            fontSize = 12.sp,
                            fontFamily = PoppinsFont,
                            color = Color.DarkGray
                        )
                    }
                }
            } else if (previewDrawable != null) {
                Image(
                    painter = painterResource(previewDrawable),
                    contentDescription = movie.name,
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = imageModifier
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No Image",
                        fontSize = 12.sp,
                        fontFamily = PoppinsFont,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                Text(
                    text = movie.name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.DarkGray,
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = movie.desc,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.DarkGray,
                    fontFamily = PoppinsFont,
                    fontSize = 10.sp,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = AppColor.Orange,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Mamah Al Cafe",
                        color = Color.DarkGray,
                        fontSize = 10.sp,
                        fontFamily = PoppinsFont
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = AppColor.Orange,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Puri Lestari - Blok H12/01",
                        color = Color.DarkGray,
                        fontSize = 10.sp,
                        fontFamily = PoppinsFont
                    )
                }
            }
        }

        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.DarkGray.copy(alpha = 0.6f)
        )
    }
}


//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//fun SearchItemPreview() {
//    SearchItem(
//        movie = mockFoodList.first(),
//        onClickMovie = {},
//        previewDrawable = R.drawable.image_burger
//    )
//}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SearchScreenPreview() {
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
            SearchScreen(
                uiState = SearchStateListener(),
                uiData = SearchDataListener(results = listOf("Burger", "Pizza", "Sushi")),
                uiEvent = SearchEventListener(),
                uiNavigation = SearchNavigationListener()
            )
        }
    }
}
