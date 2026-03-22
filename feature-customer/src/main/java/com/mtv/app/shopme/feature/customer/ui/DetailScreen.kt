/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: DetailScreen.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 08.58
 */

package com.mtv.app.shopme.feature.customer.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.common.toRupiah
import com.mtv.app.shopme.data.dto.FoodCategory
import com.mtv.app.shopme.data.dto.FoodStatus
import com.mtv.app.shopme.data.remote.request.CartVariantRequest
import com.mtv.app.shopme.data.remote.response.AddressResponse
import com.mtv.app.shopme.data.remote.response.CustomerResponse
import com.mtv.app.shopme.data.remote.response.FoodOptionResponse
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.app.shopme.data.remote.response.FoodVariantResponse
import com.mtv.app.shopme.data.remote.response.MenuSummary
import com.mtv.app.shopme.data.remote.response.Stats
import com.mtv.app.shopme.feature.customer.contract.DetailDataListener
import com.mtv.app.shopme.feature.customer.contract.DetailEventListener
import com.mtv.app.shopme.feature.customer.contract.DetailNavigationListener
import com.mtv.app.shopme.feature.customer.contract.DetailStateListener
import com.mtv.app.shopme.feature.customer.utils.StatItem
import com.mtv.app.shopme.feature.customer.utils.StatusStatItem
import com.mtv.app.shopme.feature.customer.utils.checkPrice
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING
import java.math.BigDecimal
import org.threeten.bp.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    uiState: DetailStateListener,
    uiData: DetailDataListener,
    uiEvent: DetailEventListener,
    uiNavigation: DetailNavigationListener
) {
    uiData.customerData
    val food = uiData.foodData
    val similarFoods = uiData.foodSimilarData.orEmpty()
    val filteredFoods = similarFoods.filter { it.id != food?.id }

    var showSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            food?.let { food ->
                VariantBottomSheetContent(
                    food = food,
                    onAddToCart = { variants, qty, note ->
                        uiEvent.onAddToCart(
                            food.id,
                            variants,
                            qty,
                            note
                        )
                    },
                    onClose = { showSheet = false }
                )
            }
        }
    }

    if (uiState.foodAddToCartState is Resource.Success) {
        uiNavigation.onAddToCart()
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
                            AppColor.GreenSoft,
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

            item {
                if (!food?.images.isNullOrEmpty()) {
                    DetailImage(food.images)
                }
            }
            item { Spacer(Modifier.height(16.dp)) }

            item { DetailTitle(food) }
            item { Spacer(Modifier.height(6.dp)) }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PriceChange,
                        contentDescription = null,
                        tint = AppColor.Green
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = checkPrice(food),
                        color = Color.DarkGray,
                        fontSize = 16.sp,
                        fontFamily = PoppinsFont,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            item { Spacer(Modifier.height(6.dp)) }
            item {
                DetailLocation(
                    food = food,
                    onClickCafe = { uiNavigation.onClickCafe(it) }
                )
            }
            item { Spacer(Modifier.height(12.dp)) }

            item { DetailDescription(food) }
            item { Spacer(Modifier.height(20.dp)) }

            item { DetailStatsRow(food) }
            item { Spacer(Modifier.height(22.dp)) }

            item { SectionTitle("Menu lainnya") }
            item { Spacer(Modifier.height(12.dp)) }

            items(filteredFoods) { item ->
                SimilarItemRow(
                    foodResponse = item,
                    onClickSimilarFood = {
                        uiNavigation.onNavigateToDetail(item.id)
                    }
                )
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
            colors = ButtonDefaults.buttonColors(AppColor.Green),
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
fun DetailImage(
    images: List<String>
) {

    if (images.isEmpty()) return

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

            AsyncImage(
                model = images[page],
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.no_image),
                error = painterResource(R.drawable.no_image)
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
                            if (isSelected) Color.White else Color.LightGray,
                            CircleShape
                        )
                )
            }
        }
    }
}

