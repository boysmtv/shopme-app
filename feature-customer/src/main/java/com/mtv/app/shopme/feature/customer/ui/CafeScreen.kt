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
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.toRupiah
import com.mtv.app.shopme.data.mock.DataUiMock
import com.mtv.app.shopme.domain.model.Cafe
import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.domain.model.FoodStatus
import com.mtv.app.shopme.feature.customer.contract.CafeEvent
import com.mtv.app.shopme.feature.customer.contract.CafeUiState
import com.mtv.app.shopme.feature.customer.utils.displayImage
import com.mtv.app.shopme.feature.customer.utils.displayPrice
import com.mtv.app.shopme.feature.customer.utils.isAvailable
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.UiError

@Composable
fun CafeScreen(
    state: CafeUiState,
    event: (CafeEvent) -> Unit
) {
    val cafe = (state.cafe as? LoadState.Success)?.data
    val foods = (state.foods as? LoadState.Success)?.data.orEmpty()

    Scaffold(
        topBar = {
            CafeToolbar(
                onBack = { event(CafeEvent.ClickBack) },
                onSearchClick = {}
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            item {
                CafeHeader(
                    cafe = cafe,
                    onChatClick = { event(CafeEvent.ClickChat) },
                    onWhatsappClick = { event(CafeEvent.ClickWhatsapp) }
                )

                Spacer(Modifier.height(16.dp))

                Text("Daftar Menu")
            }

            gridItems(
                data = foods,
                columnCount = 2,
                horizontalSpacing = 16.dp,
                verticalSpacing = 16.dp
            ) { item ->
                CafeFoodItem(
                    item = item,
                    onClickDetail = {
                        event(CafeEvent.ClickFood(it))
                    }
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
            .background(AppColor.White, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column {

            Row(verticalAlignment = Alignment.CenterVertically) {

                AsyncImage(
                    model = cafe?.image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.width(16.dp))

                Column {
                    Text(
                        text = cafe?.name.orEmpty(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = PoppinsFont
                    )

                    Text(
                        text = cafe?.address?.name.orEmpty(),
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = "⏰ ${cafe?.openTime} - ${cafe?.closeTime}",
                        fontSize = 12.sp,
                        color = AppColor.Green
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Minimal Order ${cafe?.minimalOrder?.toRupiah()}",
                color = AppColor.Green
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = cafe?.description.orEmpty(),
                fontSize = 12.sp,
                color = AppColor.Gray
            )

            Spacer(Modifier.height(12.dp))

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
    val image = item.displayImage()
    val price = item.displayPrice()
    val isAvailable = item.isAvailable()

    Card(
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.then(
            if (isAvailable) Modifier.clickable { onClickDetail(item.id) }
            else Modifier
        )
    ) {
        Column {

            Box {
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentScale = ContentScale.Crop
                )

                if (!isAvailable) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Black.copy(0.5f))
                    )

                    Text(
                        text = when (item.status) {
                            FoodStatus.PREORDER -> "Pre Order"
                            FoodStatus.JASTIP -> "Jastip"
                            else -> "Tidak Tersedia"
                        },
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Column(Modifier.padding(12.dp)) {

                Text(
                    text = item.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = price.toRupiah(),
                    color = AppColor.Green,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = item.category.label,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

val previewState = CafeUiState(
    cafe = LoadState.Success(DataUiMock.cafe()),
    foods = LoadState.Success(DataUiMock.foods())
)

@Preview(
    name = "Cafe Screen",
    showBackground = true,
    device = Devices.PIXEL_4_XL
)
@Composable
fun CafeScreenPreview() {
    CafeScreen(
        state = previewState,
        event = {}
    )
}

@Preview(name = "Error")
@Composable
fun CafeScreenErrorPreview() {
    CafeScreen(
        state = CafeUiState(
            cafe = LoadState.Error(UiError.Validation("Error")),
            foods = LoadState.Error(UiError.Validation("Error"))
        ),
        event = {}
    )
}