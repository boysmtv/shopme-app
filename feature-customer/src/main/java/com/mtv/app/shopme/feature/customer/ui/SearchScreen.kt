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
import androidx.compose.material.icons.filled.LocationOn
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
import coil.compose.AsyncImage
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.domain.model.FoodCategory
import com.mtv.app.shopme.data.dto.FoodItemModel
import com.mtv.app.shopme.domain.model.FoodStatus
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.app.shopme.data.remote.response.PageResponse
import com.mtv.app.shopme.domain.mapper.toUiModel
import com.mtv.app.shopme.feature.customer.contract.SearchDataListener
import com.mtv.app.shopme.feature.customer.contract.SearchEventListener
import com.mtv.app.shopme.feature.customer.contract.SearchNavigationListener
import com.mtv.app.shopme.feature.customer.contract.SearchStateListener
import com.mtv.app.shopme.nav.customer.CustomerBottomNavigationBar
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING
import java.math.BigDecimal
import kotlinx.coroutines.flow.distinctUntilChanged
import org.threeten.bp.LocalDateTime

@Composable
fun SearchScreen(
    uiState: SearchStateListener,
    uiData: SearchDataListener,
    uiEvent: SearchEventListener,
    uiNavigation: SearchNavigationListener
) {

    val listState = rememberLazyListState()

    val items: List<FoodItemModel> = uiData.results.map { it.toUiModel() }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }
            .distinctUntilChanged()
            .collect { lastVisible ->

                val total = listState.layoutInfo.totalItemsCount

                if (
                    lastVisible != null &&
                    lastVisible >= total - 2 &&
                    total > 0
                ) {
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

        val isLoading = uiState.searchFoodState is Resource.Loading
        if (uiData.query.isNotEmpty() && items.isEmpty() && !isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No result found")
            }
        } else {

            if (uiState.searchFoodState is Resource.Loading && items.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Loading...")
                }
            }

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
                        movie = food,
                        onClickItem = {
                            uiNavigation.onDetailClick(it)
                        },
                        previewDrawable = getPreviewDrawable(index)
                    )
                }

                item {
                    if (uiState.searchFoodState is Resource.Loading && items.isNotEmpty()) {
                        Text("Loading more...")
                    }
                }
            }
        }
    }
}

@Composable
fun SearchHeader(
    uiData: SearchDataListener,
    uiEvent: SearchEventListener
) {
    var query by remember(uiData.query) { mutableStateOf(uiData.query) }

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
                            query = EMPTY_STRING
                            uiEvent.onQueryChanged(EMPTY_STRING)
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
    onClickItem: (String) -> Unit,
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

            if (previewDrawable != null && movie.imageUrl.isEmpty()) {
                Image(
                    painter = painterResource(previewDrawable),
                    contentDescription = null,
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            } else {
                AsyncImage(
                    model = movie.imageUrl,
                    contentDescription = null,
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(previewDrawable ?: R.drawable.no_image),
                    error = painterResource(previewDrawable ?: R.drawable.no_image)
                )
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
                    text = movie.description,
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
                    Text(movie.cafeName, fontSize = 10.sp)
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
                    Text(movie.cafeAddress, fontSize = 10.sp)
                }
            }
        }

        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.DarkGray.copy(alpha = 0.6f)
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

    val navController = rememberNavController()

    val mockResults = mockFoodResponses()

    val mockData = SearchDataListener(
        query = "burger",
        results = mockResults
    )

    val mockState = SearchStateListener(
        searchFoodState = Resource.Success(
            ApiResponse(
                code = "SUCCESS",
                message = "Success",
                data = PageResponse(
                    content = mockResults,
                    page = 0,
                    size = mockResults.size,
                    totalElements = mockResults.size,
                    totalPages = 1,
                    last = true
                ),
                timestamp = "",
                status = 200,
                traceId = ""
            )
        )
    )

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
                uiState = mockState,
                uiData = mockData,
                uiEvent = SearchEventListener(),
                uiNavigation = SearchNavigationListener()
            )
        }
    }
}