@Composable
fun DetailTitle(food: FoodResponse?) {
    Text(
        text = food?.name ?: EMPTY_STRING,
        fontSize = 24.sp,
        fontFamily = PoppinsFont,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun DetailLocation(
    food: FoodResponse?,
    onClickCafe: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = Modifier
                .clickable {
                    food?.let { onClickCafe(it.cafeId) }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                tint = AppColor.Green
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = food?.cafeName.orEmpty(),
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
            tint = AppColor.Green
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = food?.cafeAddress.orEmpty(),
            color = Color.DarkGray,
            fontSize = 14.sp,
            fontFamily = PoppinsFont
        )
    }
}

@Composable
fun DetailDescription(food: FoodResponse?) {
    Text(
        text = food?.description ?: EMPTY_STRING,
        color = AppColor.Gray,
        fontSize = 14.sp,
        fontFamily = PoppinsFont
    )
}

@Composable
fun DetailStatsRow(food: FoodResponse?) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        StatusStatItem(food?.status ?: FoodStatus.UNKNOWN)

        StatItem(
            icon = Icons.Default.Inventory2,
            text = "Tersedia ${food?.quantity ?: 0} Pcs"
        )

        StatItem(
            icon = Icons.Default.AccessTime,
            text = food?.estimate ?: "-"
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
fun SimilarItemRow(
    foodResponse: FoodResponse,
    onClickSimilarFood: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color.Black.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            .padding(8.dp)
            .clickable {
                onClickSimilarFood()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = foodResponse.images.first(),
            contentDescription = foodResponse.name,
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
                text = foodResponse.name,
                fontWeight = FontWeight.Medium,
                fontFamily = PoppinsFont
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = foodResponse.price.toRupiah(),
                color = AppColor.Green,
                fontSize = 14.sp,
                fontFamily = PoppinsFont
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(end = 12.dp)
        ) {
            StatusStatItem(foodResponse.status)
        }
    }
}

@Composable
fun VariantBottomSheetContent(
    food: FoodResponse,
    onAddToCart: (List<CartVariantRequest>, Int, String) -> Unit,
    onClose: () -> Unit
) {

    var quantity by remember { mutableIntStateOf(1) }
    var note by remember { mutableStateOf(EMPTY_STRING) }

    val selectedOptions = remember {
        mutableStateMapOf<String, FoodOptionResponse>()
    }

    val variantPrice = selectedOptions.values.sumOf { it.price }
    val singlePrice = food.price + variantPrice
    val totalPrice = singlePrice * BigDecimal(quantity)

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

            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = AppColor.Green
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        food.variants.forEach { variant ->

            Text(
                text = variant.name,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PoppinsFont
            )

            Spacer(Modifier.height(8.dp))

            VariantSelectorDynamic(
                options = variant.options,
                selected = selectedOptions[variant.id],
                onSelect = {
                    selectedOptions[variant.id] = it
                }
            )

            Spacer(Modifier.height(16.dp))
        }

        Text(
            text = "Catatan",
            fontWeight = FontWeight.SemiBold,
            fontFamily = PoppinsFont
        )

        Spacer(Modifier.height(6.dp))

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            placeholder = {
                Text(
                    text = "Contoh: Jangan pakai sambal, nasi dipisah",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    fontFamily = PoppinsFont
                )
            },
            shape = RoundedCornerShape(12.dp),
            maxLines = 3
        )

        Spacer(Modifier.height(16.dp))

        HorizontalDivider()

        Spacer(Modifier.height(16.dp))

        Column {

            PriceRow("Harga", food.price)

            selectedOptions.values.forEach {
                if (it.price > BigDecimal.ZERO) {
                    PriceRow(it.name, it.price)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        QuantitySelector(
            quantity = quantity,
            onDecrease = {
                if (quantity > 1) quantity--
            },
            onIncrease = {
                quantity++
            }
        )

        Spacer(Modifier.height(12.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Total: ${totalPrice.toRupiah()}",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppColor.Green,
            fontFamily = PoppinsFont,
            textAlign = TextAlign.End
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {

                val variantRequests = selectedOptions.map { (variantId, option) ->
                    CartVariantRequest(
                        variantId = variantId,
                        optionId = option.id
                    )
                }

                onAddToCart(variantRequests, quantity, note)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(AppColor.Green),
            shape = RoundedCornerShape(16.dp)
        ) {

            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                tint = Color.White
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = "Tambah ($quantity)",
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = PoppinsFont
            )
        }
    }
}

@Composable
fun VariantSelectorDynamic(
    options: List<FoodOptionResponse>,
    selected: FoodOptionResponse?,
    onSelect: (FoodOptionResponse) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            val isSelected = option.id == selected?.id
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isSelected) AppColor.Green
                        else AppColor.Green.copy(alpha = 0.1f)
                    )
                    .clickable { onSelect(option) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {

                Text(
                    text = option.name,
                    color = if (isSelected) Color.White else AppColor.Green,
                    fontFamily = PoppinsFont
                )
            }
        }
    }
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppColor.Green.copy(alpha = 0.08f))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = "Jumlah",
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.SemiBold
        )

        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        if (quantity > 1) AppColor.Green else Color.Gray
                    )
                    .clickable {
                        if (quantity > 1) onDecrease()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("-", color = Color.White)
            }

            Spacer(Modifier.width(12.dp))

            Text(
                text = quantity.toString(),
                fontFamily = PoppinsFont
            )

            Spacer(Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(AppColor.Green)
                    .clickable { onIncrease() },
                contentAlignment = Alignment.Center
            ) {
                Text("+", color = Color.White)
            }
        }
    }
}

