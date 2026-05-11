/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerCreateCafeTncScreen.kt
 *
 * Last modified by Dedy Wijaya on 14/03/26 13.10
 */

package com.mtv.app.shopme.feature.seller.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.ShimmerBlock
import com.mtv.app.shopme.common.ShimmerLine
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeTncEvent
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeTncUiState

@Composable
fun SellerCreateCafeTncScreen(
    state: SellerCreateCafeTncUiState,
    event: (SellerCreateCafeTncEvent) -> Unit
) {

    val isValid = state.terms.isNotEmpty() && state.terms.all { it.checked }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            if (state.isLoading && state.terms.isEmpty()) {
                SellerCreateCafeTncShimmer()
            } else {

                Text(
                    text = state.title.ifBlank { "Create Your Cafe" },
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PoppinsFont
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = state.description.ifBlank {
                        "Before continuing to create your cafe profile, please read and agree to the following terms and conditions. These rules are designed to ensure that all cafes listed on the platform provide a safe, reliable, and trustworthy experience for customers."
                    },
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = PoppinsFont
                )

                Spacer(Modifier.height(24.dp))

                state.terms.forEachIndexed { index, item ->
                    SellerTncCard(
                        checked = item.checked,
                        title = item.title,
                        description = item.description,
                        onCheckedChange = {
                            event(SellerCreateCafeTncEvent.ToggleTerm(item.id, it))
                        }
                    )

                    if (index < state.terms.lastIndex) {
                        Spacer(Modifier.height(16.dp))
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = state.footer.ifBlank {
                        "By continuing, you acknowledge that you have read and agreed to the terms above. These agreements help maintain the quality of cafes on our platform and ensure a better experience for all users."
                    },
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontFamily = PoppinsFont
                )
            }
        }

        Button(
            onClick = { event(SellerCreateCafeTncEvent.Next) },
            enabled = isValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColor.Blue
            )
        ) {

            Text(
                text = "Continue to Create Cafe",
                color = Color.White,
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun SellerCreateCafeTncShimmer() {
    ShimmerLine(widthFraction = 0.46f, heightDp = 28)
    Spacer(Modifier.height(10.dp))
    ShimmerLine(widthFraction = 0.92f, heightDp = 14)
    Spacer(Modifier.height(6.dp))
    ShimmerLine(widthFraction = 0.88f, heightDp = 14)
    Spacer(Modifier.height(24.dp))

    repeat(3) { index ->
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F9FC))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                ShimmerBlock(
                    modifier = Modifier
                        .width(22.dp)
                        .height(22.dp),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    ShimmerLine(widthFraction = 0.58f, heightDp = 14)
                    Spacer(Modifier.height(8.dp))
                    ShimmerLine(widthFraction = 0.94f, heightDp = 12)
                    Spacer(Modifier.height(6.dp))
                    ShimmerLine(widthFraction = 0.82f, heightDp = 12)
                }
            }
        }
        if (index < 2) {
            Spacer(Modifier.height(16.dp))
        }
    }

    Spacer(Modifier.height(24.dp))
    ShimmerLine(widthFraction = 0.9f, heightDp = 13)
    Spacer(Modifier.height(6.dp))
    ShimmerLine(widthFraction = 0.84f, heightDp = 13)
}

@Composable
fun SellerTncCard(
    checked: Boolean,
    title: String,
    description: String,
    onCheckedChange: (Boolean) -> Unit
) {

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF7F9FC)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {

            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange
            )

            Spacer(Modifier.width(8.dp))

            Column {

                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PoppinsFont
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontFamily = PoppinsFont
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SellerCreateCafeTncScreenPreview() {
    SellerCreateCafeTncScreen(
        state = SellerCreateCafeTncUiState(
            title = "Create Your Cafe",
            description = "Before continuing, please read and agree to the following terms.",
            footer = "By continuing, you agree to the seller terms.",
            terms = listOf(
                com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeTncItemUiState(
                    id = "term-1",
                    title = "Accurate Cafe Information",
                    description = "All cafe information must be accurate.",
                    checked = true
                ),
                com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeTncItemUiState(
                    id = "term-2",
                    title = "Food Safety and Quality Compliance",
                    description = "All food must meet hygiene standards.",
                    checked = false
                )
            )
        ),
        event = {}
    )
}
