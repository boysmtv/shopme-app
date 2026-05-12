package com.mtv.app.shopme.feature.customer.ui.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.ShimmerBlock
import com.mtv.app.shopme.common.ShimmerLine

@Composable
fun ShimmerHomeContentSkeleton() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ShimmerLine(widthFraction = 0.62f, heightDp = 24)
            ShimmerBlock(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp)
            )
            ShimmerBlock(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                shape = RoundedCornerShape(20.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(5) {
                    Column(
                        modifier = Modifier.width(80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ShimmerBlock(
                            modifier = Modifier.size(80.dp),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }
            }
            ShimmerLine(widthFraction = 0.35f, heightDp = 18)
        }

        ShimmerHomeFoodFlow()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ShimmerHomeFoodFlow() {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val cardWidth = (maxWidth - 16.dp) / 2

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            maxItemsInEachRow = 2
        ) {
            repeat(6) { index ->
                ShimmerHomeFoodCard(
                    modifier = Modifier.width(cardWidth),
                    imageHeight = if (index % 3 == 0) 112.dp else 100.dp
                )
            }
        }
    }
}

@Composable
private fun ShimmerHomeFoodCard(
    modifier: Modifier = Modifier,
    imageHeight: androidx.compose.ui.unit.Dp = 100.dp
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {
            Box {
                ShimmerBlock(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight),
                    shape = RoundedCornerShape(0.dp)
                )
                ShimmerBlock(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(22.dp),
                    shape = CircleShape
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                ShimmerLine(widthFraction = 0.82f, heightDp = 15)
                Spacer(Modifier.height(6.dp))
                ShimmerLine(widthFraction = 0.45f, heightDp = 14)
                Spacer(Modifier.height(4.dp))
                ShimmerLine(widthFraction = 0.64f, heightDp = 12)
            }
        }
    }
}

@Composable
fun ShimmerDetailScreen() {
    ScaffoldColumnWithBottomBar(
        content = {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { ShimmerDetailHeader() }
                item { ShimmerBlock(modifier = Modifier.fillMaxWidth().height(240.dp), shape = RoundedCornerShape(8.dp)) }
                item { ShimmerLine(widthFraction = 0.58f, heightDp = 28) }
                item { ShimmerLine(widthFraction = 0.34f, heightDp = 18) }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ShimmerBlock(modifier = Modifier.size(20.dp), shape = CircleShape)
                            Spacer(Modifier.width(6.dp))
                            ShimmerLine(widthFraction = 0.28f, heightDp = 14)
                            Spacer(Modifier.width(12.dp))
                            ShimmerBlock(modifier = Modifier.size(20.dp), shape = CircleShape)
                            Spacer(Modifier.width(6.dp))
                            ShimmerLine(widthFraction = 0.42f, heightDp = 14)
                        }
                        repeat(3) {
                            ShimmerLine(widthFraction = if (it == 2) 0.6f else 1f, heightDp = 13)
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        repeat(3) {
                            ShimmerBlock(
                                modifier = Modifier
                                    .width(104.dp)
                                    .height(42.dp),
                                shape = RoundedCornerShape(14.dp)
                            )
                        }
                    }
                }
                item { ShimmerLine(widthFraction = 0.3f, heightDp = 16) }
                items(3) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.7f))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ShimmerBlock(
                            modifier = Modifier.size(42.dp),
                            shape = RoundedCornerShape(4.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            ShimmerLine(widthFraction = 0.7f, heightDp = 14)
                            Spacer(Modifier.height(6.dp))
                            ShimmerLine(widthFraction = 0.35f, heightDp = 12)
                        }
                        Spacer(Modifier.width(12.dp))
                        ShimmerBlock(
                            modifier = Modifier
                                .width(72.dp)
                                .height(24.dp),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ShimmerBlock(
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp)
                )
                ShimmerBlock(
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }
    )
}

@Composable
private fun ShimmerDetailHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ShimmerBlock(modifier = Modifier.size(48.dp), shape = CircleShape)
        ShimmerLine(
            modifier = Modifier.padding(horizontal = 16.dp),
            widthFraction = 0.34f,
            heightDp = 18
        )
        ShimmerBlock(modifier = Modifier.size(48.dp), shape = CircleShape)
    }
}

@Composable
fun ShimmerProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                androidx.compose.ui.graphics.Brush.verticalGradient(
                    listOf(AppColor.Green, AppColor.GreenSoft)
                )
            )
            .padding(top = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ShimmerBlock(modifier = Modifier.size(80.dp), shape = RoundedCornerShape(24.dp))
                Spacer(Modifier.width(16.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ShimmerLine(widthFraction = 0.52f, heightDp = 20)
                    ShimmerLine(widthFraction = 0.38f, heightDp = 14)
                    ShimmerLine(widthFraction = 0.48f, heightDp = 12)
                }
            }

            Spacer(Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(3) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            ShimmerLine(widthFraction = 0.22f, heightDp = 16)
                            Spacer(Modifier.height(6.dp))
                            ShimmerLine(widthFraction = 0.28f, heightDp = 12)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = AppColor.WhiteSoft)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                ShimmerLine(widthFraction = 0.32f, heightDp = 18)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(4) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            ShimmerBlock(
                                modifier = Modifier.size(55.dp),
                                shape = RoundedCornerShape(16.dp)
                            )
                            Spacer(Modifier.height(8.dp))
                            ShimmerLine(widthFraction = 0.5f, heightDp = 11)
                        }
                    }
                }
                repeat(6) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ShimmerBlock(
                            modifier = Modifier.size(22.dp),
                            shape = RoundedCornerShape(8.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            ShimmerLine(widthFraction = 0.45f, heightDp = 14)
                        }
                        Spacer(Modifier.width(12.dp))
                        ShimmerBlock(
                            modifier = Modifier.size(18.dp),
                            shape = RoundedCornerShape(9.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScaffoldColumnWithBottomBar(
    content: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                androidx.compose.ui.graphics.Brush.verticalGradient(
                    listOf(AppColor.GreenSoft, AppColor.WhiteSoft, AppColor.White)
                )
            )
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            content()
        }
        bottomBar()
    }
}