@Composable
fun PriceRow(
    label: String,
    price: BigDecimal
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = label,
            fontFamily = PoppinsFont
        )

        Text(
            text = "+ ${price.toRupiah()}",
            fontFamily = PoppinsFont
        )
    }

    Spacer(Modifier.height(6.dp))
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun DetailScreenPreview() {
    DetailScreen(
        uiState = DetailStateListener(),
        uiData = DetailDataListener(
            customerData = previewCustomer,
            foodData = previewFood,
            foodSimilarData = previewSimilar
        ),
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
                food = previewFood,
                onAddToCart = { _, _, _ -> },
                onClose = {}
            )

        }
    }
}

private val previewFood by lazy {
    FoodResponse(
        id = "1",
        cafeId = "1",
        name = "Bakso Telur Joss",
        cafeName = "Cafe Sejati",
        cafeAddress = "Puri Lestari - Blok G06/01",
        description = "Bakso enak dengan telur di dalamnya.",
        price = BigDecimal("30000"),
        category = FoodCategory.FOOD,
        status = FoodStatus.READY,
        quantity = 20,
        estimate = "10-15 menit",
        isActive = true,
        createdAt = LocalDateTime.parse("2026-03-07T13:48:00"),
        images = listOf("https://picsum.photos/400"),
        variants = listOf(
            FoodVariantResponse(
                id = "v1",
                name = "Ukuran",
                options = listOf(
                    FoodOptionResponse(
                        id = "o1",
                        name = "Regular",
                        price = BigDecimal.ZERO
                    ),
                    FoodOptionResponse(
                        id = "o2",
                        name = "Large",
                        price = BigDecimal(5000)
                    )
                )
            ),
            FoodVariantResponse(
                id = "v1",
                name = "Level",
                options = listOf(
                    FoodOptionResponse(
                        id = "o1",
                        name = "Tidak Pedas",
                        price = BigDecimal.ZERO
                    ),
                    FoodOptionResponse(
                        id = "o2",
                        name = "Sedang",
                        price = BigDecimal.ZERO
                    ),
                    FoodOptionResponse(
                        id = "o2",
                        name = "Pedas",
                        price = BigDecimal.ZERO
                    )
                )
            )
        )
    )
}

private val previewSimilar by lazy {
    listOf(
        FoodResponse(
            id = "2",
            cafeId = "1",
            name = "Bakso Urat",
            cafeName = "Cafe Sejati",
            cafeAddress = "Puri Lestari - Blok G06/01",
            description = "Bakso urat kenyal",
            price = BigDecimal("25000"),
            category = FoodCategory.FOOD,
            status = FoodStatus.READY,
            quantity = 30,
            estimate = "10 menit",
            isActive = true,
            createdAt = LocalDateTime.parse("2026-03-07T13:48:00"),
            images = listOf("https://picsum.photos/200"),
            variants = emptyList()
        ),
        FoodResponse(
            id = "3",
            cafeId = "1",
            name = "Bakso Telur Joss",
            cafeName = "Cafe Sejati",
            cafeAddress = "Puri Lestari - Blok G06/01",
            description = "Bakso pedas dengan sambal mercon.",
            price = BigDecimal(32000),
            category = FoodCategory.FOOD,
            status = FoodStatus.READY,
            quantity = 15,
            estimate = "12 menit",
            isActive = true,
            createdAt = LocalDateTime.parse("2026-03-07T13:48:00"),
            images = listOf("https://picsum.photos/200"),
            variants = emptyList()
        )
    )
}

val previewCustomer = CustomerResponse(
    name = "Dedy Wijaya",
    phone = "08123456789",
    email = "boys.mtv@gmail.com",
    address = AddressResponse(
        id = "1",
        village = "Puri Lestari",
        block = "H2",
        number = "21",
        rt = "012",
        rw = "002",
        isDefault = true
    ),
    photo = EMPTY_STRING,
    verified = true,
    stats = Stats(0, 0, EMPTY_STRING),
    menuSummary = MenuSummary(0, 0, 0, 0, 0)
)