fun mockFoodResponses(): List<FoodResponse> {
    return listOf(
        FoodResponse(
            id = "1",
            cafeId = "cafe_1",
            name = "Burger Cheese",
            cafeName = "Cafe Jakarta",
            cafeAddress = "Jl. Sudirman No.1",
            description = "Delicious burger with melted cheese",
            price = BigDecimal("25000"),
            category = FoodCategory.FOOD,
            status = FoodStatus.READY,
            quantity = 10,
            estimate = "10-15 min",
            isActive = true,
            createdAt = LocalDateTime.of(2024, 1, 1, 12, 0),
            images = listOf(""),
            variants = emptyList()
        ),
        FoodResponse(
            id = "2",
            cafeId = "cafe_2",
            name = "Pizza Pepperoni",
            cafeName = "Pizza House",
            cafeAddress = "Jl. Thamrin No.10",
            description = "Hot pizza with pepperoni",
            price = BigDecimal("50000"),
            category = FoodCategory.FOOD,
            status = FoodStatus.READY,
            quantity = 5,
            estimate = "15-20 min",
            isActive = true,
            createdAt = LocalDateTime.of(2024, 1, 1, 12, 0),
            images = listOf(""),
            variants = emptyList()
        ),
        FoodResponse(
            id = "3",
            cafeId = "cafe_3",
            name = "Bakso Urat",
            cafeName = "Bakso Pak Kumis",
            cafeAddress = "Jl. Asia Afrika",
            description = "Bakso kenyal dengan kuah gurih",
            price = BigDecimal("20000"),
            category = FoodCategory.FOOD,
            status = FoodStatus.READY,
            quantity = 20,
            estimate = "5-10 min",
            isActive = true,
            createdAt = LocalDateTime.of(2024, 1, 1, 12, 0),
            images = listOf(""),
            variants = emptyList()
        ),
        FoodResponse(
            id = "4",
            cafeId = "cafe_4",
            name = "Nasi Goreng Spesial",
            cafeName = "Warung Nusantara",
            cafeAddress = "Jl. Malioboro",
            description = "Nasi goreng dengan telur dan ayam",
            price = BigDecimal("30000"),
            category = FoodCategory.FOOD,
            status = FoodStatus.READY,
            quantity = 15,
            estimate = "10-15 min",
            isActive = true,
            createdAt = LocalDateTime.of(2024, 1, 1, 12, 0),
            images = listOf(""),
            variants = emptyList()
        ),
        FoodResponse(
            id = "5",
            cafeId = "cafe_5",
            name = "Sate Ayam",
            cafeName = "Sate Madura",
            cafeAddress = "Jl. Diponegoro",
            description = "Sate ayam dengan bumbu kacang",
            price = BigDecimal("28000"),
            category = FoodCategory.FOOD,
            status = FoodStatus.READY,
            quantity = 25,
            estimate = "10-15 min",
            isActive = true,
            createdAt = LocalDateTime.of(2024, 1, 1, 12, 0),
            images = listOf(""),
            variants = emptyList()
        ),
        FoodResponse(
            id = "6",
            cafeId = "cafe_6",
            name = "Ice Matcha Latte",
            cafeName = "Matcha House",
            cafeAddress = "Jl. Braga",
            description = "Minuman matcha creamy dan segar",
            price = BigDecimal("28000"),
            category = FoodCategory.DRINK,
            status = FoodStatus.READY,
            quantity = 40,
            estimate = "5-10 min",
            isActive = true,
            createdAt = LocalDateTime.of(2024, 1, 1, 12, 0),
            images = listOf(""),
            variants = emptyList()
        ),
        FoodResponse(
            id = "7",
            cafeId = "cafe_7",
            name = "Cappuccino",
            cafeName = "Coffee Corner",
            cafeAddress = "Jl. Sudirman",
            description = "Kopi cappuccino dengan foam lembut",
            price = BigDecimal("32000"),
            category = FoodCategory.DRINK,
            status = FoodStatus.READY,
            quantity = 30,
            estimate = "5 min",
            isActive = true,
            createdAt = LocalDateTime.of(2024, 1, 1, 12, 0),
            images = listOf(""),
            variants = emptyList()
        ),
        FoodResponse(
            id = "8",
            cafeId = "cafe_8",
            name = "French Fries",
            cafeName = "Snack Bar",
            cafeAddress = "Jl. Gatot Subroto",
            description = "Kentang goreng crispy dengan saus",
            price = BigDecimal("18000"),
            category = FoodCategory.SNACK, // ⬅️ pastikan enum ada
            status = FoodStatus.READY,
            quantity = 50,
            estimate = "5-10 min",
            isActive = true,
            createdAt = LocalDateTime.of(2024, 1, 1, 12, 0),
            images = listOf(""),
            variants = emptyList()
        )
    )
}