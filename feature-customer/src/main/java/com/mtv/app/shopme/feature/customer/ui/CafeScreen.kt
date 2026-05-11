/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CafeScreen.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.feature.customer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.common.SmartImage
import com.mtv.app.shopme.common.toRupiah
import com.mtv.app.shopme.data.mock.DataUiMock
import com.mtv.app.shopme.domain.model.Cafe
import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.feature.customer.contract.CafeEvent
import com.mtv.app.shopme.feature.customer.contract.CafeUiState
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

@Composable
fun CafeScreen(
    state: CafeUiState,
    event: (CafeEvent) -> Unit
) {

    val cafe = (state.cafe as? LoadState.Success)?.data
    val foods = (state.foods as? LoadState.Success)?.data ?: emptyList()

    Scaffold(
        topBar = {
            CafeToolbar(
                onBack = { event(CafeEvent.ClickBack) },
                onSearchClick = {}
            )
        },
        containerColor = Color.Transparent
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            AppColor.GreenSoft,
                            AppColor.WhiteSoft,
                            AppColor.White
                        )
                    )
                )
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {

            item {
                CafeHeader(
                    cafe = cafe,
                    onChatClick = { event(CafeEvent.ClickChat) },
                    onWhatsappClick = { event(CafeEvent.ClickWhatsapp) },
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Daftar Menu",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PoppinsFont,
                    fontSize = 16.sp,
                )

                Spacer(Modifier.height(12.dp))
            }

            gridItems(
                data = foods,
                columnCount = 2,
                horizontalSpacing = 16.dp,
                verticalSpacing = 16.dp
            ) { item ->
                CafeFoodItem(
                    item = item,
                    onClickDetail = { event(CafeEvent.ClickFood(it)) }
                )
            }
        }
    }
}


@Composable
fun CafeToolbar(
    onBack: () -> Unit,
    onSearchClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable { onBack() }
                .padding(12.dp)
        )

        Spacer(Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(Color.White)
                .clickable { onSearchClick() }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.Gray
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Search food...",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontFamily = PoppinsFont
                )
            }
        }
    }
}

@Composable
fun CafeHeader(
    cafe: Cafe?,
    onChatClick: () -> Unit,
    onWhatsappClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = AppColor.White,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SmartImage(
                    model = cafe?.image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.image_burger),
                    error = painterResource(R.drawable.image_burger)
                )

                Spacer(Modifier.width(16.dp))

                Column {
                    Text(
                        text = cafe?.name.orEmpty(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = PoppinsFont
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = cafe?.address?.name ?: EMPTY_STRING,
                        fontSize = 13.sp,
                        color = Color.Gray,
                        fontFamily = PoppinsFont
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "⏰ ${cafe?.openTime} - ${cafe?.closeTime}",
                        fontSize = 12.sp,
                        color = AppColor.Green,
                        fontFamily = PoppinsFont
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(AppColor.Green.copy(alpha = 0.1f))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = AppColor.Green,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(Modifier.width(6.dp))

                Text(
                    text = "Minimal Order ${cafe?.minimalOrder?.toRupiah()}",
                    fontSize = 12.sp,
                    color = AppColor.Green,
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = cafe?.description.orEmpty(),
                fontSize = 12.sp,
                color = AppColor.Gray,
                fontFamily = PoppinsFont,
                lineHeight = 16.sp
            )

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButton(
                    text = "Chat",
                    icon = Icons.AutoMirrored.Filled.Chat,
                    background = AppColor.Green,
                    textColor = Color.White,
                    modifier = Modifier.weight(1f),
                    onClick = { onChatClick() }
                )

                ActionButton(
                    text = "WhatsApp",
                    icon = Icons.Default.Phone,
                    background = Color(0xFF25D366),
                    textColor = Color.White,
                    modifier = Modifier.weight(1f),
                    onClick = { onWhatsappClick() }
                )
            }
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: ImageVector,
    background: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(background)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = PoppinsFont
        )
    }
}


@Composable
fun CafeFoodItem(
    item: Food,
    onClickDetail: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .then(
                if (item.isActive) {
                    Modifier.clickable { onClickDetail(item.id) }
                } else {
                    Modifier
                }
            )
    ) {
        Column {
            Box {
                Box {
                    SmartImage(
                        model = item.images.firstOrNull(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.no_image),
                        error = painterResource(R.drawable.no_image)
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

                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(22.dp)
                        .background(
                            Color.Black.copy(alpha = 0.4f),
                            CircleShape
                        )
                        .padding(4.dp)
                )
            }

            Column(Modifier.padding(12.dp)) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = PoppinsFont
                )

                Spacer(Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = item.price.toRupiah(),
                        color = AppColor.Green,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = PoppinsFont
                    )

                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = AppColor.Green,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Preview(
    name = "Cafe Screen",
    showBackground = true,
    device = Devices.PIXEL_4_XL
)
@Composable
fun CafeScreenPreview() {
    CafeScreen(
        state = CafeUiState(
            cafe = LoadState.Success(DataUiMock.cafe()),
            foods = LoadState.Success(DataUiMock.foods())
        ),
        event = {}
    )
}
