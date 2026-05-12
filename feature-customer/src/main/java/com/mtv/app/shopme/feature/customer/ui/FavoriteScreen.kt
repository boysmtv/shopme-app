package com.mtv.app.shopme.feature.customer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.ContentErrorState
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.feature.customer.contract.FavoriteEvent
import com.mtv.app.shopme.feature.customer.contract.FavoriteUiState
import com.mtv.app.shopme.feature.customer.ui.shimmer.ShimmerSearchScreen
import com.mtv.based.core.network.utils.LoadState

@Composable
fun FavoriteScreen(
    state: FavoriteUiState,
    event: (FavoriteEvent) -> Unit
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
        FavoriteHeader(onBack = { event(FavoriteEvent.ClickBack) })

        Spacer(modifier = Modifier.height(16.dp))

        when (val foods = state.foods) {
            is LoadState.Loading -> ShimmerSearchScreen()
            is LoadState.Error -> ContentErrorState(
                title = "Gagal memuat favorit",
                message = foods.error.message,
                actionLabel = "Muat ulang",
                onRetry = { event(FavoriteEvent.Load) }
            )
            is LoadState.Success -> {
                if (foods.data.isEmpty()) {
                    EmptyFavoriteState()
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(
                            items = foods.data,
                            key = { _, item -> item.id }
                        ) { index, food ->
                            SearchItem(
                                food = food,
                                isFavorite = state.favoriteIds.contains(food.id),
                                onClickItem = { event(FavoriteEvent.ClickFood(it)) },
                                onToggleFavorite = { event(FavoriteEvent.ToggleFavorite(food.id)) },
                                previewDrawable = getPreviewDrawable(index)
                            )
                        }
                    }
                }
            }
            else -> Unit
        }
    }
}

@Composable
private fun FavoriteHeader(onBack: () -> Unit) {
    androidx.compose.foundation.layout.Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 16.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = AppColor.Green
            )
        }
        Text(
            text = "Favorit Saya",
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = Color.Black
        )
    }
}

@Composable
private fun EmptyFavoriteState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Belum ada makanan favorit",
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Simpan makanan yang Anda suka dari home, cafe, search, atau detail produk.",
            fontFamily = PoppinsFont,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}
